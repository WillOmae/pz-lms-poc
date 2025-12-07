package com.wilburomae.pezeshalms.common.routers;

import com.wilburomae.pezeshalms.common.services.DeletionService;
import com.wilburomae.pezeshalms.common.services.FetchService;
import com.wilburomae.pezeshalms.common.services.UpsertService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.RouterFunctions;
import org.springframework.web.servlet.function.ServerResponse;

import java.util.function.Function;

@Component
public class CrudRouteBuilder {

    private final FetchService fetchService;
    private final DeletionService deletionService;

    public CrudRouteBuilder(FetchService fetchService, DeletionService deletionService) {
        this.fetchService = fetchService;
        this.deletionService = deletionService;
    }

    public <T, R, U> RouterFunction<ServerResponse> build(String permissionSuffix,
                                                          String nameSingular,
                                                          String commonUrl,
                                                          Class<T> requestClass,
                                                          Function<R, U> mapper,
                                                          JpaRepository<R, Long> repository,
                                                          UpsertService<T> upsertService) {

        return RouterFunctions.route()
                .POST(commonUrl, new GenericUpsertRouter<>("WRITE_" + permissionSuffix, upsertService, requestClass))
                .GET(commonUrl, new GenericFetchRouter<>("READ_" + permissionSuffix, nameSingular, mapper, repository, fetchService))
                .GET(commonUrl + "/{id}", new GenericFetchByIdRouter<>("READ_" + permissionSuffix, nameSingular, mapper, repository, fetchService))
                .PUT(commonUrl + "/{id}", new GenericUpsertRouter<>("WRITE_" + permissionSuffix, upsertService, requestClass))
                .DELETE(commonUrl + "/{id}", new GenericDeleteRouter<>("DELETE_" + permissionSuffix, nameSingular, repository, deletionService))
                .build();
    }
}
