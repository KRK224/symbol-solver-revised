package org.kryun.symbol.javaparser.management;

import lombok.Getter;

public class IdentifierGenerator {
    @Getter
    private String symbolType;
    private Long cursorId = 0L;
    private Long lastId = -1L;

    public IdentifierGenerator(String symbolType) {
        this.symbolType = symbolType;
    }

    public Long nextId() {
        Long nextId = cursorId;
        lastId = cursorId++;
        return nextId;
    }

    public Long getLastId() {
        return lastId;
    }

    public void removeId() {
        if (lastId==-1L) {
            throw new IllegalStateException("No id to remove");
        } else {
            cursorId = lastId;
            lastId--;
        }
    }

}
