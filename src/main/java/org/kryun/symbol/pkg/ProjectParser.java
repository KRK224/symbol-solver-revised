package org.kryun.symbol.pkg;

import lombok.RequiredArgsConstructor;
import org.kryun.symbol.pkg.builder.interfaces.SymbolBuilder;
import org.kryun.symbol.pkg.builder.interfaces.SymbolContainer;
import org.kryun.symbol.pkg.save.interfaces.SymbolSaver;

@RequiredArgsConstructor
public class ProjectParser {
    private final SymbolBuilder symbolBuilder;
    private final SymbolSaver symbolSaver;

    public void parseProject() throws Exception {
        SymbolContainer symbolContainer  = symbolBuilder.build();
        symbolSaver.save(symbolContainer);
        symbolContainer.clear();
    }

    public String getSymbolSaverInfo() {
        return symbolSaver.getInfo();
    }

}
