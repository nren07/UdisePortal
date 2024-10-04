package com.udise.portal.enums;

public enum ExecutionStatus {
    CREATED("CREATED"), FILE_UPLOADED("FILE_UPLOADED"),DATA_UPLOADED("DATA_UPLOADED"), SUCCESS("SUCCESS"), FAILED("FAILED"), IN_PROGRESS("IN_PROGRESS");

    private String type;

    public String getType() {
        return type;
    }

    private ExecutionStatus(String type) {
        this.type = type;
    }

    public static ExecutionStatus getStatus(String type) {
        for (ExecutionStatus status : ExecutionStatus.values()) {
            if (status.type.equalsIgnoreCase(type)) {
                return status;
            }
        }
        return null;
    }

}
