package com.fin.friend_service.Entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Table(
        name = "Friend",
        uniqueConstraints = @UniqueConstraint(columnNames = {"userOneId", "userTwoId"})
)
public class Friend {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    Long userOneId;

    Long userTwoId;

    @Builder.Default
    Date date = new Date();

    @Builder.Default
    int maxCredit = 10000000;

}
