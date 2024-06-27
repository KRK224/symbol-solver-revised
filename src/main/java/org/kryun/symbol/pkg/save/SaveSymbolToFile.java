package org.kryun.symbol.pkg.save;

import org.kryun.symbol.pkg.build.interfaces.SymbolContainer;
import org.kryun.symbol.pkg.save.interfaces.SymbolSaver;

public class SaveSymbolToFile implements SymbolSaver {

    @Override
    public void save(SymbolContainer symbolContainer) throws Exception {
        System.out.println("Save symbol to file");
    }
}
