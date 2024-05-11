package com.kpi.scql.token;

import com.kpi.scql.enums.FUNCTIONAL_TYPE;

public class FunctionalToken {

    private FUNCTIONAL_TYPE tokenType;
    private String value;

    public FunctionalToken(FUNCTIONAL_TYPE tokenType) {
        this.tokenType = tokenType;
    }

    public FunctionalToken(FUNCTIONAL_TYPE tokenType, String value) {
        this.tokenType = tokenType;
        this.value = value;
    }

    public FUNCTIONAL_TYPE getTokenType() {
        return tokenType;
    }

    public void setTokenType(FUNCTIONAL_TYPE tokenType) {
        this.tokenType = tokenType;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
