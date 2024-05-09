package com.kpi.scql.apdu;

public class Storage {

    private String objName;
    private APDU_FIELD_TYPE objType;

    public Storage(String objName, APDU_FIELD_TYPE objType) {
        this.objName = objName;
        this.objType = objType;
    }

    public String getObjName() {
        return objName;
    }

    public void setObjName(String objName) {
        this.objName = objName;
    }

    public APDU_FIELD_TYPE getObjType() {
        return objType;
    }

    public void setObjType(APDU_FIELD_TYPE objType) {
        this.objType = objType;
    }

    @Override
    public String toString() {
        return "Storage{" +
                "objName='" + objName + '\'' +
                ", objType=" + objType +
                '}';
    }
}
