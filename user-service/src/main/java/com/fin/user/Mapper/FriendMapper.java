package com.fin.user.Mapper;

import FriendshipService.AddFriendResponse;
import FriendshipService.GetAllFriendResponse;
import com.fin.user.Dto.FriendDto.FriendResponseDto;
import com.fin.user.Dto.FriendDto.FriendSummaryDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FriendMapper {

    public FriendResponseDto mapToFriendResponseDto(AddFriendResponse response){

        return FriendResponseDto.builder()
                .userId(Long.valueOf(response.getUserOneId()))
                .friendId(Long.valueOf(response.getUserTwoId()))
                .build();
    }

    public FriendSummaryDto mapToFriendSummaryDto(GetAllFriendResponse response){

        Long userOneId = Long.valueOf(response.getUserOneId());
        int count = response.getFriendIdCount();
        List<Long> list = response.getFriendIdList().stream().map(Long::valueOf).toList();

        return FriendSummaryDto.builder()
                .userOneId(userOneId)
                .count(count)
                .friendList(list)
                .build();
    }
}
