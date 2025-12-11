package com.fin.friend_service.grpc;

import FriendshipService.AddFriendRequest;
import FriendshipService.AddFriendResponse;
import FriendshipService.FriendshipServiceGrpc;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@GrpcService
public class  FriendshipGrpcService extends FriendshipServiceGrpc.FriendshipServiceImplBase {

    private static final Logger logger = LoggerFactory
            .getLogger(FriendshipGrpcService.class);

    @Override
    public void createFriendship(
            AddFriendRequest request,
            StreamObserver<AddFriendResponse> responseObserver) {

        logger.info("Create friendship request received {}", request.toString());



    }


}
