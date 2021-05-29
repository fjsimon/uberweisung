package com.fjsimon.uberweisung.domain.repository;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "wallet")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
public class Wallet {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "user_id must be provided")
    @Column(name = "user_id")
    private Long userId;

    @Min( value = 0L)
    @Column(name = "balance", nullable = false)
    @NotNull(message = "balance must be provided")
    private BigDecimal balance;

    @NotNull(message = "currency must be provided")
    @Column(name = "currency", nullable = false)
    @Enumerated(EnumType.STRING)
    private CURRENCY currency;

    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;

    @OneToMany(mappedBy = "wallet", fetch = FetchType.LAZY)
    private List<Transaction> transactions;

}