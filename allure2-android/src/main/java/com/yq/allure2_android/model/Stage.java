
package com.yq.allure2_android.model;

public enum Stage {

    SCHEDULED("scheduled"),
    RUNNING("running"),
    FINISHED("finished"),
    PENDING("pending"),
    INTERRUPTED("interrupted");
    private final String value;

    Stage(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static Stage fromValue(String v) {
        for (Stage c: Stage.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
