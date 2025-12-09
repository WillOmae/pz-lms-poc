package com.wilburomae.pezeshalms.integrationtests;

import com.wilburomae.pezeshalms.accounts.data.entities.*;
import com.wilburomae.pezeshalms.accounts.data.repositories.AccountRepository;
import com.wilburomae.pezeshalms.accounts.data.repositories.AccountStatusRepository;
import com.wilburomae.pezeshalms.accounts.data.repositories.AccountTypeRepository;
import com.wilburomae.pezeshalms.accounts.data.repositories.CurrencyRepository;
import com.wilburomae.pezeshalms.common.utilities.CollectionUtilities;
import com.wilburomae.pezeshalms.helpers.IntegrationTestHelper;
import com.wilburomae.pezeshalms.transactions.data.entities.TransactionTypeEntity;
import com.wilburomae.pezeshalms.transactions.data.repositories.TransactionTypeRepository;
import com.wilburomae.pezeshalms.transactions.dtos.TransactionEntryRequest;
import com.wilburomae.pezeshalms.transactions.dtos.TransactionRequest;
import com.wilburomae.pezeshalms.transactions.dtos.TransactionType;
import com.wilburomae.pezeshalms.users.data.entities.UserEntity;
import com.wilburomae.pezeshalms.users.data.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.security.SecureRandom;
import java.util.*;

import static com.wilburomae.pezeshalms.integrationtests.TransactionsIntegrationTests.TransactionGenerator.*;
import static java.util.Collections.emptyMap;
import static org.springframework.http.HttpStatus.*;

public class TransactionsIntegrationTests extends BaseIntegrationTests {

    private static final Random RANDOM = new SecureRandom();
    private static final String BASE_URL = "/transactions/transactions";
    @Autowired
    TransactionTypeRepository transactionTypeRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    CurrencyRepository currencyRepository;
    @Autowired
    AccountTypeRepository accountTypeRepository;
    @Autowired
    AccountStatusRepository accountStatusEntityRepository;
    private TransactionGenerator transactionGenerator;

    @BeforeEach
    void setup() {
        if (transactionGenerator == null) {
            transactionGenerator = new TransactionGenerator(integrationTestHelper, transactionTypeRepository, userRepository, accountRepository, currencyRepository, accountTypeRepository, accountStatusEntityRepository);
        }
    }

    @Test
    void whenCreateNew_thenReturnHttp201() throws Exception {
        transactionGenerator.createRequest(LOAN_DISBURSEMENT_TRANSACTION_TYPE, 120000, CURRENCY_KES);
        transactionGenerator.createRequest(LOAN_DISBURSEMENT_TRANSACTION_TYPE, 120000, CURRENCY_USD);
        transactionGenerator.createRequest(LOAN_DISBURSEMENT_TRANSACTION_TYPE, 120000, CURRENCY_UGX);
    }

    @Test
    void whenCreateDuplicate_thenReturnHttp200() throws Exception {
        // idempotency
        Map.Entry<Long, TransactionRequest> created = transactionGenerator.createRequest(LOAN_DISBURSEMENT_TRANSACTION_TYPE, 120000, CURRENCY_KES);

        Long result = integrationTestHelper.create(BASE_URL, created.getValue(), Long.class, OK);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(created.getKey(), result);
    }

    @Test
    void whenFetchExistingById_thenReturnHttp200() throws Exception {
        Map.Entry<Long, TransactionRequest> created = transactionGenerator.createRequest(LOAN_DISBURSEMENT_TRANSACTION_TYPE, 120000, CURRENCY_KES);

        TransactionType result = integrationTestHelper.fetchById(BASE_URL, created.getKey(), emptyMap(), TransactionType.class, OK);
        Assertions.assertNotNull(result);
    }

    @Test
    void whenFetchNonExistentById_thenReturnHttp404() throws Exception {
        Map.Entry<Long, TransactionRequest> created = transactionGenerator.createRequest(LOAN_DISBURSEMENT_TRANSACTION_TYPE, 120000, CURRENCY_KES);

        TransactionType result = integrationTestHelper.fetchById(BASE_URL, created.getKey() + 1, emptyMap(), TransactionType.class, NOT_FOUND);
        Assertions.assertNull(result);
    }

