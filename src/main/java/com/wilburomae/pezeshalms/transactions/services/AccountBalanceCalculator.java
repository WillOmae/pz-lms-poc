package com.wilburomae.pezeshalms.transactions.services;

import com.wilburomae.pezeshalms.accounts.data.entities.AccountTypeEntity;
import com.wilburomae.pezeshalms.common.dtos.Response;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.OK;

public class AccountBalanceCalculator {

    public Response<Long> calculateDelta(AccountTypeEntity rootAccountType, long debitAmountMinor, long creditAmountMinor) {
        long deltaMinor;
        switch (rootAccountType.getName().toUpperCase()) {
            case "ASSETS":
            case "EXPENSES":
                deltaMinor = debitAmountMinor - creditAmountMinor;
                break;
            case "LIABILITIES":
            case "EQUITY":
            case "INCOME":
                deltaMinor = creditAmountMinor - debitAmountMinor;
                break;
            default:
                return new Response<>(INTERNAL_SERVER_ERROR, "Invalid account type", null);
        }
        return new Response<>(OK, "Delta calculated", deltaMinor);
    }
}
