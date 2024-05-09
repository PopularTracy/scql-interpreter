package com.kpi.scql.generator.decorator.writer;

import com.sun.javacard.apduio.Apdu;

import java.io.IOException;

public interface ApduWriteDecorator {
    void decorate(Apdu apdu) throws IOException;
}
