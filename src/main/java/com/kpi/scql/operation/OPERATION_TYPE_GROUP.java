package com.kpi.scql.operation;

import java.util.Arrays;

public enum OPERATION_TYPE_GROUP {

    SCQL(0x10, new OPERATION[]{
            OPERATION.CREATE_TABLE,
            OPERATION.CREATE_VIEW,
            OPERATION.CREATE_DICTIONARY,
            OPERATION.DROP_TABLE,
            OPERATION.DROP_VIEW,
            OPERATION.GRANT,
            OPERATION.REVOKE,
            OPERATION.DECLARE_CURSOR,
            OPERATION.OPEN,
            OPERATION.NEXT,
            OPERATION.FETCH,
            OPERATION.FETCH_NEXT,
            OPERATION.INSERT_INTO,
            OPERATION.UPDATE,
            OPERATION.DELETE
    }),

    TRANSACTION(0x12, new OPERATION[]{
            OPERATION.BEGIN,
            OPERATION.COMMIT,
            OPERATION.ROLLBACK
    }),

    USER(0x14, new OPERATION[]{
            OPERATION.PRESENT_USER,
            OPERATION.CREATE_USER,
            OPERATION.DELETE_USER
    });

    private final int insCode;
    private final OPERATION[] operations;

    OPERATION_TYPE_GROUP(int insCode, OPERATION[] operations) {
        this.insCode = insCode;
        this.operations = operations;
    }

    public static OPERATION_TYPE_GROUP findGroup(OPERATION operation) {
        return Arrays.stream(values())
                .filter(opg -> Arrays.asList(opg.operations).contains(operation))
                .findFirst()
                .orElse(null);
    }

    public int getInsCode() {
        return insCode;
    }

    public OPERATION[] getOperations() {
        return operations;
    }

    @Override
    public String toString() {
        return super.toString() + ":";
    }
}
