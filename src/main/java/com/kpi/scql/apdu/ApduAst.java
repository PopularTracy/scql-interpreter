package com.kpi.scql.apdu;

import com.kpi.scql.operation.OPERATION;
import com.kpi.scql.operation.OPERATION_TYPE_GROUP;
import com.kpi.scql.token.OperationToken;

import java.util.ArrayList;
import java.util.List;

public class ApduAst {

    // Used for tables, view and dictionaries
    private Storage storage;
    private Storage dependent;

    private List<String> columnList = new ArrayList<>();
    private List<String> dataList = new ArrayList<>();

    private List<SearchNode> searchNodes = new ArrayList<>();

    private OPERATION_TYPE_GROUP ins;
    private OPERATION p2;

    private String scqlCommand;

    public ApduAst() {
    }

    public ApduAst(OPERATION_TYPE_GROUP ins, OPERATION p2) {
        this.ins = ins;
        this.p2 = p2;
    }

    public void addTable(String table) {
        addObj(table, APDU_FIELD_TYPE.TABLE);
    }

    public void addView(String view) {
        addObj(view, APDU_FIELD_TYPE.VIEW);
    }

    public void addObj(String name, APDU_FIELD_TYPE type) {
        this.storage = new Storage(name, type);
    }

    public void addDependent(String name, APDU_FIELD_TYPE type) {
        this.dependent = new Storage(name, type);
    }

    public void addData(String data) {
        dataList.add(data);
    }

    public void addSearchNode(String columnName, String operator, String value) {
        searchNodes.add(new SearchNode(columnName, operator, value));
    }

    public void addColumn(String column) {

        if (columnList.contains(column)) {
            throw new RuntimeException("Duplicate column name! Duplicate value: " + column);
        }

        columnList.add(column);
    }


    public OPERATION_TYPE_GROUP getIns() {
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
        return getStorageName(APDU_FIELD_TYPE.TABLE);
    }

    public String getView() {
        return getStorageName(APDU_FIELD_TYPE.VIEW);
    }

    private String getStorageName(APDU_FIELD_TYPE type) {
        if (storage.getObjType() != type) {
            throw new IllegalArgumentException("Expected type of object " + type + ", but was" + storage.getObjType());
        }

        return storage.getObjName();
    }

    public Storage getStorage() {
        return storage;
    }

    public Storage getDependent() {
        return dependent;
    }

    public List<String> getColumnList() {
        return columnList;
    }

    public List<String> getDataList() {
        return dataList;
    }

    public List<SearchNode> getSearchNodes() {
        return searchNodes;
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
                "storage=" + storage +
                ", dependent=" + dependent +
                ", columnList=" + columnList +
                ", dataList=" + dataList +
                ", searchNodes=" + searchNodes +
                ", ins=" + ins +
                ", p2=" + p2 +
                '}';
    }
}
