package com.pubcrawl.book;

import com.pubcrawl.auth.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/groups/{groupId}/books")
public class BookController {
    private final BookService bookService;
    private final AuthService authService;

    @GetMapping
    public List<BookDto> getBooksByGroup(@PathVariable UUID groupId) {
        return bookService.getBooksByGroup(groupId);
    }

    @PostMapping
    public ResponseEntity<?> uploadBook(
            @PathVariable UUID groupId,
            @RequestParam("title") String title,
            @RequestParam("file") MultipartFile file,
            UriComponentsBuilder uriBuilder) {
        
        var currentUser = authService.getCurrentUser();
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        var bookDto = bookService.uploadBook(groupId, title, file, currentUser);
        var uri = uriBuilder.path("/groups/{groupId}/books/{bookId}")
                .buildAndExpand(groupId, bookDto.getId()).toUri();
        return ResponseEntity.created(uri).body(bookDto);
    }

    @GetMapping("/{bookId}")
    public BookDto getBook(@PathVariable UUID groupId, @PathVariable UUID bookId) {
        return bookService.getBook(groupId, bookId);
    }

    @DeleteMapping("/{bookId}")
    public ResponseEntity<Void> deleteBook(@PathVariable UUID groupId, @PathVariable UUID bookId) {
        var currentUser = authService.getCurrentUser();
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        bookService.deleteBook(groupId, bookId, currentUser);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity<Void> handleBookNotFound() {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(InvalidFileTypeException.class)
    public ResponseEntity<Map<String, String>> handleInvalidFileType() {
        return ResponseEntity.badRequest().body(
            Map.of("error", "Only .epub files are allowed.")
        );
    }

    @ExceptionHandler(UnauthorizedBookOperationException.class)
    public ResponseEntity<Map<String, String>> handleUnauthorizedBookOperation() {
        return ResponseEntity.badRequest().body(
            Map.of("error", "Only group admins can delete books.")
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(
            Map.of("error", ex.getMessage())
        );
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Void> handleAccessDenied() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
