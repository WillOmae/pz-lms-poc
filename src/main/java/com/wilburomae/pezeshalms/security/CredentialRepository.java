package com.wilburomae.pezeshalms.security;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CredentialRepository extends JpaRepository<CredentialEntity, Long> {

    @Query("""
            SELECT c FROM CredentialEntity c
            JOIN FETCH c.user u
            JOIN FETCH u.contacts ct
            JOIN FETCH u.roles r
            JOIN FETCH r.permissions p
            JOIN FETCH c.status s
            WHERE ct.contact = :contact
            """)
    Optional<CredentialEntity> fetchByContact(String contact);
}
