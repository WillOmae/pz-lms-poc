package com.wilburomae.pezeshalms.users.data.repositories;

import com.wilburomae.pezeshalms.users.data.entities.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
}
