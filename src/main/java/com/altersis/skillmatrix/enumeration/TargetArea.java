package com.altersis.skillmatrix.enumeration;

import java.util.HashMap;
import java.util.Map;

public enum TargetArea {
    TRAINING("Training"),
    CDT("Client Delivery Target"),
    BD("Business Development"),
    SME("SME Task"),
    SF("Soft Skill"),
    Others("Others");

    private final String displayName;
    private static final Map<String, TargetArea> displayNameToValueMap = new HashMap<>();

    static {
        for (TargetArea area : TargetArea.values()) {
            displayNameToValueMap.put(area.displayName, area);
        }
    }

    TargetArea(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
