package com.pubcrawl.book;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@Getter
public class UploadBookRequest {
    @NotBlank(message = "Title is required")
    private String title;
    
    private MultipartFile file;
}
