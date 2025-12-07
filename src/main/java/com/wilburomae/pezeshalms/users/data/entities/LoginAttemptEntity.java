package com.wilburomae.pezeshalms.users.data.entities;

import com.wilburomae.pezeshalms.common.data.entities.IdAuditableEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "login_attempts", schema = "lms")
public class LoginAttemptEntity extends IdAuditableEntity {

    @Column(name = "username")
    private String username;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "entity_id")
    private UserEntity user;

    @Column(name = "ip_address")
    private String ipAddress;

    @Column(name = "source_system")
    private String sourceSystem;

    @OneToOne(mappedBy = "loginAttempt")
    private RefreshTokenEntity refreshToken;
}