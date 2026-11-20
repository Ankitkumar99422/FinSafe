package com.finsafe.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name = "accounts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "account_name")
    private String accountName;

    @Column(name = "current_balance", nullable = false)
    private BigDecimal currentBalance;

    @Column(nullable = false)
    private String currency;

    @Column(name = "last_synced_at", insertable = false, updatable = false)
    private OffsetDateTime lastSyncedAt;
}
