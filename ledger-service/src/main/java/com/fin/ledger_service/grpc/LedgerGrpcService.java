package com.fin.ledger_service.grpc;

import TransactionService.*;
import com.fin.ledger_service.Dto.LedgerInitDto;
import com.fin.ledger_service.Dto.LedgerRequestDto;
import com.fin.ledger_service.Dto.LedgerSummaryDto;
import com.fin.ledger_service.Mapper.GrpcMapper;
import com.fin.ledger_service.Service.LedgerServiceImpl;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;

import net.devh.boot.grpc.server.service.GrpcService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;


@GrpcService
public class LedgerGrpcService extends TransactionServiceGrpc.TransactionServiceImplBase {

        private final LedgerServiceImpl ledgerService;
        private final GrpcMapper mapper;

        LedgerGrpcService(LedgerServiceImpl ledgerService, GrpcMapper mapper) {
            this.ledgerService = ledgerService;
            this.mapper = new GrpcMapper();
        }


        @Override
        public void createTransaction(LedgerInitRequest request,
                                      StreamObserver<LedgerSummaryResponse> responseObserver) {
                try {

                        LedgerInitDto dto = new LedgerInitDto();

                        dto.setGiverId(request.getGiverId());
                        dto.setReceiverId(request.getReceiverId());
                        dto.setAmount(new BigDecimal(request.getAmount()));
                        dto.setStatus(request.getStatus());
                        dto.setCreatedAt(LocalDate.parse(request.getCreatedAt()));

                        LedgerSummaryDto summary = ledgerService.createTransaction(dto);
                        responseObserver.onNext(mapper.mapToLedgerSummaryResponse(summary));
                        responseObserver.onCompleted();
                } catch (Exception e) {
                        responseObserver.onError(Status.INTERNAL.withDescription(e.getMessage()).asRuntimeException());
                }
        }

        @Override
        public void getTransactionsByGiver(GiverRequest request,
                                           StreamObserver<LedgerListResponse> responseObserver) {
                try {

                        LedgerRequestDto internalRequest = new LedgerRequestDto();
                        internalRequest.setGiverId(request.getGiverId());

                        List<LedgerSummaryDto> results = ledgerService.getTransactionByGiver(
                                internalRequest,
                                request.getPage(),
                                request.getSize(),
                                request.getSortBy(),
                                request.getAscending()
                        );

                        LedgerListResponse response = LedgerListResponse.newBuilder()
                                .addAllTransactions(results.stream().map(mapper::mapToLedgerSummaryResponse).toList())
                                .build();

                        responseObserver.onNext(response);
                        responseObserver.onCompleted();
                } catch (Exception e) {
                        responseObserver.onError(Status.INVALID_ARGUMENT.withDescription(e.getMessage()).asRuntimeException());
                }
        }

        @Override
        public void updateTransactionStatus(UpdateStatusRequest request,
                                            StreamObserver<LedgerSummaryResponse> responseObserver) {
                try {
                        LedgerRequestDto dto = new LedgerRequestDto();
                        dto.setLedgerId(request.getLedgerId());
                        if (request.hasStatus()) {
                                dto.setStatus(request.getStatus());
                        }

                        LedgerSummaryDto updated = ledgerService.updateTransactionStatus(dto);
                        responseObserver.onNext(mapper.mapToLedgerSummaryResponse(updated));
                        responseObserver.onCompleted();
                } catch (Exception e) {
                        responseObserver.onError(Status.NOT_FOUND.withDescription(e.getMessage()).asRuntimeException());
                }
        }

}