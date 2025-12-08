package com.wilburomae.pezeshalms.transactions.data.repositories;

import com.wilburomae.pezeshalms.transactions.data.entities.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {
}
