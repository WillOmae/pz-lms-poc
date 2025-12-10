package com.wilburomae.pezeshalms.products;

import com.wilburomae.pezeshalms.common.routers.CrudRouteBuilder;
import com.wilburomae.pezeshalms.products.data.repositories.LoanProductRepository;
import com.wilburomae.pezeshalms.products.data.repositories.LoanRepository;
import com.wilburomae.pezeshalms.products.dtos.*;
import com.wilburomae.pezeshalms.products.services.DisbursementsService;
import com.wilburomae.pezeshalms.products.services.LoanProductsUpsertService;
import com.wilburomae.pezeshalms.products.services.RepaymentsService;
import com.wilburomae.pezeshalms.products.services.WriteOffService;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

@Component
public class ProductsRouter {

    @Bean
    public RouterFunction<ServerResponse> loanProducts(CrudRouteBuilder builder, LoanProductRepository repository, LoanProductsUpsertService upsertService) {
        return builder.build("LOAN_PRODUCTS", "Loan product", "/products", LoanProductRequest.class, LoanProduct::from, repository, upsertService);
    }

    @Bean
    public RouterFunction<ServerResponse> disbursements(CrudRouteBuilder builder, LoanRepository repository, DisbursementsService upsertService) {
        return builder.build("LOAN_PRODUCTS", "Disbursement", "/products/disbursements", DisbursementRequest.class, Disbursement::from, repository, upsertService);
    }

    @Bean
    public RouterFunction<ServerResponse> repayments(CrudRouteBuilder builder, LoanRepository repository, RepaymentsService upsertService) {
        return builder.build("LOAN_PRODUCTS", "Repayment", "/products/repayments", RepaymentRequest.class, Repayment::from, repository, upsertService);
    }

    @Bean
    public RouterFunction<ServerResponse> writeOffs(CrudRouteBuilder builder, LoanRepository repository, WriteOffService upsertService) {
        return builder.build("LOAN_PRODUCTS", "Repayment", "/products/writeoffs", WriteOffRequest.class, WriteOff::from, repository, upsertService);
    }
}
