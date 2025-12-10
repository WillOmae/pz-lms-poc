package com.wilburomae.pezeshalms.reports.data.repositories;

import com.wilburomae.pezeshalms.reports.dtos.CurrencyBalance;
import jakarta.annotation.Nonnull;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.temporal.TemporalAccessor;
import java.util.List;

@Repository
public class AccountBalancesRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public AccountBalancesRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<CurrencyBalance> query(long accountId, @Nonnull TemporalAccessor before) {
        String sql = """
                SELECT b.currency_id AS currency_id,
                       c.code AS currency_code,
                       te.account_balance_minor AS balance_minor
                FROM lms.account_balances b
                    LEFT JOIN lms.currencies c ON c.id = b.currency_id
                    LEFT JOIN LATERAL (
                    SELECT t.account_balance_minor,
                           t.transaction_reference,
                           t.date_created
                    FROM lms.transaction_entries t
                    WHERE t.account_id = b.account_id
                        AND t.currency = b.currency_id
                        AND t.date_created <= :before
                    ORDER BY t.date_created DESC
                    LIMIT 1
                    ) te ON TRUE
                WHERE b.account_id = :accountId
                ORDER BY b.account_id, b.currency_id;
                """;
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("accountId", accountId)
                .addValue("before", before);

        return jdbcTemplate.query(sql, params, new RowMapper<CurrencyBalance>() {
            @Override
            public CurrencyBalance mapRow(ResultSet rs, int rowNum) throws SQLException {
                long resCurrencyId = rs.getLong("currency_id");
                String resCurrencyCode = rs.getString("currency_code");
                long resBalanceMinor = rs.getLong("balance_minor");

                return new CurrencyBalance(resCurrencyId, resCurrencyCode, resBalanceMinor);
            }
        });
    }
}