package org.kryun.global.enums.symbol;

public enum BytecodeSignatureEnum {
    VOID("V", "void", true), BOOLEAN("Z", "boolean", true), BYTE("B", "byte", true),
    CHAR("C", "char", true), SHORT("S", "short", true), INT("I", "int", true),
    LONG("J", "long", true), FLOAT("F", "float", true), DOUBLE("D", "double", true),
    OBJECT("L", "object", false), ARRAY("[", "array", false),
    GENERIC("T", "generic", false), WILDCARD("*", "?", true),
    EXTENDS("+", "? extends ", true), SUPER("-", "? super ", true),
    GENERIC_BOUND(":", " extends ", true);
    private final String signature;
    private final String type;
    private final boolean hasToReplace;

    BytecodeSignatureEnum(String signature, String type, boolean hasToReplace) {
        this.signature = signature;
        this.type = type;
        this.hasToReplace = hasToReplace;
    }

    public String getSignature() {
        return signature;
    }

    public String getType() {
        return hasToReplace ? type:"";
    }

    public boolean isHasToReplace() {
        return hasToReplace;
    }

    public static String decode(String signature) {
        switch (signature) {
            case "V":
                return VOID.getType();
            case "Z":
                return BOOLEAN.getType();
            case "B":
                return BYTE.getType();
            case "C":
                return CHAR.getType();
            case "S":
                return SHORT.getType();
            case "I":
                return INT.getType();
            case "J":
                return LONG.getType();
            case "F":
                return FLOAT.getType();
            case "D":
                return DOUBLE.getType();
            case "L":
                return OBJECT.getType();
            case "[":
                return ARRAY.getType();
            case "T":
                return GENERIC.getType();
            case "*":
                return WILDCARD.getType();
            case "+":
                return EXTENDS.getType();
            case "-":
                return SUPER.getType();
            case ":":
                return GENERIC_BOUND.getType();
            default:
                return signature;
        }
    }
}
