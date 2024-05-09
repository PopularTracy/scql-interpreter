package com.kpi.scql.token;

import com.kpi.scql.operation.OPERATION;
import com.kpi.scql.operation.OPERATION_TYPE_GROUP;

public class OperationToken {

    private OPERATION_TYPE_GROUP INS;
    private OPERATION operation;

    public OperationToken() {
    }

    public OperationToken(OPERATION_TYPE_GROUP INS, OPERATION operation) {
        this.INS = INS;
        this.operation = operation;
    }

    public OPERATION getOperation() {
        return operation;
    }

    public void setOperation(OPERATION operation) {
        this.operation = operation;
    }

    public OPERATION_TYPE_GROUP getINS() {
        return INS;
    }

    public void setINS(OPERATION_TYPE_GROUP INS) {
        this.INS = INS;
    }
}
