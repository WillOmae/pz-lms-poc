package com.wilburomae.pezeshalms.products.services;

import com.wilburomae.pezeshalms.common.dtos.Response;
import com.wilburomae.pezeshalms.common.services.UpsertService;
import com.wilburomae.pezeshalms.common.utilities.CollectionUtilities;
import com.wilburomae.pezeshalms.products.data.entities.LoanProductEntity;
import com.wilburomae.pezeshalms.products.data.entities.TermUnit;
import com.wilburomae.pezeshalms.products.data.repositories.LoanProductRepository;
import com.wilburomae.pezeshalms.products.dtos.LoanProductRequest;
import com.wilburomae.pezeshalms.transactions.data.entities.ReasonTypeEntity;
import com.wilburomae.pezeshalms.transactions.data.repositories.ReasonTypeRepository;
import com.wilburomae.pezeshalms.users.data.entities.EntityType;
import com.wilburomae.pezeshalms.users.data.entities.UserEntity;
import com.wilburomae.pezeshalms.users.data.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Map;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
public class LoanProductsUpsertService implements UpsertService<LoanProductRequest> {

    private final LoanProductRepository loanProductRepository;
    private final ReasonTypeRepository reasonTypeRepository;
    private final UserRepository userRepository;

    public LoanProductsUpsertService(LoanProductRepository loanProductRepository, ReasonTypeRepository reasonTypeRepository, UserRepository userRepository) {
        this.loanProductRepository = loanProductRepository;
        this.reasonTypeRepository = reasonTypeRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    @Override
    public Response<Long> upsert(Long id, LoanProductRequest request) {
        Map<Long, ReasonTypeEntity> reasonTypes = CollectionUtilities.listToMap(reasonTypeRepository.findAll(), ReasonTypeEntity::getId);
        Map<Long, UserEntity> partners = CollectionUtilities.listToMap(userRepository.findAllByType(EntityType.PARTNER), UserEntity::getId);

        Response<LoanProductEntity> initResponse = initEntity(id, loanProductRepository, LoanProductEntity::new);
        if (initResponse.responseCode().isError()) {
            return new Response<>(NOT_FOUND, "Loan product not found", null);
        }

        LoanProductEntity entity = initResponse.data();
        entity.setName(request.name());
        entity.setDescription(request.description());
        entity.setInterestChargeable(request.interestChargeable());
        entity.setInterestRate(request.interestRate());
        entity.setAccessFeeChargeable(request.accessFeeChargeable());
        entity.setAccessFeePercentage(request.accessFeePercentage());
        entity.setTerm(request.term());
        entity.setInterestTermUnit(TermUnit.valueOf(request.interestTermUnit()));
        entity.setTermUnit(TermUnit.valueOf(request.termUnit()));
        entity.setMaxDisbursementsPerCustomer(request.maxDisbursementsPerCustomer());
        entity.setActive(request.active());

        for (long reasonTypeId : request.reasonTypes()) {
            ReasonTypeEntity reasonType = reasonTypes.get(reasonTypeId);
            if (reasonType == null) {
                return new Response<>(NOT_FOUND, "Reason type not found", null);
            }
            entity.addReasonType(reasonType);
        }

        for (long partnerId : request.partners()) {
            UserEntity partner = partners.get(partnerId);
            if (partner == null) {
                return new Response<>(NOT_FOUND, "Partner not found", null);
            }
            entity.addPartner(partner);
        }

        entity = loanProductRepository.save(entity);
        return successResponse(id, "Loan product", entity);
    }
}
