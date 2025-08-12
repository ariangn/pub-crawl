package com.pubcrawl.domain.common.vo;

import java.util.Objects;

public class String30 {
    private final String value;

    public String30(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Value cannot be null or blank");
        }

        if (value.length() > 30) {
            throw new IllegalArgumentException("Value must be at most 30 characters");
        }

        if (!value.matches("^[a-zA-Z0-9_ ]+$")) {
            throw new IllegalArgumentException("Only letters, digits, underscores, and spaces are allowed");
        }

        this.value = value;
    }

    public String getValue() {
        return value;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof String30 other)) return false;
        return value.equals(other.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
