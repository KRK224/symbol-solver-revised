package org.kryun.global.enums.symbol;

public enum SymbolStatusEnum {
    ON_GOING("onGoing"), COMPLETED("completed"), ERROR("error"), UNAVAILAbLE("unavailable"), TIMEOUT("timeout"),
    NULL("null");

    private String status;

    SymbolStatusEnum(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public static SymbolStatusEnum findEnum(String status) {
        switch (status) {
            case "onGoing":
                return ON_GOING;
            case "completed":
                return COMPLETED;
            case "error":
                return ERROR;
            case "unavailable":
                return UNAVAILAbLE;
            case "timeout":
                return TIMEOUT;
            default:
                return NULL;
        }
    }

    public String toString() {
        return status;
    }
}
