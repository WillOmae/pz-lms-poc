package com.wilburomae.pezeshalms.helpers;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DbHelper {

    @PersistenceContext
    private EntityManager em;

    @PostConstruct
    @Transactional
    public void resetSequences() {
        String sql = """
                SELECT
                    'SELECT setval(''lms.' || seq.relname || ''', (SELECT COALESCE(MAX(' || col.attname ||
                    '), 1) FROM lms.' || tbl.relname || '));' AS cmd
                FROM pg_class seq
                         JOIN pg_depend d ON d.objid = seq.oid AND d.classid = 'pg_class'::REGCLASS
                         JOIN pg_class tbl ON tbl.oid = d.refobjid
                         JOIN pg_attribute col ON col.attrelid = tbl.oid AND col.attnum = d.refobjsubid
                WHERE seq.relkind = 'S'
                  AND tbl.relnamespace = 'lms'::REGNAMESPACE;
                """;
        List<String> queries = em.createNativeQuery(sql).getResultList();
        for (String query : queries) {
            em.createNativeQuery(query).getResultList();
        }
    }
}
