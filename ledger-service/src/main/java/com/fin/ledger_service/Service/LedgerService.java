package com.fin.ledger_service.Service;

import com.fin.ledger_service.Dto.LedgerInitDto;
import com.fin.ledger_service.Dto.LedgerRequestDto;
import com.fin.ledger_service.Dto.LedgerSummaryDto;

import java.util.List;

public interface LedgerService {

    LedgerSummaryDto createTransaction(LedgerInitDto ledgerRequestDto);

    LedgerSummaryDto updateTransactionStatus(LedgerRequestDto request);

    void DeleteTransaction(LedgerRequestDto ledgerRequestDto);


    List<LedgerSummaryDto> getTransactionByGiver(LedgerRequestDto request, int page, int size, String sortBy, boolean ascending);

    List<LedgerSummaryDto> getTransactionByReceiverId(LedgerRequestDto request);

    List<LedgerSummaryDto> getTransactionByDate(LedgerRequestDto request);

    List<LedgerSummaryDto> getTransactionByStatus(LedgerRequestDto ledgerRequestDto, int page, int size, String sortBy, boolean ascending);

    List<LedgerSummaryDto> getTransactionByGiverAndStatus(LedgerRequestDto request, int page, int size, String sortBy, boolean ascending);

}