    @Test
    void whenFetch_thenReturnHttp200() throws Exception {
        transactionGenerator.createRequest(LOAN_DISBURSEMENT_TRANSACTION_TYPE, 120000, CURRENCY_KES);
        transactionGenerator.createRequest(LOAN_REPAYMENT_TRANSACTION_TYPE, 125000, CURRENCY_KES);
        transactionGenerator.createRequest(LOAN_DISBURSEMENT_TRANSACTION_TYPE, 120000, CURRENCY_USD);
        transactionGenerator.createRequest(LOAN_REPAYMENT_TRANSACTION_TYPE, 125000, CURRENCY_USD);
        transactionGenerator.createRequest(LOAN_DISBURSEMENT_TRANSACTION_TYPE, 120000, CURRENCY_UGX);
        transactionGenerator.createRequest(LOAN_REPAYMENT_TRANSACTION_TYPE, 125000, CURRENCY_UGX);

        Collection<TransactionType> result = integrationTestHelper.fetch(BASE_URL, emptyMap(), TransactionType.class, OK);
        Assertions.assertNotNull(result);
    }

    @Test
    void whenDeleteExisting_thenReturnHttp200() throws Exception {
        Map.Entry<Long, TransactionRequest> created = transactionGenerator.createRequest(LOAN_DISBURSEMENT_TRANSACTION_TYPE, 120000, CURRENCY_KES);

        Void result = integrationTestHelper.delete(BASE_URL, created.getKey(), Void.class, OK);
        Assertions.assertNull(result);
    }

    @Test
    void whenDeleteNonExistent_thenReturnHttp404() throws Exception {
        Map.Entry<Long, TransactionRequest> created = transactionGenerator.createRequest(LOAN_DISBURSEMENT_TRANSACTION_TYPE, 120000, CURRENCY_KES);

        Void result = integrationTestHelper.delete(BASE_URL, created.getKey() + 1, Void.class, NOT_FOUND);
        Assertions.assertNull(result);
    }

    public static class TransactionGenerator {

        public static final String LOAN_DISBURSEMENT_TRANSACTION_TYPE = "loanDisbursementTransactionType";
        public static final String LOAN_REPAYMENT_TRANSACTION_TYPE = "loanRepaymentTransactionType";
        public static final String CURRENCY_KES = "KES";
        public static final String CURRENCY_USD = "USD";
        public static final String CURRENCY_UGX = "UGX";

        private final IntegrationTestHelper integrationTestHelper;
        private final TransactionTypeRepository transactionTypeRepository;
        private final AccountRepository accountRepository;
        private final Map<String, AccountTypeEntity> accountTypes;
        private final Map<String, AccountStatusEntity> accountStatuses;
        private final Map<String, CurrencyEntity> currencies;
        private final Map<String, TransactionTypeEntity> transactionTypes;
        private final AccountEntity assetAccount;
        private final AccountEntity liabilityAccount;
        private final AccountEntity equityAccount;
        private final AccountEntity incomeAccount;
        private final AccountEntity expenseAccount;
        private final UserEntity user;

