package com.wilburomae.pezeshalms.reports.dtos;

import java.util.List;

public record AccountBalances(long accountId, String accountName, List<CurrencyBalance> balances) {
}