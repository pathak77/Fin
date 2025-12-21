package com.fin.friend_service.grpc;

import FriendshipService.*;
import com.fin.friend_service.Service.FriendServiceImpl;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

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
        try{
            Long one = Long.valueOf(request.getUserOneId());
            Long two = Long.valueOf(request.getUserTwoId());

            friendService.addFriend(one, two);

            responseObserver.onNext(AddFriendResponse.newBuilder().build());
            responseObserver.onCompleted();
        }
        catch( Exception e ){
            logger.info("either users are empty");
            responseObserver.onError(e);
        }



    }

    @Override
    public void getAllFriendship(
            GetAllFriendRequest request,
            StreamObserver<GetAllFriendResponse> responseObserver
    ){
        try {
            Long userId = Long.valueOf(request.getUserOneId());

            List<Long> friends = friendService.getMyFriends(userId);

            GetAllFriendResponse response = GetAllFriendResponse.newBuilder()
                    .setUserOneId(userId.toString())
                    .addAllFriendId(friends.stream().map(String::valueOf).collect(Collectors.toList()))
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(io.grpc.Status.INTERNAL.asRuntimeException());
        }
    }

    @Override
    public void checkFriendStatus(StatusRequest request, StreamObserver<StatusResponse> responseObserver) {
        try {
            boolean isFriend = friendService.areFriends(
                    Long.valueOf(request.getUserId()),
                    Long.valueOf(request.getFriendId())
            );

            responseObserver.onNext(StatusResponse.newBuilder().setAreFriends(isFriend).build());
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(Status.INVALID_ARGUMENT.asRuntimeException());
        }
    }

    @Override
    public void getBalanceWithFriend(BalanceRequest request, StreamObserver<BalanceResponse> responseObserver) {
        try {
            BigDecimal balance = friendService.getBalanceWithFriend(
                    Long.valueOf(request.getUserId()),
                    Long.valueOf(request.getFriendId())
            );

            responseObserver.onNext(BalanceResponse.newBuilder()
                    .setBalance(balance.toString())
                    .build());
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(Status.INTERNAL.withDescription("Error fetching balance").asRuntimeException());
        }
    }

    @Override
    public void deleteFriendship(
            DeleteFriendRequest request,
            StreamObserver<DeleteFriendResponse> responseObserver
    ){
        try {

            Long userOneId = Long.valueOf(request.getUserOneId());
            Long userTwoId = Long.valueOf(request.getUserTwoId());
            friendService.removeFriend(userOneId, userTwoId);

            DeleteFriendResponse response = DeleteFriendResponse
                    .newBuilder()
                    .setRes(true)
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(io.grpc.Status.NOT_FOUND.withDescription(e.getMessage()).asRuntimeException());
        }
    }


}
