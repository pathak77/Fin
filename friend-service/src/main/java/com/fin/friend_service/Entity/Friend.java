package com.fin.friend_service.Entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

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
    Long friendshipId;


            @Column(unique = true)
    Long userOneId;


    @Column(unique = true)
    Long userTwoId;

    @Builder.Default
    LocalDate createdAt;

}
