package com.kpi.scql.generator.decorator.apdu;

import com.kpi.scql.apdu.ApduAst;
import com.kpi.scql.operation.OPERATION_TYPE_GROUP;
import com.sun.javacard.apduio.Apdu;

public class TransactionalApduDecorator extends BaseApduDecorator {

    @Override
    public Apdu generateApduCommand(ApduAst apduAst) {

        if (apduAst == null) {
            throw new NullPointerException("APDU AST cannot be null!");
        }

        if (apduAst.getIns() != OPERATION_TYPE_GROUP.TRANSACTION) {
            throw new IllegalArgumentException("INS operation is not Transactional creation type. Expected"
                    + OPERATION_TYPE_GROUP.TRANSACTION
                    + ", found " + apduAst.getIns());
        }

        byte[] data = null;
        switch (apduAst.getP2()) {
            case BEGIN:
            case COMMIT:
            case ROLLBACK:
                break;
            default:
                throw new IllegalArgumentException("P2 operation "
                        + apduAst.getP2() + " not supported with "
                        + OPERATION_TYPE_GROUP.TRANSACTION
                );
        }

        return super.generateApduCommand(apduAst);
    }
}
