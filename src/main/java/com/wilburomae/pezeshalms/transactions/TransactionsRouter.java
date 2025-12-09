package com.wilburomae.pezeshalms.transactions;

import com.wilburomae.pezeshalms.common.routers.CrudRouteBuilder;
import com.wilburomae.pezeshalms.transactions.data.repositories.ReasonTypeRepository;
import com.wilburomae.pezeshalms.transactions.data.repositories.TransactionRepository;
import com.wilburomae.pezeshalms.transactions.data.repositories.TransactionTypeRepository;
import com.wilburomae.pezeshalms.transactions.dtos.*;
import com.wilburomae.pezeshalms.transactions.services.ReasonTypesUpsertService;
import com.wilburomae.pezeshalms.transactions.services.TransactionTypesUpsertService;
import com.wilburomae.pezeshalms.transactions.services.TransactionsUpsertService;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

@Component
public class TransactionsRouter {

    @Bean
    public RouterFunction<ServerResponse> reasonTypesRoutes(CrudRouteBuilder builder, ReasonTypeRepository repository, ReasonTypesUpsertService upsertService) {
        return builder.build("REASON_TYPES", "Reason type", "/transactions/reasons", ReasonTypeRequest.class, ReasonType::from, repository, upsertService);
    }

    @Bean
    public RouterFunction<ServerResponse> transactionTypesRoutes(CrudRouteBuilder builder, TransactionTypeRepository repository, TransactionTypesUpsertService upsertService) {
        return builder.build("TRANSACTION_TYPES", "Transaction type", "/transactions/types", TransactionTypeRequest.class, TransactionType::from, repository, upsertService);
    }

    @Bean
    public RouterFunction<ServerResponse> transactionsRoutes(CrudRouteBuilder builder, TransactionRepository repository, TransactionsUpsertService upsertService) {
        return builder.build("TRANSACTIONS", "Transaction", "/transactions/transactions", TransactionRequest.class, Transaction::from, repository, upsertService);
    }
}
