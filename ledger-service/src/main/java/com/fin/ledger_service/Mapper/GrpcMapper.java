package com.fin.ledger_service.Mapper;

import TransactionService.LedgerSummaryResponse;
import com.fin.ledger_service.Dto.LedgerSummaryDto;
import org.springframework.stereotype.Component;

@Component
public class GrpcMapper {

    public LedgerSummaryResponse mapToLedgerSummaryResponse(LedgerSummaryDto dto) {
        return LedgerSummaryResponse.newBuilder()
                .setLedgerId(dto.getLedgerId())
                .setGiverId(dto.getGiverId())
                .setReceiverId(dto.getReceiverId())
                .setAmount(dto.getAmount().toString())
                .setStatus(dto.getStatus())
                .setCreatedAt(dto.getCreateDate().toString())
                .setUpdatedAt(dto.getUpdateDate() != null ? dto.getUpdateDate().toString() : "")
                .setDescription(dto.getDescription())
                .build();
    }


}
