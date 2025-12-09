package com.wilburomae.pezeshalms.users.data.repositories;

import com.wilburomae.pezeshalms.users.data.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    @Query("""
            SELECT DISTINCT u FROM UserEntity u
            JOIN FETCH u.contacts c
            WHERE c.contact = :contact
            """)
    Optional<UserEntity> fetchByContact(String contact);
}
