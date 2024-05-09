package com.kpi.scql.generator.decorator.writer;

import com.kpi.scql.apdu.ApduAst;

import java.io.IOException;
import java.io.Writer;

public class CommentWriteDecorator {

    private final Writer writer;

    public CommentWriteDecorator(Writer writer) {
        this.writer = writer;
    }

    public void decorate(ApduAst apduAst) throws IOException {
        if (apduAst == null) {
            return;
        }

        writer.write("// ");
        writer.write(apduAst.getScqlCommand());
        writer.write("\n");
    }
}
