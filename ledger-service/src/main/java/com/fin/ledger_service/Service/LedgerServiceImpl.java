package com.fin.ledger_service.Service;

import com.fin.ledger_service.Dto.LedgerInitDto;
import com.fin.ledger_service.Dto.LedgerRequestDto;
import com.fin.ledger_service.Dto.LedgerSummaryDto;
import com.fin.ledger_service.Entity.Ledger;
import com.fin.ledger_service.GlobalExceptions.DoesNotExistException;
import com.fin.ledger_service.LedgerRepository.LedgerRepository;
import com.fin.ledger_service.Mapper.LedgerMapper;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


import java.awt.print.Pageable;
import java.time.LocalDate;
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


        ledger.setUpdatedAt(LocalDate.now());

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

        LocalDate date = request.getTransactionDate();

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
    public List<LedgerSummaryDto> getTransactionByStatus(LedgerRequestDto request, int page, int size, String sortBy, boolean ascending) {

        Boolean status = request.getStatus();
        if (status == null) {
            throw new IllegalArgumentException("Status cannot be null for this search");
        }

 
        Sort sort = ascending ?
                Sort.by(Sort.Direction.ASC, sortBy) :
                Sort.by(Sort.Direction.DESC, sortBy);
        
        Pageable pageable = (Pageable) PageRequest.of(page, size, sort);

       
        Page<Ledger> ledgerPage = ledgerRepository.findByStatus(status, pageable);

        
        return ledgerPage
                .getContent()
                .stream()
                .map(ledgerMapper::mapToSummaryDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<LedgerSummaryDto> getTransactionByGiver(LedgerRequestDto request, int page, int size, String sortBy, boolean ascending) {

        if(request.getGiverId() == null ) {
            throw new IllegalArgumentException("your Id is null");
        }

        Long giverId = request.getGiverId();

        Sort sort = ascending ?
                Sort.by(Sort.Direction.ASC, sortBy) :
                Sort.by(Sort.Direction.DESC, sortBy);

        Pageable pageable = (Pageable) PageRequest.of(page, size, sort);


        Page<Ledger> ledgerPage = ledgerRepository.findByGiverId(giverId, pageable);
        

        return ledgerPage
                .getContent()
                .stream()
                .map(ledgerMapper::mapToSummaryDto)
                .collect(Collectors.toList());
    }



    @Override
    public List<LedgerSummaryDto> getTransactionByGiverAndStatus(LedgerRequestDto request, int page, int size, String sortBy, boolean ascending) {

        if(request.getGiverId() == null ||
                request.getStatus() == null) {
            throw new IllegalArgumentException("your Id or status is null");
        }

        Long giverId = request.getGiverId();
        boolean status = request.getStatus();
        
        Sort sort = ascending ?
                Sort.by(Sort.Direction.ASC, sortBy) :
                Sort.by(Sort.Direction.DESC, sortBy);

        Pageable pageable = (Pageable) PageRequest.of(page, size, sort);


        Page<Ledger> ledgerPage = ledgerRepository.findByGiverIdAndStatus(giverId, status, pageable);


        return ledgerPage
                .getContent()
                .stream()
                .map(ledgerMapper::mapToSummaryDto)
                .collect(Collectors.toList());
    }

}
