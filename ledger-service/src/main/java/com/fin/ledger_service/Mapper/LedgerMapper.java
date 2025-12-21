package com.fin.ledger_service.Mapper;


import com.fin.ledger_service.Dto.LedgerSummaryDto;
import com.fin.ledger_service.Entity.Ledger;
import lombok.Builder;
import lombok.Data;
import org.springframework.stereotype.Component;

@Builder
@Data
@Component
public class LedgerMapper {

    public LedgerSummaryDto mapToSummaryDto(Ledger ledger) {
        return LedgerSummaryDto.builder()
                .ledgerId(ledger.getLedgerId())
                .giverId(ledger.getGiverId())
                .receiverId(ledger.getReceiverId())
                .amount(ledger.getAmount())
                .status(ledger.getStatus())
                .createDate(ledger.getCreatedAt())
                .updateDate(ledger.getUpdatedAt())
                .build();
    }


}
