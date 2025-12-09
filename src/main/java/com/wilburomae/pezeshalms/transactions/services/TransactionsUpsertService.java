package com.wilburomae.pezeshalms.transactions.services;

import com.wilburomae.pezeshalms.accounts.data.entities.AccountEntity;
import com.wilburomae.pezeshalms.accounts.data.entities.AccountTypeEntity;
import com.wilburomae.pezeshalms.accounts.data.entities.CurrencyEntity;
import com.wilburomae.pezeshalms.accounts.data.repositories.AccountRepository;
import com.wilburomae.pezeshalms.accounts.data.repositories.AccountTypeRepository;
import com.wilburomae.pezeshalms.accounts.data.repositories.CurrencyRepository;
import com.wilburomae.pezeshalms.common.dtos.Response;
import com.wilburomae.pezeshalms.common.services.UpsertService;
import com.wilburomae.pezeshalms.transactions.data.entities.TransactionEntity;
import com.wilburomae.pezeshalms.transactions.data.entities.TransactionEntryEntity;
import com.wilburomae.pezeshalms.transactions.data.entities.TransactionTypeEntity;
import com.wilburomae.pezeshalms.transactions.data.repositories.TransactionRepository;
import com.wilburomae.pezeshalms.transactions.data.repositories.TransactionTypeRepository;
import com.wilburomae.pezeshalms.transactions.dtos.TransactionEntryRequest;
import com.wilburomae.pezeshalms.transactions.dtos.TransactionRequest;
import com.wilburomae.pezeshalms.users.data.entities.UserEntity;
import com.wilburomae.pezeshalms.users.data.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.springframework.http.HttpStatus.*;

@Service
public class TransactionsUpsertService implements UpsertService<TransactionRequest> {

    private final TransactionRepository transactionRepository;
    private final TransactionTypeRepository transactionTypeRepository;
    private final CurrencyRepository currencyRepository;
    private final AccountRepository accountRepository;
    private final AccountTypeRepository accountTypeRepository;
    private final UserRepository userRepository;
    private final TransactionReferenceGenerator transactionReferenceGenerator;
    private final AccountBalanceCalculator accountBalanceCalculator = new AccountBalanceCalculator();

    public TransactionsUpsertService(TransactionRepository transactionRepository, TransactionTypeRepository transactionTypeRepository, CurrencyRepository currencyRepository, AccountRepository accountRepository, AccountTypeRepository accountTypeRepository, UserRepository userRepository, TransactionReferenceGenerator transactionReferenceGenerator) {
        this.transactionRepository = transactionRepository;
        this.transactionTypeRepository = transactionTypeRepository;
        this.currencyRepository = currencyRepository;
        this.accountRepository = accountRepository;
        this.accountTypeRepository = accountTypeRepository;
        this.userRepository = userRepository;
        this.transactionReferenceGenerator = transactionReferenceGenerator;
    }

    @Transactional
    @Override
    public Response<Long> upsert(Long id, TransactionRequest request) {
        Optional<TransactionEntity> duplicateTransaction = transactionRepository.checkDuplicate(id, request.idempotencyKey());
        if (duplicateTransaction.isPresent()) {
            return new Response<>(OK, "Transaction already exists", duplicateTransaction.get().getId());
        }

        Map<Long, CurrencyEntity> currencies = new HashMap<>();
        for (CurrencyEntity currency : currencyRepository.findAll()) {
            currencies.put(currency.getId(), currency);
        }

        Optional<TransactionTypeEntity> foundTransactionType = transactionTypeRepository.findById(request.typeId());
        if (foundTransactionType.isEmpty()) {
            return new Response<>(NOT_FOUND, "Transaction type not found", null);
        }

        Optional<UserEntity> foundOppositeParty = userRepository.findById(request.oppositePartyId());
        if (foundOppositeParty.isEmpty()) {
            return new Response<>(NOT_FOUND, "Opposite party not found", null);
        }

        TransactionEntity entity = new TransactionEntity();
        entity.setIdempotencyKey(request.idempotencyKey());
        entity.setTransactionReference(transactionReferenceGenerator.generateReference());
        entity.setOppositeParty(foundOppositeParty.get());
        entity.setTransactionType(foundTransactionType.get());

        Response<Void> entriesResponse = processEntries(entity, currencies, request.entries());
        if (entriesResponse.responseCode().isError()) {
            return new Response<>(entriesResponse.responseCode(), entriesResponse.responseDesc(), null);
        }

        entity = transactionRepository.save(entity);
        return successResponse(id, "Transaction", entity);
    }

    private Response<Void> processEntries(TransactionEntity entity, Map<Long, CurrencyEntity> currencies, List<TransactionEntryRequest> entries) {
        Map<Long, AccountEntity> accounts = new HashMap<>();
        accountRepository.findAll().forEach(account -> accounts.put(account.getId(), account));

        long totalDebitsMinor = 0;
        long totalCreditsMinor = 0;
        for (TransactionEntryRequest entry : entries) {
            AccountEntity account = accounts.get(entry.accountId());
            if (account == null) {
                return new Response<>(NOT_FOUND, "Account not found", null);
            }

            CurrencyEntity currency = currencies.get(entry.currencyId());
            if (currency == null) {
                return new Response<>(NOT_FOUND, "Currency not found", null);
            }

            AccountTypeEntity accountType = account.getAccountType();
            Optional<AccountTypeEntity> foundRootAccountType = accountTypeRepository.fetchRoot(accountType.getId());
            if (foundRootAccountType.isEmpty()) {
                return new Response<>(INTERNAL_SERVER_ERROR, "Root account type not found", null);
            }
            AccountTypeEntity rootAccountType = foundRootAccountType.get();

            Response<Long> deltaResponse = accountBalanceCalculator.calculateDelta(rootAccountType, entry.debitAmountMinor(), entry.creditAmountMinor());
            if (deltaResponse.responseCode().isError()) {
                return new Response<>(deltaResponse.responseCode(), deltaResponse.responseDesc(), null);
            }

            Response<Long> balanceResponse = account.modifyAccountBalance(currency.getId(), deltaResponse.data());
            if (balanceResponse.responseCode().isError()) {
                return new Response<>(balanceResponse.responseCode(), balanceResponse.responseDesc(), null);
            }

            TransactionEntryEntity entryEntity = buildEntryEntity(entry.debitAmountMinor(), entry.creditAmountMinor(), currency, account, balanceResponse.data());
            entryEntity.setTransaction(entity);
            entity.getTransactionEntries().add(entryEntity);

            totalDebitsMinor += entry.debitAmountMinor();
            totalCreditsMinor += entry.creditAmountMinor();
        }

        if (totalDebitsMinor != totalCreditsMinor) {
            return new Response<>(BAD_REQUEST, "Debit and credit amounts do not match", null);
        }

        return new Response<>(OK, "Transaction entries processed", null);
    }

    private TransactionEntryEntity buildEntryEntity(long debitAmountMinor, long creditAmountMinor, CurrencyEntity currency, AccountEntity account, long newBalanceMinor) {
        TransactionEntryEntity entryEntity = new TransactionEntryEntity();
        entryEntity.setTransactionReference(transactionReferenceGenerator.generateReference());
        entryEntity.setDebitAmountMinor(debitAmountMinor);
        entryEntity.setCreditAmountMinor(creditAmountMinor);
        entryEntity.setCurrency(currency);
        entryEntity.setAccount(account);
        entryEntity.setAccountBalanceMinor(newBalanceMinor);

        return entryEntity;
    }
}
