package com.wilburomae.pezeshalms.transactions.data.repositories;

import com.wilburomae.pezeshalms.transactions.data.entities.TransactionTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionTypeRepository extends JpaRepository<TransactionTypeEntity, Long> {
}
