package com.fin.friend_service.Entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Table(name = "friend")
public class Friend {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long friendshipId;

    @NotBlank(message = "Username is mandatory")
    @Column(unique = true, nullable = false, length = 50)
    String username;

    @NotBlank(message = "Email is mandatory")
    @Email
    @Column(unique = true, nullable = false, length = 20)
    String userEmail;

    @NotBlank(message = "Username is mandatory")
    @Column(unique = true, nullable = false, length = 50)
    String friendName;

    @NotBlank(message = "Email is mandatory")
    @Email
    @Column(unique = true, nullable = false, length = 20)
    String friendEmail;

}
