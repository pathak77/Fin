package com.fin.user.Mapper;

import TransactionService.LedgerListResponse;
import com.fin.user.Dto.LedgerDto.LedgerSummaryDto;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class LedgerMapper{

    public List<LedgerSummaryDto> mapToLedgerSummaryList(LedgerListResponse protoResponse) {
        return protoResponse.getTransactionsList().stream()
                .map(proto -> LedgerSummaryDto.builder()
                        .ledgerId(proto.getLedgerId())
                        .giverId(proto.getGiverId())
                        .receiverId(proto.getReceiverId())
                        .amount(new BigDecimal(proto.getAmount()))
                        .status(proto.getStatus())
                        .createDate(LocalDate.parse(proto.getCreatedAt()))
                        .updateDate(LocalDate.parse(proto.getUpdatedAt()))
                        .description(proto.getDescription())
                        .build())
                .collect(Collectors.toList());
    }

}

