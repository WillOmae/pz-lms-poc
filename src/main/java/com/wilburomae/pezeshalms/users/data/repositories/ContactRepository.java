package com.wilburomae.pezeshalms.users.data.repositories;

import com.wilburomae.pezeshalms.users.data.entities.ContactEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactRepository extends JpaRepository<ContactEntity, Long> {
}
