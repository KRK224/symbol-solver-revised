package org.kryun.symbol.model.dto.interfaces;

public interface FQNReferable extends Referable {

    public abstract Long getFullQualifiedNameId();

    public abstract void setFullQualifiedNameId(Long fullQualifiedNameId);
}
