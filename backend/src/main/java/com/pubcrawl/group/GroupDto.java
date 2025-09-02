package com.pubcrawl.group;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@Getter
public class GroupDto {
    private UUID id;
    private String name;
    private String pfpUrl;
    private UUID ownerId;
    private LocalDateTime createdAt;
    private String inviteCode;
}
