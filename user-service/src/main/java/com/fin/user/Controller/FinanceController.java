package com.fin.user.Controller;

import TransactionService.LedgerInitRequest;
import TransactionService.LedgerSummaryResponse;
import com.fin.user.Dto.LedgerInitRequestDTO;
import com.fin.user.Dto.UserDto;
import com.fin.user.Entity.User;
import com.fin.user.Service.UserServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/user")
public class FinanceController {

    UserServiceImpl userService;

     public FinanceController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<User>> getAll() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{username}")
    public ResponseEntity<UserDto> updateUser(@PathVariable String username){
         UserDto userDto = userService.getUserByUsername(username);
         return ResponseEntity.ok(userDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id,
                                              @Valid @RequestBody UserDto userDto) {
        if (!id.equals(userDto.getUserId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID in path must match ID in body");
        }

        UserDto updated = userService.updateUser(id, userDto);
        return ResponseEntity.ok(updated);
    }

    @PostMapping
    public ResponseEntity<UserDto> addUser(@Valid @RequestBody UserDto userDto) {
        UserDto created = userService.addUser(userDto);

        return ResponseEntity
                .created(URI.create("/user/" + created.getUserId()))
                .body(created);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        boolean deleted = userService.deleteUser(id);

        if (deleted) {
            return ResponseEntity.noContent().build(); // 204 No Content - standard for successful DELETE
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with id: " + id);
        }
    }



    @PostMapping("/transaction")
    public ResponseEntity<LedgerSummaryResponse> createTransaction(@RequestBody LedgerInitRequestDTO dto) {
        LedgerInitRequest request = LedgerInitRequest.newBuilder()
                .setGiverId(dto.getGiverId())
                .setReceiverId(dto.getReceiverId())
                .setAmount(dto.getAmount().toString())
                .setStatus(dto.isStatus())
                .setCreatedAt(LocalDate.now().toString())
                .build();

        return ResponseEntity.ok(ledgerStub.createTransaction(request));
    }


    @GetMapping("/giver/{userId}")
    public ResponseEntity<List<LedgerSummaryResponse>> getMyLentTransactions(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "false") boolean ascending) {

        GiverRequest request = GiverRequest.newBuilder()
                .setGiverId(userId)
                .setPage(page)
                .setSize(size)
                .setSortBy(sortBy)
                .setAscending(ascending)
                .build();

        LedgerListResponse response = ledgerStub.getTransactionsByGiver(request);
        return ResponseEntity.ok(response.getTransactionsList());
    }


    @PatchMapping("/{ledgerId}/status")
    public ResponseEntity<LedgerSummaryResponse> updateStatus(
            @PathVariable Long ledgerId,
            @RequestParam boolean paid) {

        UpdateStatusRequest request = UpdateStatusRequest.newBuilder()
                .setLedgerId(ledgerId)
                .setStatus(paid)
                .build();

        return ResponseEntity.ok(ledgerStub.updateTransactionStatus(request));
    }


    @DeleteMapping("/{ledgerId}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long ledgerId) {
        ledgerStub.deleteTransaction(DeleteLedgerRequest.newBuilder()
                .setLedgerId(ledgerId)
                .build());
        return ResponseEntity.noContent().build();
    }
}
