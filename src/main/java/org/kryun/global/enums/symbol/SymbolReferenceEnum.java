package org.kryun.global.enums.symbol;

public enum SymbolReferenceEnum {
    PRIMITIVE(0L),
    JDK(-1L),
    ARRAY(-10L),
    NULL(-100L);

    private final Long referenceTypeId;

    SymbolReferenceEnum(Long typeId) {
        this.referenceTypeId = typeId;
    }

    public Long getReferenceTypeId() {
        return referenceTypeId;
    }
}