package com.fin.user.Dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Component
public class LedgerRequestDto {

    Long ledgerId;

    @NotNull
    Long giverId;

    Long receiverId;

    @NotNull
    LocalDate transactionDate;

    Boolean status;

}