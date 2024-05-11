package com.kpi.scql;

import com.kpi.scql.enums.FIELD_TYPE;
import com.kpi.scql.apdu.ApduAst;
import com.kpi.scql.exception.MatchTokenException;
import com.kpi.scql.exception.NotSupportedTokenException;
import com.kpi.scql.reader.Lexer;
import com.kpi.scql.enums.FUNCTIONAL_TYPE;
import com.kpi.scql.token.FunctionalToken;
import com.kpi.scql.token.OperationToken;

import java.util.function.Consumer;

public class Parser {

    private final Lexer lexer;
    private ApduAst apduAST;

    public Parser(Lexer lexer) {
        this.lexer = lexer;
    }

    public ApduAst parseNext() throws NotSupportedTokenException, MatchTokenException {
        apduAST = new ApduAst();
        OperationToken operationToken = lexer.nextOperation();

        apduAST.setScqlCommand(lexer.getCommandNormalized());

        if (operationToken == null) {
            return null;
        }

        switch (operationToken.getOperation()) {
            case CREATE_TABLE:
                createTable(operationToken);
                break;
            case CREATE_VIEW:
                createView(operationToken);
                break;
            case INSERT_INTO:
                insertInto(operationToken);
                break;
            case DECLARE_CURSOR:
                declareCursor(operationToken);
                break;
            case OPEN:
                open(operationToken);
                break;
            case NEXT:
                nextOperation(operationToken);
                break;
            case FETCH:
                fetch(operationToken);
                break;
            case FETCH_NEXT:
                fetchNext(operationToken);
                break;
            case DROP_TABLE:
                dropTable(operationToken);
                break;
            case DROP_VIEW:
                dropView(operationToken);
                break;
            case DELETE:
                delete(operationToken);
                break;
            case UPDATE:
                update(operationToken);
                break;
            case BEGIN:
                begin(operationToken);
                break;
            case COMMIT:
                commit(operationToken);
                break;
            case ROLLBACK:
                rollback(operationToken);
                break;
            default: throw new NotSupportedTokenException("Token " + operationToken.getOperation().name() + " is not supported!");
        }

        return apduAST;
    }

    private FunctionalToken next() {
        return lexer.nextAttribute();
    }

    /**
     * CREATE TABLE name ('column1', 'column2', 'columnN')
     * @param operationToken
     */
    private void createTable(OperationToken operationToken) throws MatchTokenException {

        // CREATE TABLE
        apduAST.addOperation(operationToken);

        // name
        consumeAttribute(apduAST::addTable);

        // (
        matchTokenStrong(FUNCTIONAL_TYPE.LBRACKET);

        FunctionalToken token = consumeToken(apduAST::addColumn);

        // )
        matchTokenStrong(token, FUNCTIONAL_TYPE.RBRACKET);
        // ; - end of
        matchTokenStrong(FUNCTIONAL_TYPE.SEMICOLON);
    }

    /**
     * CREATE VIEW name AS SELECT * | ('column_name' [, 'column_name'])
     * FROM table_name [WHERE column_name comparison 'string' [AND column_name comparison 'string']];
     * @param operationToken
     * @throws MatchTokenException
     */
    public void createView(OperationToken operationToken) throws MatchTokenException {

        // CREATE VIEW
        apduAST.addOperation(operationToken);

        // name
        consumeAttribute(apduAST::addView);

        matchTokenStrong(FUNCTIONAL_TYPE.AS);

        // SELECT
        FunctionalToken clause = select();

        // ; - end of
        matchTokenStrong(clause, FUNCTIONAL_TYPE.SEMICOLON);
    }

    /**
     *  INSERT INTO table_name VALUES ('column' [, 'column']);
     * @param operationToken
     */
    public void insertInto(OperationToken operationToken) throws MatchTokenException {

        // INSERT INTO
        apduAST.addOperation(operationToken);

        // table_name
        consumeAttribute(apduAST::addTable);

        matchTokenStrong(FUNCTIONAL_TYPE.VALUES);
        matchTokenStrong(FUNCTIONAL_TYPE.LBRACKET);

        FunctionalToken nextOperand = consumeToken(apduAST::addData);
        matchTokenStrong(nextOperand, FUNCTIONAL_TYPE.RBRACKET);
        matchTokenStrong(FUNCTIONAL_TYPE.SEMICOLON);
    }

