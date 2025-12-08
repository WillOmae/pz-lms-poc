package com.wilburomae.pezeshalms.transactions;

import com.wilburomae.pezeshalms.common.routers.CrudRouteBuilder;
import com.wilburomae.pezeshalms.transactions.data.repositories.TransactionTypeRepository;
import com.wilburomae.pezeshalms.transactions.dtos.TransactionType;
import com.wilburomae.pezeshalms.transactions.dtos.TransactionTypeRequest;
import com.wilburomae.pezeshalms.transactions.services.TransactionTypesUpsertService;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

@Component
public class TransactionsRouter {

    @Bean
    public RouterFunction<ServerResponse> transactionTypesRoutes(CrudRouteBuilder builder, TransactionTypeRepository repository, TransactionTypesUpsertService upsertService) {
        return builder.build("TRANSACTION_TYPES", "Transaction type", "/transactions/types", TransactionTypeRequest.class, TransactionType::from, repository, upsertService);
    }
}
