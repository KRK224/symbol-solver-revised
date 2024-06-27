package org.kryun.symbol.javaparser.model.exception;

import lombok.Getter;

@Getter
public class PreSaveSymbolException extends Exception{
    private int code;
    private String detailMessage;

    public PreSaveSymbolException(int code, String message){
        super(message);
        this.code = code;
    }

    public PreSaveSymbolException(int code, String message, String detailMessage){
        super(message);
        this.code = code;
        this.detailMessage = message;
    }
}
