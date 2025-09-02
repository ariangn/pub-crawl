package com.pubcrawl.group;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CreateGroupRequest {
    @NotBlank(message = "Group name is required")
    @Size(min = 1, max = 255, message = "Group name must be between 1 and 255 characters")
    private String name;

    private String pfpUrl;
}
