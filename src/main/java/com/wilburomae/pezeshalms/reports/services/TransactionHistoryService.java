package com.wilburomae.pezeshalms.reports.services;

import com.wilburomae.pezeshalms.common.dtos.Response;
import com.wilburomae.pezeshalms.reports.ReportsRouter.ReportsHandler.Params;
import com.wilburomae.pezeshalms.reports.data.repositories.AccountBalancesRepository;
import com.wilburomae.pezeshalms.reports.dtos.AccountBalances;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

@Service
public class TransactionHistoryService {

    private final AccountBalancesRepository accountBalancesRepository;

    public TransactionHistoryService(AccountBalancesRepository accountBalancesRepository) {
        this.accountBalancesRepository = accountBalancesRepository;
    }

    @Transactional
    public Response<AccountBalances> query(Params params) {
        String sAccountId = params.get("accountId");
        if (sAccountId == null) {
            return new Response<>(BAD_REQUEST, "No account id provided", null);
        }
        long accountId = Long.parseLong(sAccountId);

        String sBefore = params.get("before");
        TemporalAccessor before;
        if (sBefore == null) {
            before = OffsetDateTime.now(OffsetDateTime.now().getOffset());
        } else {
            before = DateTimeFormatter.ISO_OFFSET_DATE_TIME.parse(sBefore);
        }
//        List<AccountBalances> data = accountBalancesRepository.query(accountId, before);
        System.out.println();
        return new Response<>(OK, "Balances fetched successfully", null);
    }
}
