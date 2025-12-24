package com.fin.user.Service.LedgerService;

import TransactionService.*;
import com.fin.user.Dto.LedgerDto.LedgerSummaryDto;
import com.fin.user.Mapper.LedgerMapper;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class LedgerServiceGrpcClient {

    private static final Logger logger = LoggerFactory.getLogger(LedgerServiceGrpcClient.class);

    private final TransactionServiceGrpc.TransactionServiceBlockingStub transactionStub;

    private final LedgerMapper mapper;

    LedgerServiceGrpcClient(
            @Value("${grpc.client.ledger-service.address}") String serverAddress,
            @Value("${grpc.server.ledger-service.port}") int port,
            LedgerMapper mapper
    ){
        this.mapper = mapper;

        logger.info("Transaction service initialized address : {} , port : {}", serverAddress, port);

        ManagedChannel channel = ManagedChannelBuilder.forAddress(serverAddress, port).usePlaintext().build();

        transactionStub = TransactionServiceGrpc.newBlockingStub(channel);

    }

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

    public LedgerSummaryResponse updateTransactionStatus(Long ledgerId, boolean paid) {

        UpdateStatusRequest request = UpdateStatusRequest.newBuilder()
                .setLedgerId(ledgerId)
                .setStatus(paid)
                .build();
        return transactionStub.updateTransactionStatus(request);
    }

    public void deleteTransaction(Long ledgerId) {
        DeleteLedgerRequest request = DeleteLedgerRequest.newBuilder()
                .setLedgerId(ledgerId)
                .build();

        transactionStub.deleteTransaction(request);
    }

    public List<LedgerSummaryDto> getTransactionsByReceiver(
            Long giverId,
            Long receiverID,
            int page,
            int size,
            String sortBy,
            boolean ascending
    ) {

        ReceiverRequest request = ReceiverRequest.newBuilder()
                .setGiverId(giverId)
                .setReceiverId(receiverID)
                .setPage(page)
                .setSize(size)
                .setSortBy(sortBy)
                .setAscending(ascending)
                .build();

        LedgerListResponse response = transactionStub.getTransactionsByReceiver(request);


        return mapper.mapToLedgerSummaryList(response);
    }

    public List<LedgerSummaryDto> getTransactionsByDate(
            Long giverId,
            Long receiverID,
            int page,
            int size,
            String sortBy,
            boolean ascending,
            LocalDate date
    ) {

        DateRequest request = DateRequest.newBuilder()
                .setTransactionDate(String.valueOf(date))
                .setGiverId(giverId)
                .setReceiverId(receiverID)
                .setPage(page)
                .setSize(size)
                .setSortBy(sortBy)
                .setAscending(ascending)
                .build();

        LedgerListResponse response = transactionStub.getTransactionsByDate(request);
        return mapper.mapToLedgerSummaryList(response);
    }


    public List<LedgerSummaryDto> getTransactionsByGiver(
            Long giverId,
            int page,
            int size,
            String sortBy,
            boolean ascending
    ) {

        GiverRequest request = GiverRequest.newBuilder()
                .setGiverId(giverId)
                .setPage(page)
                .setSize(size)
                .setSortBy(sortBy)
                .setAscending(ascending)
                .build();

        LedgerListResponse response = transactionStub.getTransactionsByGiver(request);
        return mapper.mapToLedgerSummaryList(response);
    }
    public List<LedgerSummaryDto> getTransactionsByGiverAndStatus(
            Long giverId,
            boolean status,
            int page,
            int size,
            String sortBy,
            boolean ascending)
    {

        GiverStatusRequest request = GiverStatusRequest.newBuilder()
                .setGiverId(giverId)
                .setStatus(status)
                .setPage(page)
                .setSize(size)
                .setSortBy(sortBy)
                .setAscending(ascending)
                .build();

        LedgerListResponse response = transactionStub.getTransactionsByGiverAndStatus(request);
        return mapper.mapToLedgerSummaryList(response);

    }


}
