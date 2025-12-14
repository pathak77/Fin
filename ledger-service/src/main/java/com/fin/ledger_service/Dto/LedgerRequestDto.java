package com.fin.ledger_service.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Component
public class LedgerRequestDto {

    Long ledgerId;
    Long giverId;
    Long receiverId;
    Date transactionDate;
    Boolean status;

}