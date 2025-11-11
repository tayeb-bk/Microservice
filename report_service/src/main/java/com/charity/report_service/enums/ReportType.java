package com.charity.report_service.enums;

public enum ReportType {
    DONATION("Donation Report"),
    EVENT("Event Report"),
    CAMPAIGN("Campaign Report"),
    FORMATION("Formation Report"),
    BLOG("Blog Report"),
    NOTIFICATION("Notification Report"),
    GENERAL("General Report");

    private final String displayName;

    ReportType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}