    /**
     * DECLARE CURSOR FOR SELECT * | 'column' [, 'column']
     *  [WHERE column_name comparison 'string' [AND column_name comparison 'string']];
     * @param operationToken
     */
    private void declareCursor(OperationToken operationToken) throws MatchTokenException {

        // DECLARE CURSOR
        apduAST.addOperation(operationToken);

        matchTokenStrong(FUNCTIONAL_TYPE.FOR);

        FunctionalToken token = select();

        matchTokenStrong(token, FUNCTIONAL_TYPE.SEMICOLON);
    }

    /**
     * OPEN;
     * Opens the declared cursor
     * @param operationToken
     * @throws MatchTokenException
     */
    private void open(OperationToken operationToken) throws MatchTokenException {
        apduAST.addOperation(operationToken);
        matchTokenStrong(FUNCTIONAL_TYPE.SEMICOLON);
    }

    /**
     * NEXT;
     * @param operationToken
     * @throws MatchTokenException
     */
    private void nextOperation(OperationToken operationToken) throws MatchTokenException {
        apduAST.addOperation(operationToken);
        matchTokenStrong(FUNCTIONAL_TYPE.SEMICOLON);
    }

    /**
     * FETCH;
     * @param operationToken
     * @throws MatchTokenException
     */
    private void fetch(OperationToken operationToken) throws MatchTokenException {
        apduAST.addOperation(operationToken);
        matchTokenStrong(FUNCTIONAL_TYPE.SEMICOLON);
    }

    /**
     * FETCH NEXT;
     * @param operationToken
     * @throws MatchTokenException
     */
    private void fetchNext(OperationToken operationToken) throws MatchTokenException {
        apduAST.addOperation(operationToken);
        matchTokenStrong(FUNCTIONAL_TYPE.SEMICOLON);
    }

    /**
     * DROP TABLE table_name;
     * @param operationToken
     * @throws MatchTokenException
     */
    private void dropTable(OperationToken operationToken) throws MatchTokenException {
        apduAST.addOperation(operationToken);
        // table_name
        consumeAttribute(apduAST::addTable);
        matchTokenStrong(FUNCTIONAL_TYPE.SEMICOLON);
    }

    /**
     * DROP VIEW view_name;
     * @param operationToken
     * @throws MatchTokenException
     */
    private void dropView(OperationToken operationToken) throws MatchTokenException {
        apduAST.addOperation(operationToken);
        // view_name
        consumeAttribute(apduAST::addView);
        matchTokenStrong(FUNCTIONAL_TYPE.SEMICOLON);
    }

    /**
     * DELETE;
     * @param operationToken
     * @throws MatchTokenException
     */
    private void delete(OperationToken operationToken) throws MatchTokenException {
        apduAST.addOperation(operationToken);
        matchTokenStrong(FUNCTIONAL_TYPE.SEMICOLON);
    }

    /**
     * UPDATE SET columnName = 'value' [, columnName = 'value'];
     * @param operationToken
     * @throws MatchTokenException
     */
    private void update(OperationToken operationToken) throws MatchTokenException {
        apduAST.addOperation(operationToken);

        matchTokenStrong(FUNCTIONAL_TYPE.SET);

        FunctionalToken clause;

        do {
            // Column name
            consumeAttribute(apduAST::addColumn);

            matchTokenStrong(FUNCTIONAL_TYPE.EQUAL);

            matchTokenStrong(FUNCTIONAL_TYPE.APOSTROPHE);
            consumeAttribute(apduAST::addData);
            matchTokenStrong(FUNCTIONAL_TYPE.APOSTROPHE);

            clause = next();
        } while (matchTokenBool(clause, FUNCTIONAL_TYPE.COMMA));

        matchTokenStrong(clause, FUNCTIONAL_TYPE.SEMICOLON);
    }

    /**
     * TRANSACTION: BEGIN;
     * @param operationToken
     * @throws MatchTokenException
     */
    private void begin(OperationToken operationToken) throws MatchTokenException {
        apduAST.addOperation(operationToken);
        matchTokenStrong(FUNCTIONAL_TYPE.SEMICOLON);
    }

    /**
     * TRANSACTION: COMMIT;
     * @param operationToken
     * @throws MatchTokenException
     */
    private void commit(OperationToken operationToken) throws MatchTokenException {
        apduAST.addOperation(operationToken);
        matchTokenStrong(FUNCTIONAL_TYPE.SEMICOLON);
    }

    /**
     * TRANSACTION: ROLLBACK;
     * @param operationToken
     * @throws MatchTokenException
     */
    private void rollback(OperationToken operationToken) throws MatchTokenException {
        apduAST.addOperation(operationToken);
        matchTokenStrong(FUNCTIONAL_TYPE.SEMICOLON);
    }

