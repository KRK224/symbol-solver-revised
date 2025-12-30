package org.kryun.symbol.pkg;

import org.kryun.symbol.pkg.builder.interfaces.SymbolBuilder;
import org.kryun.symbol.pkg.save.interfaces.SymbolSaver;
import org.springframework.stereotype.Component;

@Component
public class ProjectParser {

    private final SymbolBuilder symbolBuilder;
    private final SymbolSaver symbolSaver;

    public ProjectParser(SymbolBuilder symbolBuilder, SymbolSaver symbolSaver) {
        this.symbolBuilder = symbolBuilder;
        this.symbolSaver = symbolSaver;
    }

    public void parseProject() throws Exception {
        symbolSaver.save(symbolBuilder.build());
    }
}
