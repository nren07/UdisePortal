package com.udise.portal.enums;

public enum JobStatus {
    PENDING("Pending"),
    COMPLETED("Completed"),
    IN_PROGRESS("InProgress"),
    ALREADY_COMPLETED("Already_Completed");


    private final String displayName;

    // Constructor to set custom string representation
    JobStatus(String displayName) {
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
