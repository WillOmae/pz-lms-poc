package com.wilburomae.pezeshalms.products.data.repositories;

import com.wilburomae.pezeshalms.products.data.entities.LoanProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoanProductRepository extends JpaRepository<LoanProductEntity, Long> {
}
