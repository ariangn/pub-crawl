package com.pubcrawl.user;

import com.pubcrawl.auth.JwtResponse;
import com.pubcrawl.auth.JwtService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    @GetMapping
    public Iterable<UserDto> getAllUsers(
        @RequestParam(required = false, defaultValue = "", name = "sort") String sortBy
    ) {
        return userService.getAllUsers(sortBy);
    }

    @GetMapping("/{id}")
    public UserDto getUser(@PathVariable UUID id) {
        return userService.getUser(id);
    }

    @PostMapping
    public ResponseEntity<JwtResponse> registerUser(
            @Valid @RequestBody RegisterUserRequest request) {

        var userDto = userService.registerUser(request);
        
        // generate JWT token upon registration
        var user = userRepository.findById(userDto.getId()).orElseThrow();
        var accessToken = jwtService.generateAccessToken(user);
        
        return ResponseEntity.ok(new JwtResponse(accessToken.toString()));
    }

    @PutMapping("/{id}")
    public UserDto updateUser(
        @PathVariable(name = "id") UUID id,
        @RequestBody UpdateUserRequest request) {
        return userService.updateUser(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);
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

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Void> handleUserNotFound() {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Void> handleAccessDenied() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
