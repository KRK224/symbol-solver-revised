package org.kryun.symbol.javaparser.model.exception;

import lombok.Getter;
import org.kryun.global.enums.symbol.SymbolStatusEnum;

@Getter
public class PreSaveSymbolException extends Exception {
    private int code;
    private String detailMessage;
    private SymbolStatusEnum symbolStatusEnum;

    public PreSaveSymbolException(int code, String message) {
        super(message);
        this.code = code;
    }

    public PreSaveSymbolException(int code, SymbolStatusEnum symbolStatusEnum, String message) {
        super(message);
        this.code = code;
        this.symbolStatusEnum = symbolStatusEnum;
    }

    public PreSaveSymbolException(int code, String message, String detailMessage) {
        super(message);
        this.code = code;
        this.detailMessage = detailMessage;
    }

    public PreSaveSymbolException(int code, SymbolStatusEnum symbolStatusEnum, String message, String detailMessage) {
        super(message);
        this.code = code;
        this.symbolStatusEnum = symbolStatusEnum;
        this.detailMessage = detailMessage;
    }
}
