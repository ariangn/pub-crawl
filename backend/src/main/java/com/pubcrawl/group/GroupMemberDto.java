package com.pubcrawl.group;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@Getter
public class GroupMemberDto {
    private UUID id;
    private UUID groupId;
    private UUID userId;
    private String username;
    private String email;
    private String pfpUrl;
    private MemberRole role;
    private String highlightColor;
    private LocalDateTime joinedAt;
}
