package com.wilburomae.pezeshalms.users.data.repositories;

import com.wilburomae.pezeshalms.users.data.entities.CredentialEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CredentialRepository extends JpaRepository<CredentialEntity, Long> {
}
