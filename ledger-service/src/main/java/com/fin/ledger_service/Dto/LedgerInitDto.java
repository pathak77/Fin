package com.fin.ledger_service.Dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Component
public class LedgerInitDto {

    Long ledgerId;

    @NotNull
    Long receiverId;

    @NotNull
    Long giverId;

    BigDecimal amount;
    LocalDate createdAt;
    LocalDate updatedAt;
    Boolean status;
}
