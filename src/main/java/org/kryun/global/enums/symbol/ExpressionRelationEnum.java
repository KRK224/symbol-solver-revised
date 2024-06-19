package org.kryun.global.enums.symbol;

public enum ExpressionRelationEnum {
    ARRAY_ACCESS_NAME("ArrayAccessExpr", "ArrayName"),
    ARRAY_CREATION_TYPE("ArrayCreationExpr", "ElementType"),
    ASSIGN_TARGET("AssignExpr", "Target"), ASSIGN_VALUE("AssignExpr", "Value"),
    BINARY_LEFT("BinaryExpr", "Left"), BINARY_RIGHT("BinaryExpr", "Right"),
    CAST_EXPRESSION("CastExpr", "Expression"), CAST_TYPE("CastExpr", "Type"),
    CONDITIONAL_THEN("ConditionalExpr", "Then"), CONDITIONAL_ELSE("ConditionalExpr", "Else"),
    ENCLOSED_INNER("EnclosedExpr", "Inner"),
    FIELD_ACCESS_SCOPE("FieldAccessExpr", "Scope"), FIELD_ACCESS_NAME("FieldAccessExpr", "Name"),
    METHOD_CALL_SCOPE("MethodCallExpr", "Scope"), UNARY_EXPRESSION("UnaryExpr",
            "Expression"), LAMBDA_EXPRESSION("LambdaExpr", "Expression"),
    METHOD_REFERENCE_SCOPE("MethodReferenceExpr", "Scope"), OBJECT_CREATION_TYPE(
            "ObjectCreationExpr", "Type");

    private final String parentType;
    private final String childType;
    private final String signature;

    ExpressionRelationEnum(String parentType, String childType) {
        this.parentType = parentType;
        this.childType = childType;
        this.signature = parentType + "_" + childType;
    }

    public String getParentType() {
        return parentType;
    }

    public String getChildType() {
        return childType;
    }

    public String getSignature() {
        return signature;
    }

    public String toString() {
        return signature;
    }

    public static ExpressionRelationEnum getEnumBySignature(String signature) {
        for (ExpressionRelationEnum e : ExpressionRelationEnum.values()) {
            if (e.getSignature().equals(signature)) {
                return e;
            }
        }
        return null;
    }
}
