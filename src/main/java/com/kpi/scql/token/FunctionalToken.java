package com.kpi.scql.token;

public class FunctionalToken {

    private FUNCTIONAL_TOKEN_TYPE tokenType;
    private String value;

    public FunctionalToken(FUNCTIONAL_TOKEN_TYPE tokenType) {
        this.tokenType = tokenType;
    }

    public FunctionalToken(FUNCTIONAL_TOKEN_TYPE tokenType, String value) {
        this.tokenType = tokenType;
        this.value = value;
    }

    public FUNCTIONAL_TOKEN_TYPE getTokenType() {
        return tokenType;
    }

    public void setTokenType(FUNCTIONAL_TOKEN_TYPE tokenType) {
        this.tokenType = tokenType;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
