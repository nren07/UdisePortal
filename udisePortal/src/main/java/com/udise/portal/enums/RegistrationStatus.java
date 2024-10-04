package com.udise.portal.enums;

public enum RegistrationStatus {

    REQUESTED("Requested"), APPROVED("Approved"), REJECTED("Rejected");

    private String label;

    RegistrationStatus(String label) {
        this.label = label;
    }

    public String label() {
        return this.label;
    }
}
