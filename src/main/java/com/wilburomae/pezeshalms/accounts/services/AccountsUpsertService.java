package com.wilburomae.pezeshalms.accounts.services;

import com.wilburomae.pezeshalms.accounts.data.entities.*;
import com.wilburomae.pezeshalms.accounts.data.repositories.*;
import com.wilburomae.pezeshalms.accounts.dtos.AccountBalanceRequest;
import com.wilburomae.pezeshalms.accounts.dtos.AccountRequest;
import com.wilburomae.pezeshalms.common.dtos.Response;
import com.wilburomae.pezeshalms.common.services.UpsertService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

@Service
public class AccountsUpsertService implements UpsertService<AccountRequest> {

    private final AccountRepository accountRepository;
    private final AccountTypeRepository accountTypeRepository;
    private final AccountStatusRepository accountStatusRepository;
    private final AccountBalanceRepository accountBalanceRepository;
    private final CurrencyRepository currencyRepository;

    public AccountsUpsertService(AccountRepository accountRepository, AccountTypeRepository accountTypeRepository, AccountStatusRepository accountStatusRepository, AccountBalanceRepository accountBalanceRepository, CurrencyRepository currencyRepository) {
        this.accountRepository = accountRepository;
        this.accountTypeRepository = accountTypeRepository;
        this.accountStatusRepository = accountStatusRepository;
        this.accountBalanceRepository = accountBalanceRepository;
        this.currencyRepository = currencyRepository;
    }

    @Transactional
    @Override
    public Response<Long> upsert(Long id, AccountRequest request) {
        Response<AccountEntity> initResponse = initEntity(id, accountRepository, AccountEntity::new);
        if (initResponse.responseCode().isError()) {
            return new Response<>(NOT_FOUND, "Account not found", null);
        }
        AccountEntity entity = initResponse.data();
        entity.setName(request.name());
        entity.setDescription(request.description());

        Optional<AccountTypeEntity> foundType = accountTypeRepository.findById(request.typeId());
        if (foundType.isEmpty()) {
            return new Response<>(NOT_FOUND, "Account type not found", null);
        }
        entity.setAccountType(foundType.get());

        Optional<AccountStatusEntity> foundStatus = accountStatusRepository.findById(request.statusId());
        if (foundStatus.isEmpty()) {
            return new Response<>(NOT_FOUND, "Account status not found", null);
        }
        entity.setAccountStatus(foundStatus.get());
        entity.setOverdrawable(request.overdrawable());

        Response<Void> balancesResponse = processBalances(entity, request.balances());
        if (balancesResponse.responseCode().isError()) {
            return new Response<>(balancesResponse.responseCode(), balancesResponse.responseDesc(), null);
        }

        entity = accountRepository.save(entity);
        return successResponse(id, "Account", entity);
    }

    private Response<Void> processBalances(AccountEntity entity, List<AccountBalanceRequest> balances) {
        for (AccountBalanceRequest balance : balances) {
            Optional<CurrencyEntity> foundCurrency = currencyRepository.findById(balance.currencyId());
            if (foundCurrency.isEmpty()) {
                return new Response<>(NOT_FOUND, "Currency not found", null);
            }

            AccountBalanceEntity balanceEntity;
            if (balance.id() == null) {
                balanceEntity = new AccountBalanceEntity();
            } else {
                Optional<AccountBalanceEntity> foundBalance = accountBalanceRepository.findById(balance.id());
                if (foundBalance.isEmpty()) {
                    return new Response<>(NOT_FOUND, "Account balance not found", null);
                }
                balanceEntity = foundBalance.get();
            }
            balanceEntity.setAccount(entity);
            balanceEntity.setCurrency(foundCurrency.get());
            balanceEntity.setBalanceMinor(balance.balanceMinor());
            balanceEntity.setUpperThresholdMinor(balance.upperThresholdMinor());
            balanceEntity.setLowerThresholdMinor(balance.lowerThresholdMinor());

            entity.addBalance(balanceEntity);
        }

        return new Response<>(OK, "Balances processed", null);
    }
}
