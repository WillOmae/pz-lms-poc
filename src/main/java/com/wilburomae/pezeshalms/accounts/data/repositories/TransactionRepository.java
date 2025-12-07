package com.wilburomae.pezeshalms.accounts.data.repositories;

import com.wilburomae.pezeshalms.accounts.data.entities.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {
}
