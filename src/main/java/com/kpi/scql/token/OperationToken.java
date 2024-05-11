package com.kpi.scql.token;

import com.kpi.scql.enums.OPERATION;
import com.kpi.scql.enums.OPERATION_GROUP;

public class OperationToken {

    private OPERATION_GROUP INS;
    private OPERATION operation;

    public OperationToken() {
    }

    public OperationToken(OPERATION_GROUP INS, OPERATION operation) {
        this.INS = INS;
        this.operation = operation;
    }

    public OPERATION getOperation() {
        return operation;
    }

    public void setOperation(OPERATION operation) {
        this.operation = operation;
    }

    public OPERATION_GROUP getINS() {
        return INS;
    }

    public void setINS(OPERATION_GROUP INS) {
        this.INS = INS;
    }
}
