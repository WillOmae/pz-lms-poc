package com.wilburomae.pezeshalms.accounts.data.repositories;

import com.wilburomae.pezeshalms.accounts.data.entities.PartnerAccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PartnerAccountRepository extends JpaRepository<PartnerAccountEntity, Long> {
}
