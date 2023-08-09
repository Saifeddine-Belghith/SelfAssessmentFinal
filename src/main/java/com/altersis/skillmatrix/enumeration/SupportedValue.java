package com.altersis.skillmatrix.enumeration;

import java.util.HashMap;
import java.util.Map;

public enum SupportedValue{
    CL("CL - Continuous Learning"),
    FEEDBACK("Feedback"),
    TRUST("Trust"),
    HONESTY("Honesty"),
    OPC("OPC - Openness Towards Change"),
    PPDI("PPDI - Passion & Process Driven Innovation"),
    PCC("PCC - Proactive & Conscious Collaboration"),
    SOLIDARITY("Solidarity");
    private final String displayName;
    private static final Map<String, SupportedValue> displayNameToValueMap = new HashMap<>();

    static {
        for (SupportedValue supportedValue : SupportedValue.values()) {
            displayNameToValueMap.put(supportedValue.displayName, supportedValue);
        }
    }

    SupportedValue(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
