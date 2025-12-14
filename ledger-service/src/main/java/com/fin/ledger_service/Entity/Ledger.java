package com.fin.ledger_service.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "ledger_transactions")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Ledger {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ledgerId;

    @Column(nullable = false)
    private Long giverId;

    @Column(nullable = false)
    private Long receiverId;

    @Column(nullable = false, precision = 15, scale = 4)
    private BigDecimal amount;

    @Builder.Default
    Date createdAt = new Date();

    @Builder.Default
    Date  updatedAt = new Date();

    @Builder.Default
    Boolean status = Boolean.FALSE ;
}
