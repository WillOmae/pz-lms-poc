package com.wilburomae.pezeshalms.security;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CredentialStatusRepository extends JpaRepository<CredentialStatusEntity, Long> {
}
