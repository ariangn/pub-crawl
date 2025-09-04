package com.pubcrawl.book;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@Getter
public class BookDto {
    private UUID id;
    private UUID groupId;
    private String title;
    private String storageKey;
    private Long size;
    private BookStatus status;
    private LocalDateTime createdAt;
}
