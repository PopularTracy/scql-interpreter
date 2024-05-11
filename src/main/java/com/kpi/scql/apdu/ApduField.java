package com.kpi.scql.apdu;

import com.kpi.scql.enums.FIELD_TYPE;

public class ApduField {

    private String objName;
    private FIELD_TYPE objType;

    public ApduField(String objName, FIELD_TYPE objType) {
        this.objName = objName;
        this.objType = objType;
    }

    public String getObjName() {
        return objName;
    }

    public void setObjName(String objName) {
        this.objName = objName;
    }

    public FIELD_TYPE getObjType() {
        return objType;
    }

    public void setObjType(FIELD_TYPE objType) {
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
