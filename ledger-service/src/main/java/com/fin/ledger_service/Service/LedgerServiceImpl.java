package com.fin.ledger_service.Service;

import com.fin.ledger_service.Dto.LedgerInitDto;
import com.fin.ledger_service.Dto.LedgerRequestDto;
import com.fin.ledger_service.Dto.LedgerSummaryDto;
import com.fin.ledger_service.Entity.Ledger;
import com.fin.ledger_service.GlobalExceptions.DoesNotExistException;
import com.fin.ledger_service.LedgerRepository.LedgerRepository;
import com.fin.ledger_service.Mapper.LedgerMapper;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;


import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class LedgerServiceImpl implements LedgerService {

    private final LedgerMapper ledgerMapper;
    private final LedgerRepository ledgerRepository;

    public LedgerServiceImpl(LedgerRepository ledgerRepository, LedgerMapper ledgerMapper) {
        this.ledgerRepository = ledgerRepository;
        this.ledgerMapper = ledgerMapper;
    }



    @Override
    @Transactional
    public LedgerSummaryDto createTransaction(LedgerInitDto request) {
        Ledger ledger = Ledger.builder()
                .giverId(request.getGiverId())
                .receiverId(request.getReceiverId())
                .amount(request.getAmount())
                .status(request.getStatus())
                .createdAt(request.getCreatedAt())
                .updatedAt(request.getUpdatedAt())
                .build();

        Ledger savedLedger = ledgerRepository.save(ledger);

        return ledgerMapper.mapToSummaryDto(savedLedger);
    }



    @Override
    @Transactional
    public LedgerSummaryDto updateTransactionStatus(LedgerRequestDto request) {

        Boolean exists = ledgerRepository.existsByLedgerId(request.getLedgerId());

        if(!exists) {
            throw new DoesNotExistException("this transaction doesn't exist");
        }

        Ledger ledger = ledgerRepository.findByLedgerId(request.getLedgerId());

        if (request.getStatus() != null) {
            ledger.setStatus(request.getStatus());
        } else {
            ledger.setStatus(true);
        }

        ledger.setUpdatedAt(new Date());

        Ledger updatedLedger = ledgerRepository.save(ledger);
        return ledgerMapper.mapToSummaryDto(updatedLedger);
    }



    @Override
    @Transactional
    public void DeleteTransaction(LedgerRequestDto request) {

        if (ledgerRepository.existsById(request.getLedgerId())) {
            ledgerRepository.deleteById(request.getLedgerId());
        } else {
            throw new RuntimeException("Cannot delete: Transaction ID not found");
        }
    }



    @Override
    public List<LedgerSummaryDto> getTransactionByReceiverId(LedgerRequestDto request) {

        if(request.getGiverId() == null ||
        request.getReceiverId() == null ) {
            throw new IllegalArgumentException("your Id is null");
        }


        Long giverId = request.getGiverId();
        Long receiverId = request.getReceiverId();

        return ledgerRepository.findByGiverIdAndReceiverId( giverId, receiverId )
                .stream()
                .map(ledgerMapper::mapToSummaryDto)
                .collect(Collectors.toList());


    }



    @Override
    public List<LedgerSummaryDto> getTransactionByDate(LedgerRequestDto request) {

        Date date = request.getTransactionDate();

        if (date == null) {
            throw new IllegalArgumentException("your Date is null");
        }

        List<Ledger> ledgers = ledgerRepository.findByCreatedAt(date);

        return ledgers
                .stream()
                .map(ledgerMapper::mapToSummaryDto)
                .collect(Collectors.toList());
    }



    @Override
    public List<LedgerSummaryDto> getTransactionByStatus(LedgerRequestDto request) {
        Boolean status = request.getStatus();
        if (status == null) {
            throw new IllegalArgumentException("your Status is null");
        }


        List<Ledger> ledgers = ledgerRepository.findByStatus(status, );

        return ledgers.stream()
                .map(ledgerMapper::mapToSummaryDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<LedgerSummaryDto> getTransactionByGiver(LedgerRequestDto request) {

        if(request.getGiverId() == null ) {
            throw new IllegalArgumentException("your Id is null");
        }

        Long giverId = request.getGiverId();

        List<Ledger> ledgers = ledgerRepository.findByGiverId(giverId, );

        return ledgers.stream()
                .map(ledgerMapper::mapToSummaryDto)
                .collect(Collectors.toList());
    }



    @Override
    public List<LedgerSummaryDto> getTransactionByGiverAndStatus(LedgerRequestDto request) {

        if(request.getGiverId() == null ||
                request.getStatus() == null) {
            throw new IllegalArgumentException("your Id or status is null");
        }

        Long giverId = request.getGiverId();
        boolean status = request.getStatus();

        List<Ledger> ledgers = ledgerRepository.findByGiverIdAndStatus(giverId, );

        return ledgers.stream()
                .map(ledgerMapper::mapToSummaryDto)
                .collect(Collectors.toList());
    }

}
