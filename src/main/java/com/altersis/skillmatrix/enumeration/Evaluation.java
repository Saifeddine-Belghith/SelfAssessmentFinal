package com.altersis.skillmatrix.enumeration;

public enum Evaluation {
    EXCELLENT("Excellent"),
    GOOD("Good"),
    APPROPRIATE("Appropriate"),
    UNDER_EXPECTATION("Under Expectation"),
    POOR("Poor"),
    NOT_APPLICABLE("Not Applicable");

    private String displayName;

    Evaluation(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}

