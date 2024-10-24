package com.udise.portal.enums;

public enum ExecutionStatus {
    CREATED("CREATED"), FILE_UPLOADED("FILE_UPLOADED"),DATA_UPLOADED("DATA_UPLOADED"), SUCCESS("SUCCESS"), FAILED("FAILED"), IN_PROGRESS("IN_PROGRESS");

    private final String displayName;

    // Constructor to set custom string representation
    ExecutionStatus(String displayName) {
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
