package com.udise.portal.enums;

public enum JobType {
    SERVICE1("SERVICE1"),
    SERVICE2("SERVICE2"),
    SERVICE3("SERVICE3");

    private final String displayName;

    // Constructor to set custom string representation
    JobType(String displayName) {
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
