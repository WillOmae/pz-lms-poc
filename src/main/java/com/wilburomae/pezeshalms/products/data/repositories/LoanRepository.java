package com.wilburomae.pezeshalms.products.data.repositories;

import com.wilburomae.pezeshalms.products.data.entities.LoanEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoanRepository extends JpaRepository<LoanEntity, Long> {
}
