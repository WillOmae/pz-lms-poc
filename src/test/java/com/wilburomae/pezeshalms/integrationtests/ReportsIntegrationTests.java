package com.wilburomae.pezeshalms.integrationtests;

import com.wilburomae.pezeshalms.accounts.data.entities.AccountBalanceEntity;
import com.wilburomae.pezeshalms.accounts.data.entities.AccountEntity;
import com.wilburomae.pezeshalms.accounts.data.repositories.AccountRepository;
import com.wilburomae.pezeshalms.accounts.data.repositories.AccountStatusRepository;
import com.wilburomae.pezeshalms.accounts.data.repositories.AccountTypeRepository;
import com.wilburomae.pezeshalms.accounts.data.repositories.CurrencyRepository;
import com.wilburomae.pezeshalms.reports.dtos.AccountBalances;
import com.wilburomae.pezeshalms.transactions.data.repositories.TransactionTypeRepository;
import com.wilburomae.pezeshalms.users.data.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.security.SecureRandom;
import java.util.Collection;
import java.util.Map;
import java.util.Random;

import static com.wilburomae.pezeshalms.integrationtests.TransactionsIntegrationTests.TransactionGenerator.*;
import static org.springframework.http.HttpStatus.OK;

public class ReportsIntegrationTests extends BaseIntegrationTests {

    private static final Random RANDOM = new SecureRandom();
    private static final String BASE_URL = "/reports";

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
    private TransactionsIntegrationTests.TransactionGenerator transactionGenerator;

    @BeforeEach
    void setup() {
        if (transactionGenerator == null) {
            transactionGenerator = new TransactionsIntegrationTests.TransactionGenerator(integrationTestHelper, transactionTypeRepository, userRepository, accountRepository, currencyRepository, accountTypeRepository, accountStatusEntityRepository);
        }
    }

    @Test
    void whenQueryWithAccountIdAndTimestamp_thenReturnHttp200() throws Exception {
        transactionGenerator.createRequest(LOAN_DISBURSEMENT_TRANSACTION_TYPE, 120000, CURRENCY_KES);
        transactionGenerator.createRequest(LOAN_DISBURSEMENT_TRANSACTION_TYPE, 120000, CURRENCY_USD);
        transactionGenerator.createRequest(LOAN_DISBURSEMENT_TRANSACTION_TYPE, 120000, CURRENCY_UGX);

        for (AccountEntity account : accountRepository.findAll()) {
            for (AccountBalanceEntity accountBalance : account.getAccountBalances()) {
                System.out.println(accountBalance.getCurrency().getCode() + ": " + accountBalance.getBalanceMinor());
            }
        }
        System.out.println("&&&&&&&&&&&&&");

        for (AccountEntity account : accountRepository.findAll()) {
            Collection<AccountBalances> fetched = integrationTestHelper.fetch(BASE_URL + "/balances/" + account.getId(), Map.of(), AccountBalances.class, OK);
            System.out.println(fetched);
        }
    }
}
