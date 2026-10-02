package com.uc.ms_security.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "sessions")
@Getter
@Setter
@NoArgsConstructor
public class Session {

    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long id;

    @Column(
            nullable = false,
            unique = true,
            length = 512
    )
    private String token;

    @Column(
            nullable = false
    )
    private LocalDateTime expiration;

    @Column(
            name = "code_2fa",
            length = 10
    )
    private String code2FA;

    // Relación N a 1 con User (Muchos registros de Session pertenecen a un único User)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "user_id",
            nullable = false
    )
    private User user;
}
