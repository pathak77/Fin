package com.fin.ledger_service.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Component
public class LedgerInitDto {
    Long ledgerId;
    Long receiverId;
    Long giverId;
    BigDecimal amount;
    Date createdAt;
    Date updatedAt;
    Boolean status;
}
