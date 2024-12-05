package com.udise.portal.enums;

public enum Role {
    CLIENT("Client"),
    USER("User"),
    SUPER_ADMIN("Super_Admin");

    private final String displayName;

    // Constructor to set custom string representation
    Role(String displayName) {
        this.displayName = displayName;
    }

    // Method to get the custom string representation
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName; // Return custom string representation when using toString()
    }
}
