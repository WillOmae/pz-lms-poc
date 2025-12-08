package com.wilburomae.pezeshalms.transactions.data.repositories;

import com.wilburomae.pezeshalms.transactions.data.entities.TransactionEntryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionEntryRepository extends JpaRepository<TransactionEntryEntity, Long> {
}
