package com.kpi.scql.token;

public enum FUNCTIONAL_TOKEN_TYPE {
    // Symbols
    LBRACKET,
    RBRACKET,
    APOSTROPHE,
    SEMICOLON,
    ASTERISK,
    COMMA,

    // With attributes
    ATTRIBUTE,
    AS,
    SELECT,
    FROM,
    WHERE,
    VALUES,
    FOR,
    SET,

    // Comparison
    AND,
    EQUAL,
    NOT_EQUAL,
    MORE_THAN,
    LESS_THAN,
    EQUAL_MORE_THEN,
    EQUAL_LESS_THAN,

    // Other
    EOF
}
