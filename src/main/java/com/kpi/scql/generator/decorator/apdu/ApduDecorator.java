package com.kpi.scql.generator.decorator.apdu;

import com.kpi.scql.apdu.ApduAst;
import com.sun.javacard.apduio.Apdu;

public interface ApduDecorator {
    Apdu generateApduCommand(ApduAst apduAst);
}
