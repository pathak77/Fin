package com.fin.ledger_service.LedgerRepository;

import com.fin.ledger_service.Entity.Ledger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.Date;
import java.util.List;


@Repository
public interface LedgerRepository extends JpaRepository<Ledger, Long> {

    Boolean existsByLedgerId(Long ledgerId);

    Ledger findByLedgerId(Long ledgerId);

    List<Ledger> findByCreatedAt(Date date);

    List<Ledger> findByStatus(Boolean status, Pageable pageable);

    List<Ledger> findByGiverIdAndStatus(Long giverId, Boolean status, Pageable pageable);


    List<Ledger> findByGiverIdAndReceiverId(Long giverId, Long receiverId);

    List<Ledger> findByGiverId(Long giverId, Pageable pageable);

}
