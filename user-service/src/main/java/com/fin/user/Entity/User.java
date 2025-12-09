package com.fin.user.Entity;


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
@Table(name = "user_table")
public class User{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long userId;

    @NotBlank(message = "Username is mandatory")
    @Column(unique = true, nullable = false, length = 50)
    String username;

    @NotBlank(message = "Email is mandatory")
    @Email
    @Column(unique = true, nullable = false, length = 20)
    String email;

    @NotBlank
    String password;

    @Builder.Default
    boolean isActive = false;

    @Builder.Default
    boolean isEmailVerified = false;

}
