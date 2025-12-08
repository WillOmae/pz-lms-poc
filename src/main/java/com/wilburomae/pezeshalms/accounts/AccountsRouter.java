package com.wilburomae.pezeshalms.accounts;

import com.wilburomae.pezeshalms.accounts.data.repositories.*;
import com.wilburomae.pezeshalms.accounts.dtos.*;
import com.wilburomae.pezeshalms.accounts.services.*;
import com.wilburomae.pezeshalms.common.routers.CrudRouteBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

@Component
public class AccountsRouter {

    @Bean
    public RouterFunction<ServerResponse> accountsRoutes(CrudRouteBuilder builder, AccountRepository repository, AccountsUpsertService upsertService) {
        return builder.build("ACCOUNTS", "Account", "/accounts/accounts", AccountRequest.class, Account::from, repository, upsertService);
    }

    @Bean
    public RouterFunction<ServerResponse> accountStatusesRoutes(CrudRouteBuilder builder, AccountStatusRepository repository, AccountStatusesUpsertService upsertService) {
        return builder.build("ACCOUNT_STATUSES", "Account status", "/accounts/statuses", AccountStatusRequest.class, AccountStatus::from, repository, upsertService);
    }

    @Bean
    public RouterFunction<ServerResponse> accountTypesRoutes(CrudRouteBuilder builder, AccountTypeRepository repository, AccountTypesUpsertService upsertService) {
        return builder.build("ACCOUNT_TYPES", "Account type", "/accounts/types", AccountTypeRequest.class, AccountType::from, repository, upsertService);
    }

    @Bean
    public RouterFunction<ServerResponse> currenciesRoutes(CrudRouteBuilder builder, CurrencyRepository repository, CurrenciesUpsertService upsertService) {
        return builder.build("CURRENCIES", "Currency", "/accounts/currencies", CurrencyRequest.class, Currency::from, repository, upsertService);
    }

    @Bean
    public RouterFunction<ServerResponse> partnerAccountsRoutes(CrudRouteBuilder builder, PartnerAccountRepository repository, PartnerAccountsUpsertService upsertService) {
        return builder.build("PARTNER_ACCOUNTS", "Partner account", "/accounts/partners", PartnerAccountRequest.class, PartnerAccount::from, repository, upsertService);
    }
}
