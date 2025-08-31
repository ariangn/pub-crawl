package com.pubcrawl.user;

import lombok.Data;

@Data
public class UpdateUserRequest {
    public String username;
    public String pfpUrl;
    public String email;
}