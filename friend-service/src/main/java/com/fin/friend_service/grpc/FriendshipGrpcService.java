package com.fin.friend_service.grpc;

import FriendshipService.AddFriendRequest;
import FriendshipService.AddFriendResponse;
import FriendshipService.FriendshipServiceGrpc;
import com.fin.friend_service.Service.FriendServiceImpl;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@GrpcService
public class  FriendshipGrpcService extends FriendshipServiceGrpc.FriendshipServiceImplBase {

    public final FriendServiceImpl friendService;

    FriendshipGrpcService(FriendServiceImpl friendService){
        this.friendService = friendService;
    }
    private static final Logger logger = LoggerFactory
            .getLogger(FriendshipGrpcService.class);

    @Override
    public void createFriendship(
            AddFriendRequest request,
            StreamObserver<AddFriendResponse> responseObserver) {

        logger.info("Create friendship request received {}", request.toString());

        Long one = Long.valueOf(request.getUserOneId());
        Long two = Long.valueOf(request.getUserTwoId());

        friendService.addFriend(one, two);

        responseObserver.onNext(AddFriendResponse.newBuilder().build());
        responseObserver.onCompleted();


    }


}
