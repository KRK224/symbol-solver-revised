package org.kryun.symbol.pkg.save.interfaces;

import java.lang.reflect.Field;
import org.kryun.symbol.pkg.build.interfaces.SymbolContainer;

public interface SymbolSaver {
    void save(SymbolContainer symbolContainer) throws Exception;
}
