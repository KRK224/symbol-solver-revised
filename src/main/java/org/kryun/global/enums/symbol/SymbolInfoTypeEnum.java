package org.kryun.global.enums.symbol;

public enum SymbolInfoTypeEnum {
    CLASS("class"), ENUM("enum"), INTERFACE("interface"), METHOD_DECL("methodDecl"), METHOD_CALL_EXPR("methodCallExpr"),
    STRUCT("struct"), UNION("union"),
    STMT_VAR_DECL("stmtVarDecl"),
    MEMBER_VAR_DECL("memberVarDecl"), PARAMETER("parameter"), RETURN_MAPPER("returnMapper"), IMPORT("import"),
    UNKNOWN("unknown");

    private String type;

    SymbolInfoTypeEnum(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public String toString() {
        return type;
    }
}