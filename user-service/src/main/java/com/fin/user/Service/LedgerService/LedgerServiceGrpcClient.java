package com.fin.user.Service.LedgerService;

import FriendshipService.GetAllFriendRequest;
import TransactionService.*;
import com.fin.user.Dto.LedgerInitRequestDTO;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class LedgerServiceGrpcClient {

  @GrpcClient("transaction-service")
        private TransactionServiceGrpc.TransactionServiceBlockingStub transactionStub;

        public LedgerSummaryResponse createTransaction(Long giverId, Long receiverId, BigDecimal amount) {
            LedgerInitRequest request = LedgerInitRequest.newBuilder()
                    .setGiverId(giverId)
                    .setReceiverId(receiverId)
                    .setAmount(amount.toString())
                    .setStatus(false)
                    .setCreatedAt(LocalDate.now().toString())
                    .build();
            return transactionStub.createTransaction(request);
        }

    public ResponseEntity<LedgerSummaryResponse> createTransaction(@RequestBody LedgerInitRequestDTO dto) {
        LedgerInitRequest request = LedgerInitRequest.newBuilder()
                .setGiverId(dto.getGiverId())
                .setReceiverId(dto.getReceiverId())
                .setAmount(dto.getAmount().toString())
                .setStatus(dto.isStatus())
                .setCreatedAt(LocalDate.now().toString())
                .build();

        return ResponseEntity.ok(transactionStub.createTransaction(request));
    }



    public ResponseEntity<List<LedgerSummaryResponse>> getMyLentTransactions(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "false") boolean ascending) {

        GiverRequest request = GiverRequest.newBuilder()
                .setGiverId(userId)
                .setPage(page)
                .setSize(size)
                .setSortBy(sortBy)
                .setAscending(ascending)
                .build();

        LedgerListResponse response = transactionStub.getTransactionsByGiver(request);
        return ResponseEntity.ok(response.getTransactionsList());
    }



    public ResponseEntity<LedgerSummaryResponse> updateStatus(
            @PathVariable Long ledgerId,
            @RequestParam boolean paid) {

        UpdateStatusRequest request = UpdateStatusRequest.newBuilder()
                .setLedgerId(ledgerId)
                .setStatus(paid)
                .build();

        return ResponseEntity.ok(transactionStub.updateTransactionStatus(request));
    }



    public ResponseEntity<Void> deleteTransaction(@PathVariable Long ledgerId) {
        EmptyResponse flag = transactionStub.deleteTransaction(DeleteLedgerRequest.newBuilder()
                .setLedgerId(ledgerId)
                .build());

        return ResponseEntity.noContent().build();
    }
}
