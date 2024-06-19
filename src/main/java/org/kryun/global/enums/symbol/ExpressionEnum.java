package org.kryun.global.enums.symbol;

public enum ExpressionEnum {
    BOOLEAN_LITERAL_EXPR("BooleanLiteralExpr"), CHAR_LITERAL_EXPR(
            "CharLiteralExpr"), DOUBLE_LITERAL_EXPR(
            "DoubleLiteralExpr"), INT_LITERAL_EXPR("IntegerLiteralExpr"), LONG_LITERAL_EXPR(
            "LongLiteralExpr"), STRING_LITERAL_EXPR("StringLiteralExpr"), NULL_LITERAL_EXPR(
            "NullLiteralExpr"), TEXT_BLOCK_LITERAL_EXPR("TextBlockLiteralExpr"),

    CLASS_EXPR("ClassExpr"), INSTANCE_OF_EXPR("InstanceOfExpr"),
    NAME_EXPR("NameExpr"), TYPE_EXPR("TypeExpr"),
    METHOD_CALL_EXPR("MethodCallExpr"),
    OBJECT_CREATION_EXPR("ObjectCreationExpr"), ARRAY_ACCESS_EXPR("ArrayAccessExpr"),
    ARRAY_CREATION_EXPR("ArrayCreationExpr"), ASSIGN_EXPR("AssignExpr"),
    BINARY_EXPR("BinaryExpr"), CAST_EXPR("CastExpr"),
    CONDITIONAL_EXPR("ConditionalExpr"), FIELD_ACCESS_EXPR("FieldAccessExpr"),
    LAMBDA_EXPR("LambdaExpr"), METHOD_REFERENCE_EXPR(
            "MethodReferenceExpr"), SUPER_EXPR("SuperExpr"), THIS_EXPR("ThisExpr"),
    UNARY_EXPR("UnaryExpr"), ENCLOSED_EXPR("EnclosedExpr");


    private final String type;

    ExpressionEnum(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public String toString() {
        return type;
    }
}
