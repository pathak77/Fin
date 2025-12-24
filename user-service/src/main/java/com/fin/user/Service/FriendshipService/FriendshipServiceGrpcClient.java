package com.fin.user.Service.FriendshipService;

import FriendshipService.*;
import com.fin.user.Dto.FriendDto.FriendResponseDto;
import com.fin.user.Dto.FriendDto.FriendSummaryDto;
import com.fin.user.Mapper.FriendMapper;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class FriendshipServiceGrpcClient {

    private static final Logger logger = LoggerFactory.getLogger(FriendshipServiceGrpcClient.class);
    private final FriendshipServiceGrpc.FriendshipServiceBlockingStub blockingStub;
    private final ManagedChannel channel;
    private final FriendMapper mapper;

    public FriendshipServiceGrpcClient(
            @Value("${grpc.client.friend-service.address}") String serverAddress,
            @Value("${grpc.server.friend-service.port}") int port,
            FriendMapper mapper
    ){

        this.mapper = mapper;

        logger.info("Creating connection to the server {} {}", serverAddress, port);

         channel = ManagedChannelBuilder.forAddress(serverAddress, port).usePlaintext().build();

        blockingStub = FriendshipServiceGrpc.newBlockingStub(channel);

    }

    public FriendResponseDto createFriendship(String userOneId, String userTwoId){
        logger.info("Adding user {} to {} initialized ", userOneId, userTwoId);
        AddFriendRequest friendRequest = AddFriendRequest
                .newBuilder()
                .setUserOneId(userOneId)
                .setUserTwoId(userTwoId)
                .build();

        AddFriendResponse response =  blockingStub.createFriendship(friendRequest);
        return mapper.mapToFriendResponseDto(response);
    }

    public Boolean deleteFriendship(String userOneId, String userTwoId){
        logger.info("unfriending user {} to {} initialized ", userOneId, userTwoId);
        DeleteFriendRequest deleteFriendRequest = DeleteFriendRequest
                .newBuilder()
                .setUserOneId(userOneId)
                .setUserTwoId(userTwoId)
                .build();

        return blockingStub.deleteFriendship(deleteFriendRequest).getRes();

    }

    public FriendSummaryDto getAllFriendship(String userOneId){
        logger.info("getting all user relating to {} ", userOneId);

        GetAllFriendRequest allFriendRequest = GetAllFriendRequest
                .newBuilder()
                .setUserOneId(userOneId)
                .build();

        GetAllFriendResponse response =  blockingStub.getAllFriendship(allFriendRequest);

        return mapper.mapToFriendSummaryDto(response);
    }

    @PreDestroy
    public void shutdown() {
        if (channel != null && !channel.isShutdown()) {
            try {
                logger.info("Shutting down gRPC channel.");
                channel.shutdown().awaitTermination(5, java.util.concurrent.TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                logger.error("gRPC channel shutdown interrupted.", e);
            }
        }
    }
}
