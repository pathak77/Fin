package com.fin.ledger_service.LedgerRepository;

import com.fin.ledger_service.Entity.Ledger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.time.LocalDate;
import java.util.List;


@Repository
public interface LedgerRepository extends JpaRepository<Ledger, Long> {

    Boolean existsByLedgerId(Long ledgerId);

    Ledger findByLedgerId(Long ledgerId);

    List<Ledger> findByCreatedAt(LocalDate date);


    List<Ledger> findByGiverIdAndReceiverId(Long giverId, Long receiverId);

    Page<Ledger> findByStatus(Boolean status, Pageable pageable);

    Page<Ledger> findByGiverIdAndStatus(Long giverId, Boolean status, Pageable pageable);

    Page<Ledger> findByGiverId(Long giverId, Pageable pageable);
}
