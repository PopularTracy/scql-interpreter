package com.kpi.scql.apdu;

import com.kpi.scql.enums.FIELD_TYPE;
import com.kpi.scql.enums.OPERATION;
import com.kpi.scql.enums.OPERATION_GROUP;
import com.kpi.scql.token.OperationToken;

import java.util.ArrayList;
import java.util.List;

public class ApduAst {

    // Used for tables, view and dictionaries
    private ApduField apduField;
    private ApduField dependent;

    private List<String> columnList = new ArrayList<>();
    private List<String> dataList = new ArrayList<>();

    private List<ApduSearchNode> apduSearchNodes = new ArrayList<>();

    private OPERATION_GROUP ins;
    private OPERATION p2;

    private String scqlCommand;

    public ApduAst() {
    }

    public ApduAst(OPERATION_GROUP ins, OPERATION p2) {
        this.ins = ins;
        this.p2 = p2;
    }

    public void addTable(String table) {
        addObj(table, FIELD_TYPE.TABLE);
    }

    public void addView(String view) {
        addObj(view, FIELD_TYPE.VIEW);
    }

    public void addObj(String name, FIELD_TYPE type) {
        this.apduField = new ApduField(name, type);
    }

    public void addDependent(String name, FIELD_TYPE type) {
        this.dependent = new ApduField(name, type);
    }

    public void addData(String data) {
        dataList.add(data);
    }

    public void addSearchNode(String columnName, String operator, String value) {
        apduSearchNodes.add(new ApduSearchNode(columnName, operator, value));
    }

    public void addColumn(String column) {

        if (columnList.contains(column)) {
            throw new RuntimeException("Duplicate column name! Duplicate value: " + column);
        }

        columnList.add(column);
    }


    public OPERATION_GROUP getIns() {
        return ins;
    }

    public OPERATION getP2() {
        return p2;
    }

    public void addOperation(OperationToken token) {
        ins = token.getINS();
        p2 = token.getOperation();
    }

    public String getTable() {
        return getStorageName(FIELD_TYPE.TABLE);
    }

    public String getView() {
        return getStorageName(FIELD_TYPE.VIEW);
    }

    private String getStorageName(FIELD_TYPE type) {
        if (apduField.getObjType() != type) {
            throw new IllegalArgumentException("Expected type of object " + type + ", but was" + apduField.getObjType());
        }

        return apduField.getObjName();
    }

    public ApduField getStorage() {
        return apduField;
    }

    public ApduField getDependent() {
        return dependent;
    }

    public List<String> getColumnList() {
        return columnList;
    }

    public List<String> getDataList() {
        return dataList;
    }

    public List<ApduSearchNode> getSearchNodes() {
        return apduSearchNodes;
    }

    public String getScqlCommand() {
        return scqlCommand;
    }

    public void setScqlCommand(String scqlCommand) {
        this.scqlCommand = scqlCommand;
    }

    @Override
    public String toString() {
        return "ApduAst{" +
                "storage=" + apduField +
                ", dependent=" + dependent +
                ", columnList=" + columnList +
                ", dataList=" + dataList +
                ", searchNodes=" + apduSearchNodes +
                ", ins=" + ins +
                ", p2=" + p2 +
                '}';
    }
}
