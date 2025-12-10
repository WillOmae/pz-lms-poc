package com.wilburomae.pezeshalms.products.data.repositories;

import com.wilburomae.pezeshalms.products.data.entities.LoanRepaymentScheduleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoanRepaymentScheduleRepository extends JpaRepository<LoanRepaymentScheduleEntity, Long> {
}