        public TransactionGenerator(IntegrationTestHelper integrationTestHelper, TransactionTypeRepository transactionTypeRepository, UserRepository userRepository, AccountRepository accountRepository, CurrencyRepository currencyRepository, AccountTypeRepository accountTypeRepository, AccountStatusRepository accountStatusRepository) {
            this.integrationTestHelper = integrationTestHelper;
            this.transactionTypeRepository = transactionTypeRepository;
            this.accountRepository = accountRepository;

            accountTypes = CollectionUtilities.listToMap(accountTypeRepository.findAll(), AccountTypeEntity::getName);
            accountStatuses = CollectionUtilities.listToMap(accountStatusRepository.findAll(), AccountStatusEntity::getName);
            currencies = CollectionUtilities.listToMap(currencyRepository.findAll(), CurrencyEntity::getCode);
            assetAccount = createAccount(accountTypes.get("ASSETS"), accountStatuses.get("ACTIVE"), currencies.values());
            liabilityAccount = createAccount(accountTypes.get("LIABILITIES"), accountStatuses.get("ACTIVE"), currencies.values());
            equityAccount = createAccount(accountTypes.get("EQUITY"), accountStatuses.get("ACTIVE"), currencies.values());
            incomeAccount = createAccount(accountTypes.get("INCOME"), accountStatuses.get("ACTIVE"), currencies.values());
            expenseAccount = createAccount(accountTypes.get("EXPENSES"), accountStatuses.get("ACTIVE"), currencies.values());
            createTransactionTypes();
            transactionTypes = CollectionUtilities.listToMap(transactionTypeRepository.findAll(), TransactionTypeEntity::getName);
            user = userRepository.findAll().getFirst();
        }

        private void createTransactionTypes() {
            createTransactionType("loanDisbursementTransactionType", Set.of(liabilityAccount), Set.of(assetAccount));
            createTransactionType("loanRepaymentTransactionType", Set.of(assetAccount), Set.of(incomeAccount));
        }

        private AccountEntity createAccount(AccountTypeEntity accountType, AccountStatusEntity accountStatus, Collection<CurrencyEntity> currencies) {
            String name = accountType.getName().toLowerCase() + "-" + accountStatus.getName().toLowerCase() + "-" + RANDOM.nextInt(1000, 10000);
            AccountEntity account = new AccountEntity();
            account.setName(name);
            account.setDescription("Description for " + name + ".");
            account.setAccountType(accountType);
            account.setOverdrawable(false);
            account.setAccountStatus(accountStatus);

            for (CurrencyEntity currency : currencies) {
                AccountBalanceEntity accountBalance = new AccountBalanceEntity();
                accountBalance.setCurrency(currency);
                accountBalance.setBalanceMinor(0);
                accountBalance.setUpperThresholdMinor(10000000000L);
                accountBalance.setLowerThresholdMinor(10L);
                account.addBalance(accountBalance);
            }
            return accountRepository.save(account);
        }

        public void createTransactionType(String name, Set<AccountEntity> debitAccounts, Set<AccountEntity> creditAccounts) {
            TransactionTypeEntity transactionType = new TransactionTypeEntity();
            transactionType.setName(name);
            transactionType.setDescription("Description for " + name + ".");
            transactionType.setReversible(false);
            transactionType.setDebitAccounts(debitAccounts);
            transactionType.setCreditAccounts(creditAccounts);
            transactionTypeRepository.save(transactionType);
        }

        public Map.Entry<Long, TransactionRequest> createRequest(String transactionType, long amountMinor, String currencyCode) throws Exception {
            TransactionTypeEntity transactionTypeEntity = transactionTypes.get(transactionType);
            Set<AccountEntity> debitAccounts = transactionTypeEntity.getDebitAccounts();
            Set<AccountEntity> creditAccounts = transactionTypeEntity.getCreditAccounts();
            CurrencyEntity currency = currencies.get(currencyCode);

            List<TransactionEntryRequest> entries = new ArrayList<>();
            long perDebitAccount = amountMinor / debitAccounts.size();
            for (AccountEntity account : debitAccounts) {
                TransactionEntryRequest entry = new TransactionEntryRequest(account.getId(), perDebitAccount, 0, currency.getId());
                entries.add(entry);
            }

            long perCreditAccount = amountMinor / creditAccounts.size();
            for (AccountEntity account : creditAccounts) {
                TransactionEntryRequest entry = new TransactionEntryRequest(account.getId(), 0, perCreditAccount, currency.getId());
                entries.add(entry);
            }

            String idempotencyKey = UUID.randomUUID().toString();
            TransactionRequest request = new TransactionRequest(idempotencyKey, transactionTypeEntity.getId(), user.getId(), entries);
            Long result = integrationTestHelper.create(BASE_URL, request, Long.class, CREATED);
            Assertions.assertNotNull(result);
            return Map.entry(result, request);
        }
    }
}
