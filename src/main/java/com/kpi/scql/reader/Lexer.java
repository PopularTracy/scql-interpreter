package com.kpi.scql.reader;

import com.kpi.scql.exception.NotSupportedTokenException;
import com.kpi.scql.operation.OPERATION;
import com.kpi.scql.operation.OPERATION_TYPE_GROUP;
import com.kpi.scql.token.FUNCTIONAL_TOKEN_TYPE;
import com.kpi.scql.token.FunctionalToken;
import com.kpi.scql.token.OperationToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;

public class Lexer {

    private final BufferedReader reader;

    private final Set<String> reservedChars = Set.of(";", ",", "(", ")", "--", "'", "=", "!=", "<", ">", "<=", ">=");
    private boolean isEofReached = false;
    private String commandNormalized;

    private final Queue<String> tokenQueue = new ArrayDeque<>();

    public Lexer(BufferedReader reader) {
        this.reader = reader;
    }

    public OperationToken nextOperation() throws NotSupportedTokenException {

        String str;
        str = consume();

        if (str.equals("EOF")) {
            return null;
        }

        if (OPERATION.firstTokenNameStartsWith(str)) {
            String op = str + "_" + peek();
            if (OPERATION.findByName(op) != null) {
                str = op;
                consume();
            }
        }
        OPERATION op = OPERATION.findByName(str);

        if (op == null) {
            throw new NotSupportedTokenException("Token " + str + " is not supported!");
        }

        OPERATION_TYPE_GROUP ins = OPERATION_TYPE_GROUP.findGroup(op);

        return new OperationToken(ins, op);
    }

    public FunctionalToken nextAttribute() {
        String keyword = consume();
        switch (keyword.toUpperCase()) {
            case "'": return new FunctionalToken(FUNCTIONAL_TOKEN_TYPE.APOSTROPHE);
            case "(": return new FunctionalToken(FUNCTIONAL_TOKEN_TYPE.LBRACKET);
            case ")": return new FunctionalToken(FUNCTIONAL_TOKEN_TYPE.RBRACKET);
            case "*": return new FunctionalToken(FUNCTIONAL_TOKEN_TYPE.ASTERISK);
            case ",": return new FunctionalToken(FUNCTIONAL_TOKEN_TYPE.COMMA);
            case ";": return new FunctionalToken(FUNCTIONAL_TOKEN_TYPE.SEMICOLON);
            case "=": return new FunctionalToken(FUNCTIONAL_TOKEN_TYPE.EQUAL, keyword);
            case "!=": return new FunctionalToken(FUNCTIONAL_TOKEN_TYPE.NOT_EQUAL, keyword);
            case ">": return new FunctionalToken(FUNCTIONAL_TOKEN_TYPE.MORE_THAN, keyword);
            case "<": return new FunctionalToken(FUNCTIONAL_TOKEN_TYPE.LESS_THAN, keyword);
            case ">=": return new FunctionalToken(FUNCTIONAL_TOKEN_TYPE.EQUAL_MORE_THEN, keyword);
            case "<=": return new FunctionalToken(FUNCTIONAL_TOKEN_TYPE.EQUAL_LESS_THAN, keyword);
            case "AS": return new FunctionalToken(FUNCTIONAL_TOKEN_TYPE.AS);
            case "SELECT": return new FunctionalToken(FUNCTIONAL_TOKEN_TYPE.SELECT);
            case "FROM": return new FunctionalToken(FUNCTIONAL_TOKEN_TYPE.FROM);
            case "WHERE": return new FunctionalToken(FUNCTIONAL_TOKEN_TYPE.WHERE);
            case "AND": return new FunctionalToken(FUNCTIONAL_TOKEN_TYPE.AND);
            case "VALUES": return new FunctionalToken(FUNCTIONAL_TOKEN_TYPE.VALUES);
            case "FOR": return new FunctionalToken(FUNCTIONAL_TOKEN_TYPE.FOR);
            case "SET": return new FunctionalToken(FUNCTIONAL_TOKEN_TYPE.SET);
            case "EOF": return new FunctionalToken(FUNCTIONAL_TOKEN_TYPE.EOF);
            default: return new FunctionalToken(FUNCTIONAL_TOKEN_TYPE.ATTRIBUTE, keyword);
        }
    }

    private String consume()  {

        if (!tokenQueue.isEmpty()) {
            return tokenQueue.poll();
        }

        // Reading the lines until semicolon or EOF
        StringBuilder builder = new StringBuilder();
        try {
            String line;
            while ((line = reader.readLine()) != null) {

                // Skipping comments and other whitespaces
                line = line.replaceAll("--.*$", "").trim();

                if (line.startsWith("--") || line.isEmpty()) {
                    continue;
                }

                builder.append(line);

                if (line.endsWith(";")) {
                    break;
                }
            }

            // If reached to EOF closing reader
            if (line == null && builder.length() == 0) {
                isEofReached = true;
                return "EOF";
            } else if (line == null) {
                isEofReached = true;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Normalizing command and splitting it by the whitespaces
        String command = builder.toString();
        commandNormalized = command.replaceAll("\\s+", " ");
        String[] tokens = command.split("\\s+");

        // Split command splitting by each unique token, like name, reserved keywords etc
        for (String token : tokens) {
            if (reservedChars.contains(token)) {
                tokenQueue.add(token);
                continue;
            }

            // Creating builder with previous values
            StringBuilder previous = new StringBuilder();
            boolean isPrevious = false;
            for (char c : token.toCharArray()) {
                String symbol = Character.toString(c);

                // If this symbol and previous are making a reserved keyword
                // adding it to the queue of tokens
                if (isPrevious) {
                    // If it's a pair of reserved symbols <=, !=, >=
                    // adding it to the String builder
                    if (reservedChars.contains(previous + symbol)) {
                        previous.append(symbol);
                    }

                    // Adding symbol to the queue and clearing String builder
                    tokenQueue.add(previous.toString());
                    previous.delete(0, previous.length());
                    isPrevious = false;
                }

                // Checking the reserved keyword and adding it to queue
                if (reservedChars.contains(symbol)) {

                    // If String builder is not empty, that means
                    // previous reserved key is not a pair of keyword
                    // adding it to the queue and clearing the string builder
                    if (previous.length() != 0) {
                        tokenQueue.add(previous.toString());
                        previous.delete(0, previous.length());
                    }

                    previous.append(symbol);
                    isPrevious = true;
                } else {
                    previous.append(symbol);
                }
            }

            tokenQueue.add(previous.toString());
        }

        if (isEofReached) {
            tokenQueue.add("EOF");
        }

        return tokenQueue.poll();
    }

    public String peek() {
        return tokenQueue.peek();
    }

    public boolean hasNext() {
        return !isEofReached;
    }

    public String getCommandNormalized() {
        return commandNormalized;
    }
}