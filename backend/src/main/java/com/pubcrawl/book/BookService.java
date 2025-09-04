package com.pubcrawl.book;

import com.pubcrawl.group.GroupMemberRepository;
import com.pubcrawl.user.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Service
public class BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final S3Service s3Service;
    private final GroupMemberRepository groupMemberRepository;
    private final S3Properties s3Properties;

    public List<BookDto> getBooksByGroup(UUID groupId) {
        var books = bookRepository.findByGroupId(groupId);
        return books.stream()
                .map(bookMapper::toDto)
                .toList();
    }

    public BookDto getBook(UUID groupId, UUID bookId) {
        var book = bookRepository.findById(bookId)
                .orElseThrow(BookNotFoundException::new);
        
        // verify book belongs to the group 
        if (!book.getGroup().getId().equals(groupId)) {
            throw new BookNotFoundException();
        }
        
        return bookMapper.toDto(book);
    }

    public void deleteBook(UUID groupId, UUID bookId, User user) {
        var book = bookRepository.findById(bookId)
                .orElseThrow(BookNotFoundException::new);
        
        // verify book belongs to the group
        if (!book.getGroup().getId().equals(groupId)) {
            throw new BookNotFoundException();
        }
        
        // verify user is a member of the group
        var groupMember = groupMemberRepository.findByGroupIdAndUserId(groupId, user.getId())
                .orElseThrow(() -> new IllegalArgumentException("User is not a member of this group"));
        
        // only group admins can delete books
        if (groupMember.getRole() != com.pubcrawl.group.MemberRole.ADMIN) {
            throw new UnauthorizedBookOperationException();
        }
        
        // delete from S3 if storage key exists
        if (book.getStorageKey() != null) {
            try {
                deleteFileFromS3(book.getStorageKey());
            } catch (Exception e) {
                // Log error but continue with database deletion
                System.err.println("Failed to delete file from S3: " + e.getMessage());
            }
        }
        
        // delete from database
        bookRepository.delete(book);
    }
    
    private void deleteFileFromS3(String storageKey) throws IOException {
        // Create S3 client
        var s3Client = software.amazon.awssdk.services.s3.S3Client.builder()
                .region(software.amazon.awssdk.regions.Region.of(s3Properties.getRegion()))
                .credentialsProvider(software.amazon.awssdk.auth.credentials.StaticCredentialsProvider.create(
                        software.amazon.awssdk.auth.credentials.AwsBasicCredentials.create(
                                s3Properties.getAccessKeyId(),
                                s3Properties.getSecretAccessKey()
                        )
                ))
                .build();

        // Delete file from S3
        var deleteObjectRequest = software.amazon.awssdk.services.s3.model.DeleteObjectRequest.builder()
                .bucket(s3Properties.getBucketName())
                .key(storageKey)
                .build();

        s3Client.deleteObject(deleteObjectRequest);
    }

    public BookDto uploadBook(UUID groupId, String title, MultipartFile file, User user) {
        // verify user is a member of the group
        var groupMember = groupMemberRepository.findByGroupIdAndUserId(groupId, user.getId())
                .orElseThrow(() -> new IllegalArgumentException("User is not a member of this group"));
        
        // validate file type
        if (!isValidEpubFile(file)) {
            throw new InvalidFileTypeException();
        }
        
        try {
            // upload to S3
            String storageKey = s3Service.uploadFile(file, file.getOriginalFilename(), groupId);
            
            // create book entity
            var book = Book.builder()
                    .title(title)
                    .storageKey(storageKey)
                    .size(file.getSize())
                    .status(BookStatus.READY)
                    .createdAt(LocalDateTime.now())
                    .build();
            
            // set group (we'll need to fetch it)
            var group = groupMember.getGroup();
            book.setGroup(group);
            
            // save to database
            bookRepository.save(book);
            
            return bookMapper.toDto(book);
            
        } catch (IOException e) {
            // create book with FAILED status
            var book = Book.builder()
                    .title(title)
                    .storageKey(null)
                    .size(file.getSize())
                    .status(BookStatus.FAILED)
                    .createdAt(LocalDateTime.now())
                    .build();
            
            var group = groupMember.getGroup();
            book.setGroup(group);
            
            bookRepository.save(book);
            
            throw new RuntimeException("Failed to upload file to S3", e);
        }
    }
    
    private boolean isValidEpubFile(MultipartFile file) {
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null) {
            return false;
        }
        
        String extension = originalFilename.toLowerCase();
        return extension.endsWith(".epub");
    }
}
