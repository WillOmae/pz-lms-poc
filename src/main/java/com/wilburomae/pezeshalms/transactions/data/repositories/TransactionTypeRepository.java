package com.wilburomae.pezeshalms.transactions.data.repositories;

import com.wilburomae.pezeshalms.transactions.data.entities.TransactionTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TransactionTypeRepository extends JpaRepository<TransactionTypeEntity, Long> {

    Optional<TransactionTypeEntity> findByName(String name);
}
