package org.kryun.symbol.javaparser.model.exception;


import lombok.Getter;
import org.kryun.global.enums.symbol.SymbolStatusEnum;

@Getter
public class SaveSymbolException extends Exception {
    private final SymbolStatusEnum symbolStatusEnum;
    private String detailMessage;

    public SaveSymbolException(SymbolStatusEnum symbolStatusEnum, String message) {
        super(message);
        this.symbolStatusEnum = symbolStatusEnum;
    }

    public SaveSymbolException(SymbolStatusEnum symbolStatusEnum, String message, String detailMessage) {
        super(message);
        this.symbolStatusEnum = symbolStatusEnum;
        this.detailMessage = detailMessage;
    }

}
