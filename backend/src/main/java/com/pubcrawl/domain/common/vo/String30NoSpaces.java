package com.pubcrawl.domain.common.vo;

import java.util.Objects;

public class String30NoSpaces {

    private final String value;

    public String30NoSpaces(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Value cannot be null or blank");
        }

        if (value.length() > 30) {
            throw new IllegalArgumentException("Value must be at most 30 characters");
        }

        if (!value.matches("^[a-zA-Z0-9_]+$")) {
            throw new IllegalArgumentException("Only letters, digits, and underscores are allowed");
        }

        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof String30NoSpaces other)) return false;
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

