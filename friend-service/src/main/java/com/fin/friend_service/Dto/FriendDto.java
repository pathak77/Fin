package com.fin.friend_service.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;



@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Component
public class FriendDto {

    Long friendshipId;
    String username;
    String userEmail;
    String friendName;
    String friendEmail;

}
