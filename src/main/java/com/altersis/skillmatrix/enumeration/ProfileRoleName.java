package com.altersis.skillmatrix.enumeration;

public enum ProfileRoleName {
    JPE("JPE - Junior Performance Engineer"),
    AE("AE - APM Engineer"),
    PE("PE - Performance Engineer"),
    PC("PC - Performance Consultant"),
    BA("BA - Business Administrator/PMO"),
    SAE("SAE - Senior APM Engineer"),
    SPE("SPE - Senior Performance Engineer"),
    SPC("SPC - Senior Performance Consultant"),
    SBA("SBA - Senior Business Administrator/PMO"),
    PA("PA - Performance Architect"),
    PPE("PPE - Principal Performance Engineer"),
    PPC("PPC - Principal Performance Consultant"),
    EM("EM - Engagement Manager"),
    MC("MC - Managing Consultant");
    private final String displayName;

    ProfileRoleName(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
