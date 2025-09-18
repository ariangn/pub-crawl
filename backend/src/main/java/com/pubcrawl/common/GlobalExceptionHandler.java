package com.pubcrawl.common;

import com.pubcrawl.user.DuplicateEmailException;
import com.pubcrawl.user.DuplicateUsernameException;
import com.pubcrawl.group.DuplicateGroupNameException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorDto> handleUnreadableMessage() {
        return ResponseEntity.badRequest().body(
            new ErrorDto("Invalid request body")
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(
            MethodArgumentNotValidException exception
    ) {
        var errors = new HashMap<String, String>();

        exception.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });

        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Map<String, String>> handleBadCredentials() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
            Map.of("error", "Invalid credentials, please try again.")
        );
    }

    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<Map<String, String>> handleDuplicateEmail() {
        return ResponseEntity.badRequest().body(
            Map.of("error", "Email is already registered.")
        );
    }

    @ExceptionHandler(DuplicateUsernameException.class)
    public ResponseEntity<Map<String, String>> handleDuplicateUsername() {
        return ResponseEntity.badRequest().body(
            Map.of("error", "Username is already taken.")
        );
    }

    @ExceptionHandler(DuplicateGroupNameException.class)
    public ResponseEntity<Map<String, String>> handleDuplicateGroupName() {
        return ResponseEntity.badRequest().body(
            Map.of("error", "Group name is already taken.")
        );
    }
}