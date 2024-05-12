package com.kpi.scql;

import com.kpi.scql.apdu.ApduAst;
import com.kpi.scql.apdu.ApduContainer;
import com.kpi.scql.exception.MatchTokenException;
import com.kpi.scql.exception.NotSupportedApduException;
import com.kpi.scql.exception.NotSupportedTokenException;
import com.kpi.scql.generator.Generator;
import com.kpi.scql.parser.Parser;
import com.kpi.scql.reader.Lexer;
import com.kpi.scql.socket.SmartCardConnector;
import com.sun.javacard.apduio.CadTransportException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {

        String path = new File(".").getCanonicalPath() + "/scql/src/main/resources/";

        File scqlFile = new File(path + "command.scql");

        if (!scqlFile.exists()) {
            throw new RuntimeException("Controller file does not exist!");
        }

        String name = scqlFile.getName().replaceAll("\\.scql", "_");

        File out = new File(path + name + "apdu.script");
        if (out.createNewFile()) {
            System.out.println("File " + out.getName() + " was created!");
        }

        List<ApduContainer> apdus = new ArrayList<>();

        // Reading, parsing and generating APDUs from SCQL
        try (BufferedReader bf = new BufferedReader(new FileReader(scqlFile));
             BufferedWriter bw = new BufferedWriter(new FileWriter(out))
        ) {
            Lexer lexer = new Lexer(bf);
            Parser parser = new Parser(lexer);
            Generator generator = new Generator(bw);

            while (lexer.hasNext()) {
                ApduAst apduAST = parser.parseNext();

                if (apduAST != null) {
                    System.out.println(apduAST);
                    ApduContainer apdu = generator.generateNextAPDU(apduAST);
                    apdus.add(apdu);
                }
            }

        } catch (IOException | MatchTokenException | NotSupportedTokenException | NotSupportedApduException e) {
            e.printStackTrace();
        }

        System.out.println();
        System.out.println();
        System.out.println();

        // Connecting to the smart card via java CAD
        try(SmartCardConnector connector = new SmartCardConnector()) {
            connector.openAndSelect();

            for (ApduContainer container : apdus) {
                System.out.println(container.getScqlCommand());
                connector.exchangeApdu(container.getApdu());
                System.out.println();
                System.out.println();
            }

            connector.powerDown();
        } catch (CadTransportException e) {
            System.out.println("Error with Cad" + e.getMessage());
            e.printStackTrace();
        }
    }
}
