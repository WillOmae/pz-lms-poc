package com.wilburomae.pezeshalms.auditlogs;

import jakarta.validation.constraints.NotNull;
import org.springframework.jdbc.datasource.DelegatingDataSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AppUserDatasource extends DelegatingDataSource {

    public AppUserDatasource(DataSource dataSource) {
        super(dataSource);
    }

    @NotNull
    @Override
    public Connection getConnection() throws SQLException {
        Connection connection = obtainTargetDataSource().getConnection();
        setAppUser(connection);
        return connection;
    }

    @NotNull
    @Override
    public Connection getConnection(@NotNull String username, @NotNull String password) throws SQLException {
        Connection connection = super.getConnection(username, password);
        setAppUser(connection);
        return connection;
    }

    private void setAppUser(Connection conn) throws SQLException {
        String currentUser = getCurrentAppUser();
        try (PreparedStatement ps = conn.prepareStatement("SET audit.user_id = '" + currentUser + "';")) {
            ps.execute();
        }
    }

    private String getCurrentAppUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (auth != null && auth.isAuthenticated()) ? auth.getName() : "system";
    }
}