    /**
     * 'column' [, 'column']
     * @param consumer
     * @return
     * @throws MatchTokenException
     */
    private FunctionalToken consumeToken(Consumer<String> consumer) throws MatchTokenException {
        FunctionalToken nextOperand;
        do {

            matchTokenStrong(FUNCTIONAL_TYPE.APOSTROPHE);
            consumeAttribute(consumer);
            matchTokenStrong(FUNCTIONAL_TYPE.APOSTROPHE);

            nextOperand = next();
        } while (matchTokenBool(nextOperand, FUNCTIONAL_TYPE.COMMA));

        return nextOperand;
    }

    /**
     * SELECT * | ('column_name' [, 'column_name']) FROM table_name
     *      [WHERE column_name comparison 'string' [AND column_name comparison 'string']]
     * @return Last token
     * @throws MatchTokenException
     */
    private FunctionalToken select() throws MatchTokenException {
        matchTokenStrong(FUNCTIONAL_TYPE.SELECT);

        FunctionalToken nextOperand = next();

        if (matchTokenBool(nextOperand, FUNCTIONAL_TYPE.ASTERISK)) {
            apduAST.addColumn("*");
        } else {
            // ( 'column' [, 'column'])
            // (
            matchTokenStrong(nextOperand, FUNCTIONAL_TYPE.LBRACKET);
            FunctionalToken attr = consumeToken(apduAST::addColumn);
            matchTokenStrong(attr, FUNCTIONAL_TYPE.RBRACKET);
        }

        matchTokenStrong(FUNCTIONAL_TYPE.FROM);

        // table name
        FunctionalToken tableName = next();
        matchTokenStrong(tableName, FUNCTIONAL_TYPE.ATTRIBUTE);
        apduAST.addDependent(tableName.getValue(), FIELD_TYPE.TABLE);

        FunctionalToken clause = next();
        if (matchTokenBool(clause, FUNCTIONAL_TYPE.WHERE)) {
            do {
                FunctionalToken column = next();
                matchTokenStrong(column, FUNCTIONAL_TYPE.ATTRIBUTE);

                FunctionalToken operand = next();
                switch (operand.getTokenType()) {
                    case EQUAL:
                    case NOT_EQUAL:
                    case LESS_THAN:
                    case MORE_THAN:
                    case EQUAL_LESS_THAN:
                    case EQUAL_MORE_THEN:
                        break;
                    default:
                        throw new MatchTokenException("Expected a comparison operator, but was" + operand.getTokenType());
                }

                // '
                matchTokenStrong(FUNCTIONAL_TYPE.APOSTROPHE);

                FunctionalToken condition = next();
                matchTokenStrong(condition, FUNCTIONAL_TYPE.ATTRIBUTE);

                matchTokenStrong(FUNCTIONAL_TYPE.APOSTROPHE);

                apduAST.addSearchNode(column.getValue(), operand.getValue(), condition.getValue());
                clause = next();
            } while (matchTokenBool(clause, FUNCTIONAL_TYPE.AND));
        }

        return clause;
    }

    private void consumeAttribute(Consumer<String> astConsumer) throws MatchTokenException {
        FunctionalToken token = next();
        matchTokenStrong(token, FUNCTIONAL_TYPE.ATTRIBUTE);
        astConsumer.accept(token.getValue());
    }

    private void matchTokenStrong(FUNCTIONAL_TYPE expected) throws MatchTokenException {
        matchTokenStrong(next(), expected);
    }

    private void matchTokenStrong(FunctionalToken current, FUNCTIONAL_TYPE expected) throws MatchTokenException {

        if (current == null) {
            throw new NullPointerException("Token is null");
        }

        matchTokenStrong(current.getTokenType(), expected);
    }

    private void matchTokenStrong(FUNCTIONAL_TYPE current, FUNCTIONAL_TYPE expected) throws MatchTokenException {

        if (current == null) {
            throw new NullPointerException("Token is null");
        }

        if (!current.equals(expected)) {
            throw new MatchTokenException("Token match exception! Token type: " + current
                    + ", but excepted: " + expected);
        }
    }

    private boolean matchTokenBool(FUNCTIONAL_TYPE expected) {
        return matchTokenBool(next(), expected);
    }

    private boolean matchTokenBool(FunctionalToken current, FUNCTIONAL_TYPE expected) {
        if (current == null) {
            throw new NullPointerException("Token is null");
        }

        return current.getTokenType().equals(expected);
    }
}
