package com.wilburomae.pezeshalms.transactions.services;

import com.wilburomae.pezeshalms.common.dtos.Response;
import com.wilburomae.pezeshalms.common.services.UpsertService;
import com.wilburomae.pezeshalms.common.utilities.CollectionUtilities;
import com.wilburomae.pezeshalms.transactions.data.entities.ReasonTypeEntity;
import com.wilburomae.pezeshalms.transactions.data.entities.TransactionTypeEntity;
import com.wilburomae.pezeshalms.transactions.data.repositories.ReasonTypeRepository;
import com.wilburomae.pezeshalms.transactions.data.repositories.TransactionTypeRepository;
import com.wilburomae.pezeshalms.transactions.dtos.ReasonTypeRequest;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Map;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
public class ReasonTypesUpsertService implements UpsertService<ReasonTypeRequest> {

    private final ReasonTypeRepository reasonTypeRepository;
    private final TransactionTypeRepository transactionTypeRepository;

    public ReasonTypesUpsertService(ReasonTypeRepository reasonTypeRepository, TransactionTypeRepository transactionTypeRepository) {
        this.reasonTypeRepository = reasonTypeRepository;
        this.transactionTypeRepository = transactionTypeRepository;
    }

    @Transactional
    @Override
    public Response<Long> upsert(Long id, ReasonTypeRequest request) {
        Map<Long, TransactionTypeEntity> transactionTypes = CollectionUtilities.listToMap(transactionTypeRepository.findAll(), TransactionTypeEntity::getId);

        Response<ReasonTypeEntity> initResponse = initEntity(id, reasonTypeRepository, ReasonTypeEntity::new);
        if (initResponse.responseCode().isError()) {
            return new Response<>(NOT_FOUND, "Reason type not found", null);
        }

        ReasonTypeEntity entity = initResponse.data();
        entity.setName(request.name());
        entity.setDescription(request.description());

        for (long transactionTypesId : request.transactionTypes()) {
            TransactionTypeEntity transactionType = transactionTypes.get(transactionTypesId);
            if (transactionType == null) {
                return new Response<>(NOT_FOUND, "Transaction type not found", null);
            }
            entity.addTransactionType(transactionType);
        }

        entity = reasonTypeRepository.save(entity);
        return successResponse(id, "Reason type", entity);
    }
}
