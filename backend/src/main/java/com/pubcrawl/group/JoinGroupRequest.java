package com.pubcrawl.group;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class JoinGroupRequest {
    @NotBlank(message = "Invite code is required")
    private String inviteCode;
    
    @Pattern(regexp = "^#[0-9A-Fa-f]{6}$", message = "Highlight color must be a valid hex color (e.g., #FF6B6B)")
    private String highlightColor;
}
