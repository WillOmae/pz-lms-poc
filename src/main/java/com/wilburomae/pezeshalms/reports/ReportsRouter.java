package com.wilburomae.pezeshalms.reports;

import com.wilburomae.pezeshalms.common.dtos.Response;
import com.wilburomae.pezeshalms.common.routers.PermissionChecker;
import com.wilburomae.pezeshalms.reports.ReportsRouter.ReportsHandler.Params;
import com.wilburomae.pezeshalms.reports.services.AccountBalancesService;
import com.wilburomae.pezeshalms.reports.services.TransactionHistoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.function.*;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static org.springframework.http.HttpStatus.CONFLICT;

@Component
public class ReportsRouter {

    @Bean
    public RouterFunction<ServerResponse> queryAccountBalances(AccountBalancesService accountBalancesService) {
        Function<Params, Response<?>> operation = accountBalancesService::query;
        return RouterFunctions.route().GET("/reports/balances/{accountId}", new ReportsHandler("REPORTS", operation)).build();
    }

    @Bean
    public RouterFunction<ServerResponse> queryTransactionHistory(TransactionHistoryService transactionHistoryService) {
        Function<Params, Response<?>> operation = transactionHistoryService::query;
        return RouterFunctions.route().GET("/reports/transactions/{accountId}", new ReportsHandler("REPORTS", operation)).build();
    }

    public static class ReportsHandler implements HandlerFunction<ServerResponse> {

        private final Logger logger = LoggerFactory.getLogger(ReportsHandler.class);
        private final String permission;
        private final Function<Params, Response<?>> operation;

        public ReportsHandler(String permission, Function<Params, Response<?>> operation) {
            this.permission = "READ_" + permission;
            this.operation = operation;
        }

        @Override
        public ServerResponse handle(ServerRequest req) {
            PermissionChecker.validatePermission(permission);

            Params params = new Params(req.pathVariables());
            params.putAll(req.params().asSingleValueMap());
            try {
                return operation.apply(params).toServerResponse();
            } catch (DataIntegrityViolationException e) {
                logger.error(e.getMessage(), e);
                return new Response<>(CONFLICT, "Duplicate entry", null).toServerResponse();
            }
        }

        public static class Params extends HashMap<String, String> {

            public Params() {
            }

            public Params(Map<String, String> map) {
                super(map);
            }
        }
    }
}
