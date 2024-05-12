package com.kpi.scql.generator;

import com.kpi.scql.apdu.ApduContainer;
import com.kpi.scql.apdu.ApduAst;
import com.kpi.scql.exception.NotSupportedApduException;
import com.kpi.scql.generator.decorator.apdu.BaseApduDecorator;
import com.kpi.scql.generator.decorator.apdu.ScqlApduDecorator;
import com.kpi.scql.generator.decorator.apdu.TransactionalApduDecorator;
import com.kpi.scql.generator.decorator.writer.CommandWriteDecorator;
import com.kpi.scql.generator.decorator.writer.CommentWriteDecorator;
import com.sun.javacard.apduio.Apdu;

import java.io.IOException;
import java.io.Writer;

public class Generator {

    private final Writer writer;
    private final CommentWriteDecorator commentDecorator;
    private final CommandWriteDecorator commandDecorator;

    public Generator(Writer writer) {
        this.writer = writer;
        this.commentDecorator = new CommentWriteDecorator(writer);
        this.commandDecorator = new CommandWriteDecorator(writer);
    }

    // CLA INS P1 P2 Lc N Lp data
    public ApduContainer generateNextAPDU(ApduAst apduAst) throws NotSupportedApduException, IOException {

        BaseApduDecorator apduDecorator;
        switch (apduAst.getIns()) {
            case SCQL:
                apduDecorator = new ScqlApduDecorator();
                break;
            case TRANSACTION:
                apduDecorator = new TransactionalApduDecorator();
                break;
            default: throw new NotSupportedApduException("Not supported generation functionality for INS " + apduAst.getIns());
        }

        Apdu apdu = apduDecorator.generateApduCommand(apduAst);

        try {
            commentDecorator.decorate(apduAst);
            commandDecorator.decorate(apdu);

            writer.write("\n");
        } catch (IOException e) {
            System.out.println("An error writing commands");
        }

        return new ApduContainer(apdu, apduAst.getScqlCommand());
    }
}
