package com.wilburomae.pezeshalms.accounts.data.repositories;

import com.wilburomae.pezeshalms.accounts.data.entities.AccountTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountTypeRepository extends JpaRepository<AccountTypeEntity, Long> {

    @Query(value = """
            WITH RECURSIVE ancestors AS (
                SELECT id, parent_account_type_id
                FROM lms.account_types
                WHERE id = :id
                UNION ALL
                SELECT at.id, at.parent_account_type_id
                FROM lms.account_types at
                         JOIN ancestors a ON a.parent_account_type_id = at.id
            )
            SELECT * FROM lms.account_types
            WHERE id = (
                SELECT id FROM ancestors WHERE ancestors.parent_account_type_id IS NULL LIMIT 1
            );
            """, nativeQuery = true)
    Optional<AccountTypeEntity> fetchRoot(long id);
}
