package com.wilburomae.pezeshalms.accounts.data.repositories;

import com.wilburomae.pezeshalms.accounts.data.entities.TransactionEntryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionEntryRepository extends JpaRepository<TransactionEntryEntity, Long> {
}
