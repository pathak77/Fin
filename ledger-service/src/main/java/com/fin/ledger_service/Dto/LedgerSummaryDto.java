package com.fin.ledger_service.Dto;

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
public class LedgerSummaryDto {

    Long ledgerId;
    Long giverId;
    Long receiverId;
    BigDecimal amount;
    String description;
    LocalDate createDate;
    LocalDate updateDate;
    Boolean status;

}
