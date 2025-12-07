package com.wilburomae.pezeshalms.users.data.repositories;

import com.wilburomae.pezeshalms.users.data.entities.LoginAttemptEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginAttemptRepository extends JpaRepository<LoginAttemptEntity, Long> {
}
