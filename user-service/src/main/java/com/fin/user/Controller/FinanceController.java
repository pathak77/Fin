package com.fin.user.Controller;


import FriendshipService.FriendshipServiceGrpc;
import TransactionService.LedgerSummaryResponse;
import com.fin.user.Dto.FriendDto.FriendResponseDto;
import com.fin.user.Dto.FriendDto.FriendSummaryDto;
import com.fin.user.Dto.LedgerDto.LedgerSummaryDto;
import com.fin.user.Dto.UserDto;
import com.fin.user.Entity.User;
import com.fin.user.Service.FriendshipService.FriendshipServiceGrpcClient;
import com.fin.user.Service.LedgerService.LedgerServiceGrpcClient;
import com.fin.user.Service.UserServiceImpl;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/app")
public class FinanceController {

    private final UserServiceImpl userService;
    private final LedgerServiceGrpcClient ledgerService;
    private final FriendshipServiceGrpcClient friendService;


     public FinanceController(UserServiceImpl userService,
                              LedgerServiceGrpcClient ledgerService,
                              FriendshipServiceGrpcClient friendService)
     {
        this.userService = userService;
        this.ledgerService = ledgerService;
         this.friendService = friendService;
     }

    @GetMapping
    public ResponseEntity<List<User>> getAll() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{username}")
    public ResponseEntity<UserDto> getUser(@PathVariable String username){
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
            return ResponseEntity.noContent().build();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with id: " + id);
        }
    }

// FRIEND SERVICE IMPLEMENTATION
    @GetMapping("{userId}/friend")
    public ResponseEntity<FriendSummaryDto> getAllFriends(@PathVariable Long userId) {
        String user = String.valueOf(userId);

        FriendSummaryDto response = friendService.getAllFriendship(user);

        return ResponseEntity.ok(response);
    }


    @PostMapping("/friend")
    public ResponseEntity<FriendResponseDto> addFriend(@Valid FriendResponseDto request){

         String user = String.valueOf(request.getUserId());
         String friend = String.valueOf(request.getFriendId());

         FriendResponseDto response = friendService.createFriendship(user, friend);
         return ResponseEntity.created(URI.create("/friend/" + response.getFriendId()))
                 .body(response);

    }

    @DeleteMapping("/friend")
    public ResponseEntity<?> deleteFriend(@Valid FriendResponseDto request){

         String user = String.valueOf(request.getUserId());
         String friend = String.valueOf(request.getFriendId());

         boolean response = friendService.deleteFriendship(user, friend);
         return ResponseEntity.ok(response);
    }



// LEDGER SERVICE IMPLEMENTATION


    @PostMapping("{uid}/ledger/{fid}")
    public ResponseEntity<LedgerSummaryResponse> createTransaction(
            @PathVariable Long uid,
            @PathVariable Long fid,
            @RequestParam(name = "amount",
                    required = false,
                    defaultValue = "0") BigDecimal amount) {

        return ResponseEntity.ok(ledgerService.createTransaction(uid, fid, amount));

    }


    @PatchMapping("ledger/{ledgerId}/status")
    public ResponseEntity<LedgerSummaryResponse> updateStatus(
            @PathVariable Long ledgerId,
            @RequestParam boolean paid) {
        return ResponseEntity.ok(ledgerService.updateTransactionStatus(ledgerId, paid));
    }


    @DeleteMapping("ledger/{ledgerId}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long ledgerId) {
        ledgerService.deleteTransaction(ledgerId);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("ledger/receiver/{receiverId}")
    public ResponseEntity<List<LedgerSummaryDto>> getByReceiver(
            @RequestParam Long giverId,
            @PathVariable Long receiverId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "true") boolean ascending) {

        List<LedgerSummaryDto> transactions = ledgerService.getTransactionsByReceiver(giverId, receiverId, page, size, sortBy, ascending);
        return ResponseEntity.ok(transactions);
    }


    @GetMapping("ledger/{giverId}/date")
    public ResponseEntity<List<LedgerSummaryDto>> getByDate(
            @PathVariable Long giverId,
            @RequestParam Long receiverId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "true") boolean ascending) {

        List<LedgerSummaryDto> transactions = ledgerService.getTransactionsByDate(giverId, receiverId, page, size, sortBy, ascending, date);
        return ResponseEntity.ok(transactions);
    }


    @GetMapping("{giverId}/ledger/giver/")
    public ResponseEntity<List<LedgerSummaryDto>> getByGiver(
            @PathVariable Long giverId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "true") boolean ascending) {

        List<LedgerSummaryDto> transactions = ledgerService.getTransactionsByGiver(giverId, page, size, sortBy, ascending);
        return ResponseEntity.ok(transactions);
    }


    @GetMapping("{giverId}/ledger/giver/status")
    public ResponseEntity<List<LedgerSummaryDto>> getByGiverAndStatus(
            @PathVariable Long giverId,
            @RequestParam boolean status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "true") boolean ascending) {

        List<LedgerSummaryDto> transactions = ledgerService.getTransactionsByGiverAndStatus(giverId, status, page, size, sortBy, ascending);
        return ResponseEntity.ok(transactions);
    }
}
