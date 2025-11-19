//package com.fin.user.GlobalExceptions;
//
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.context.request.WebRequest;
//
//import java.nio.file.attribute.UserPrincipalNotFoundException;
//
//@ControllerAdvice
//public class UserExceptions{
//
//    @ExceptionHandler(UserPrincipalNotFoundException.class)
//    public ResponseEntity<ErrorDetails> handleUserNotFoundException(UserNotFoundException ex, WebRequest request) {
//        ErrorDetails errorDetails = new ErrorDetails(
//                System.currentTimeMillis(),
//                ex.getMessage(),
//                request.getDescription(false)
//        );
//        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
//    }
//}
