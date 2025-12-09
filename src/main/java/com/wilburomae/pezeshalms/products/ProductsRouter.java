package com.wilburomae.pezeshalms.products;

import com.wilburomae.pezeshalms.common.routers.CrudRouteBuilder;
import com.wilburomae.pezeshalms.products.data.repositories.LoanProductRepository;
import com.wilburomae.pezeshalms.products.dtos.LoanProduct;
import com.wilburomae.pezeshalms.products.dtos.LoanProductRequest;
import com.wilburomae.pezeshalms.products.services.LoanProductsUpsertService;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

@Component
public class ProductsRouter {

    @Bean
    public RouterFunction<ServerResponse> loanProductsRoutes(CrudRouteBuilder builder, LoanProductRepository repository, LoanProductsUpsertService upsertService) {
        return builder.build("LOAN_PRODUCTS", "Loan product", "/products", LoanProductRequest.class, LoanProduct::from, repository, upsertService);
    }
}
