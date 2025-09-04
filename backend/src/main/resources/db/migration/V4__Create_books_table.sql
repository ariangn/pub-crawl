-- Create books table
CREATE TABLE books (
    id BINARY(16) PRIMARY KEY,
    group_id BINARY(16) NOT NULL,
    title VARCHAR(255) NOT NULL,
    storage_key VARCHAR(500),
    size BIGINT,
    status ENUM('UPLOADED', 'READY', 'FAILED') NOT NULL,
    created_at DATETIME(6) NOT NULL,
    CONSTRAINT fk_books_group FOREIGN KEY (group_id) REFERENCES `groups`(id) ON DELETE CASCADE
);

-- Create indexes for better performance
CREATE INDEX idx_books_group_id ON books(group_id);
CREATE INDEX idx_books_status ON books(status);
CREATE INDEX idx_books_created_at ON books(created_at);
