package com.wilburomae.pezeshalms.transactions.data.repositories;

import com.wilburomae.pezeshalms.transactions.data.entities.TransactionTypeComponentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionTypeComponentRepository extends JpaRepository<TransactionTypeComponentEntity, Long> {
}
