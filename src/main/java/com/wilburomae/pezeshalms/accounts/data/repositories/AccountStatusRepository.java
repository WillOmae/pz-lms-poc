package com.wilburomae.pezeshalms.accounts.data.repositories;

import com.wilburomae.pezeshalms.accounts.data.entities.AccountStatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountStatusRepository extends JpaRepository<AccountStatusEntity, Long> {
}
