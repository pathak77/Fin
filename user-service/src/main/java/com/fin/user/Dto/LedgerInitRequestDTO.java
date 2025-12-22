package com.fin.user.Dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class LedgerInitRequestDTO {
    private Long giverId;
    private Long receiverId;
    private BigDecimal amount;
    private boolean status;
}