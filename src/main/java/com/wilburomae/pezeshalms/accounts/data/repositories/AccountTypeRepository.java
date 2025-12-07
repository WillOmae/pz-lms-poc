package com.wilburomae.pezeshalms.accounts.data.repositories;

import com.wilburomae.pezeshalms.accounts.data.entities.AccountTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountTypeRepository extends JpaRepository<AccountTypeEntity, Long> {
}
