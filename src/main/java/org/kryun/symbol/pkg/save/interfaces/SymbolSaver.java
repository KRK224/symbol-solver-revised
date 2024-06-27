package org.kryun.symbol.pkg.save.interfaces;

import org.kryun.symbol.pkg.build.interfaces.SymbolContainer;

public interface SymbolSaver {
    void save(SymbolContainer symbolContainer) throws Exception;
}
