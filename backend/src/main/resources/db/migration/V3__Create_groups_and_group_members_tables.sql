-- Create groups table
CREATE TABLE `groups` (
    id BINARY(16) PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    pfp_url VARCHAR(255),
    owner_id BINARY(16) NOT NULL,
    created_at DATETIME(6) NOT NULL,
    invite_code VARCHAR(8) NOT NULL UNIQUE,
    CONSTRAINT fk_groups_owner FOREIGN KEY (owner_id) REFERENCES users(id)
);

-- Create group_members table
CREATE TABLE group_members (
    id BINARY(16) PRIMARY KEY,
    group_id BINARY(16) NOT NULL,
    user_id BINARY(16) NOT NULL,
    role ENUM('ADMIN', 'MEMBER') NOT NULL,
    highlight_color VARCHAR(7),
    joined_at DATETIME(6) NOT NULL,
    CONSTRAINT fk_group_members_group FOREIGN KEY (group_id) REFERENCES `groups`(id) ON DELETE CASCADE,
    CONSTRAINT fk_group_members_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT uk_group_members_user UNIQUE (group_id, user_id)
);

-- Create indexes for better performance
CREATE INDEX idx_groups_owner_id ON `groups`(owner_id);
CREATE INDEX idx_groups_invite_code ON `groups`(invite_code);
CREATE INDEX idx_group_members_group_id ON group_members(group_id);
CREATE INDEX idx_group_members_user_id ON group_members(user_id);
