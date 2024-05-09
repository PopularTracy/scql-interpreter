package com.kpi.scql.generator.decorator.apdu;

import com.kpi.scql.apdu.ApduAst;
import com.sun.javacard.apduio.Apdu;

public abstract class BaseApduDecorator implements ApduDecorator {

    @Override
    public Apdu generateApduCommand(ApduAst apduAst) {

        if (apduAst == null) {
            throw new NullPointerException("APDU AST object cannot be null!");
        }

        Apdu apdu = new Apdu();
        apdu.command = new byte[] {0x00, (byte) apduAst.getIns().getInsCode(), 0x00,(byte) apduAst.getP2().getOperationP2()};

        return apdu;
    }
}
