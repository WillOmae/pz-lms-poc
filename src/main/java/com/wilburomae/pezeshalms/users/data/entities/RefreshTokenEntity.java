package com.wilburomae.pezeshalms.users.data.entities;

import com.wilburomae.pezeshalms.common.data.entities.IdAuditableEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@Table(name = "refresh_tokens", schema = "lms")
public class RefreshTokenEntity extends IdAuditableEntity {

    @Column(name = "token")
    private String token;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "entity_id")
    private UserEntity user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "login_attempts_id")
    private LoginAttemptEntity loginAttempt;

    @Column(name = "expires_at")
    private OffsetDateTime expiresAt;
}