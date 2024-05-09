package com.kpi.scql.generator.decorator.apdu;

import com.kpi.scql.apdu.ApduAst;
import com.kpi.scql.apdu.SearchNode;
import com.kpi.scql.operation.OPERATION_TYPE_GROUP;
import com.sun.javacard.apduio.Apdu;
import org.apache.commons.lang3.ArrayUtils;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ScqlApduDecorator extends BaseApduDecorator {

    @Override
    public Apdu generateApduCommand(ApduAst apduAst) {

        if (apduAst == null) {
            throw new NullPointerException("APDU AST cannot be null!");
        }

        if (apduAst.getIns() != OPERATION_TYPE_GROUP.SCQL) {
            throw new IllegalArgumentException("INS operation is not SCQL creation type. Expected"
                    + OPERATION_TYPE_GROUP.SCQL
                    + ", found " + apduAst.getIns());
        }

        byte[] data = null;
        switch (apduAst.getP2()) {
            case CREATE_TABLE:
                data = createTable(apduAst);
                break;
            case CREATE_VIEW:
                data = createView(apduAst);
                break;
            case INSERT_INTO:
                data = insertInto(apduAst);
                break;
            case DECLARE_CURSOR:
                data = declareCursor(apduAst);
                break;
            case OPEN:
            case NEXT:
            case FETCH:
            case FETCH_NEXT:
            case DELETE:
                break;
            case DROP_TABLE:
                data = dropTable(apduAst);
                break;
            case DROP_VIEW:
                data = dropView(apduAst);
                break;
            case UPDATE:
                data = update(apduAst);
                break;
            default:
                throw new IllegalArgumentException("P2 operation "
                        + apduAst.getP2() + " not supported with "
                        + OPERATION_TYPE_GROUP.SCQL
                );
        }

        Apdu apdu = super.generateApduCommand(apduAst);

        if (data != null) {
            apdu.setDataIn(data, data.length);
        }

        return apdu;
    }

    private byte[] dropTable(ApduAst apduAst) {
        List<Byte> data = new ArrayList<>();

        // lp table_name
        processObj(data, apduAst.getTable());

        return ArrayUtils.toPrimitive(data.toArray(new Byte[0]));
    }

    private byte[] dropView(ApduAst apduAst) {
        List<Byte> data = new ArrayList<>();

        // lp table_name
        processObj(data, apduAst.getView());

        return ArrayUtils.toPrimitive(data.toArray(new Byte[0]));
    }

    private byte[] createTable(ApduAst apduAst) {

        List<Byte> data = new ArrayList<>();

        // lp table_name
        processObj(data, apduAst.getTable());

        List<String> columns = apduAst.getColumnList();

        // N
        data.add((byte) columns.size());

        // lp column_name
        for (String column : columns) {
            processObj(data, column);
        }

        return ArrayUtils.toPrimitive(data.toArray(new Byte[0]));
    }

    private byte[] createView(ApduAst apduAst) {
        List<Byte> data = new ArrayList<>();

        processObj(data, apduAst.getView());

        generateSelect(data, apduAst);

        return ArrayUtils.toPrimitive(data.toArray(new Byte[0]));
    }

    private byte[] insertInto(ApduAst apduAst) {
        List<Byte> data = new ArrayList<>();

        processObj(data, apduAst.getTable());

        int N = apduAst.getDataList().size();
        data.add((byte) N);

        for (String column : apduAst.getDataList()) {
            processObj(data, column);
        }

        return ArrayUtils.toPrimitive(data.toArray(new Byte[0]));
    }

    private byte[] declareCursor(ApduAst apduAst) {

        List<Byte> data = new ArrayList<>();

        generateSelect(data, apduAst);

        return ArrayUtils.toPrimitive(data.toArray(new Byte[0]));
    }

    private byte[] update(ApduAst apduAst) {
        List<Byte> data = new ArrayList<>();

        int columns = apduAst.getColumnList().size();
        data.add((byte) columns);

        // Lp column Lp value
        for (int i = 0; i < columns; i++) {
            data.add((byte) 2); // 2 parameters
            processObj(data, apduAst.getColumnList().get(i));
            processObj(data, apduAst.getDataList().get(i));
        }

        return ArrayUtils.toPrimitive(data.toArray(new Byte[0]));
    }

    private void generateSelect(List<Byte> data, ApduAst apduAst) {
        processObj(data, apduAst.getDependent().getObjName());

        List<String> columns = apduAst.getColumnList();

        if (columns.size() == 1 && columns.get(0).equals("*")) {
            data.add((byte) 0); // N
        } else {
            // N of tables
            data.add((byte) columns.size());

            // lp column_name
            for (String column : columns) {
                processObj(data, column);
            }
        }

        List<SearchNode> search = apduAst.getSearchNodes();

        if (!search.isEmpty()) {

            // N of searches
            data.add((byte) search.size());

            for (SearchNode node : search) {
                // lp column name, lp comparison operator, lp string
                processObj(data, node.getColumnName());
                data.add((byte) 1);
                data.add(node.getOperatorByte());
                processObj(data, node.getValue());
            }
        }
    }

    private void processObj(List<Byte> source, String value) {

        int lp = value.length();
        source.add((byte) lp);

        for (byte valueByte : value.getBytes(StandardCharsets.UTF_8)) {
            source.add(valueByte);
        }
    }

}
