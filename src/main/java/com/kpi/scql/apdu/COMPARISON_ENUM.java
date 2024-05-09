package com.kpi.scql.apdu;

import java.util.Arrays;

public enum COMPARISON_ENUM {

    EQUAL("=", 0x3d),
    LESS_THAN("<",0x3c),
    MORE_THAN(">", 0x3e),
    EQUAL_LESS_THAN("<=", 0x4c),
    EQUAL_MORE_THEN(">=", 0x47),
    NOT_EQUAL("!=", 0x23);

    private final String value;
    private final int code;

    COMPARISON_ENUM(String value, int code) {
        this.value = value;
        this.code = code;
    }

    public static int findCodeByValue(String name) {
        return Arrays
                .stream(values())
                .filter(c -> c.value.equals(name))
                .findFirst()
                .get()
                .code;
    }

    public String getValue() {
        return value;
    }

    public int getCode() {
        return code;
    }
}
