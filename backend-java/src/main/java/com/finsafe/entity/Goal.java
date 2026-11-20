package com.finsafe.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;

@Entity
@Table(name = "goals")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Goal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "goal_name", nullable = false)
    private String goalName;

    @Column(name = "target_amount", nullable = false)
    private BigDecimal targetAmount;

    @Column(name = "saved_amount", nullable = false)
    private BigDecimal savedAmount;

    @Column(name = "target_date", nullable = false)
    private LocalDate targetDate;

    @Column(nullable = false)
    private String priority; // 'HIGH', 'MEDIUM', 'LOW'

    @Column(nullable = false)
    private String status; // 'IN_PROGRESS', 'REACHED', 'DELAYED'

    @Column(name = "created_at", insertable = false, updatable = false)
    private OffsetDateTime createdAt;
}
