package com.fin.user.Service.FriendshipService;

import FriendshipService.*;
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

    public FriendshipServiceGrpcClient(
            @Value("${friendship.service.address:localhost}") String serverAddress,
            @Value("${friendship.service.grpc.port:9001}") int port
    ){
        logger.info("Creating connection to the server {} {}", serverAddress, port);


         channel = ManagedChannelBuilder.forAddress(serverAddress, port).usePlaintext().build();

        blockingStub = FriendshipServiceGrpc.newBlockingStub(channel);

    }

    public AddFriendResponse createFriendship(String userOneId, String userTwoId){
        logger.info("Adding user {} to {} initialized ", userOneId, userTwoId);
        AddFriendRequest friendRequest = AddFriendRequest
                .newBuilder()
                .setUserOneId(userOneId)
                .setUserTwoId(userTwoId)
                .build();

        return blockingStub.createFriendship(friendRequest);
    }

    public DeleteFriendResponse deleteFriendship(String userOneId, String userTwoId){
        logger.info("unfriending user {} to {} initialized ", userOneId, userTwoId);
        DeleteFriendRequest deleteFriendRequest = DeleteFriendRequest
                .newBuilder()
                .setUserOneId(userOneId)
                .setUserTwoId(userTwoId)
                .build();

        return blockingStub.deleteFriendship(deleteFriendRequest);
    }

    public GetAllFriendResponse getAllFriendship(String userOneId){
        logger.info("getting all user relating to {} ", userOneId);

        GetAllFriendRequest allFriendRequest = GetAllFriendRequest
                .newBuilder()
                .setUserOneId(userOneId)
                .build();

        return blockingStub.getAllFriendship(allFriendRequest);
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
