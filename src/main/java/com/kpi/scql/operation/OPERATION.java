package com.kpi.scql.operation;

import java.util.Arrays;

public enum OPERATION {

    // SCQL
    CREATE_TABLE(0x80),
    CREATE_VIEW(0x81),
    CREATE_DICTIONARY(0x82),
    DROP_TABLE(0x83),
    DROP_VIEW(0x84),
    GRANT(0x85),
    REVOKE(0x86),
    DECLARE_CURSOR(0x87),
    OPEN(0x88),
    NEXT(0x89),
    FETCH(0x8A),
    FETCH_NEXT(0x8B),
    INSERT_INTO(0x8C),
    UPDATE(0x8D),
    DELETE(0x8E),

    // Transaction
    BEGIN(0x80),
    COMMIT(0x81),
    ROLLBACK(0x82),

    // User
    PRESENT_USER(0x80),
    CREATE_USER(0x81),
    DELETE_USER(0x82);

    private final int operationP2;

    OPERATION(int operationP2) {
        this.operationP2 = operationP2;
    }

    public static OPERATION findByName(String name) {
        String normalizedName = name.toUpperCase();

        return Arrays.stream(values())
                .filter(op -> op.name().equals(normalizedName))
                .findFirst()
                .orElse(null);
    }

    public static boolean firstTokenNameStartsWith(String name) {
        String normalizedName = name.toUpperCase();
        return Arrays.stream(values())
                .anyMatch(op -> op.name().startsWith(normalizedName + "_"));
    }

    public int getOperationP2() {
        return operationP2;
    }

    @Override
    public String toString() {
        return super.toString().replaceAll("_", " ");
    }
}
