package com.kpi.scql.generator.decorator.writer;

import com.sun.javacard.apduio.Apdu;

import java.io.IOException;
import java.io.Writer;

public class CommandWriteDecorator implements ApduWriteDecorator {

    private final String hexFormat = "0x%02x";
    private final Writer writer;

    public CommandWriteDecorator(Writer writer) {
        this.writer = writer;
    }

    @Override
    public void decorate(Apdu apdu) throws IOException {

        if (apdu == null) {
            return;
        }

        int dataLength = 0;

        if (apdu.dataIn != null) {
            dataLength = apdu.dataIn.length;
        }

        int totalLength = apdu.lc + apdu.le + apdu.command.length + dataLength;

        writer.write("// Command total length in bytes: " + totalLength + "\n");

        writer.write("// ");
        writer.write(apdu.toString());
        writer.write("\n");

        for (byte header : apdu.getCommand()) {
            writer.write(String.format(hexFormat, header));
            writer.write(" ");
        }

        writer.write(String.format(hexFormat, apdu.getLc()));
        writer.write(" ");

        if (apdu.getDataIn() != null) {
            for (byte data : apdu.getDataIn()) {
                writer.write(String.format(hexFormat, data));
                writer.write(" ");
            }
        }

        writer.write(String.format(hexFormat, apdu.getLe()));
        writer.write(";\n");
    }
}
