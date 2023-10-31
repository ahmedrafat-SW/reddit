package com.dev.redditclone.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "token")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SequenceGenerator(name = "token_seq", sequenceName = "token_seq", allocationSize = 1)
public class VerificationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "token_seq")
    private Long id;
    private String token;
    private Instant expireDate;
    @OneToOne(fetch = FetchType.LAZY)
    private User user;

}
