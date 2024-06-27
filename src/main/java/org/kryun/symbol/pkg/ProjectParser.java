package org.kryun.symbol.pkg;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.kryun.symbol.pkg.build.interfaces.SymbolBuilder;
import org.kryun.symbol.pkg.build.interfaces.SymbolContainer;
import org.kryun.symbol.pkg.save.interfaces.SymbolSaver;

@RequiredArgsConstructor
public class ProjectParser {
    private final SymbolBuilder symbolBuilder;
    private final SymbolSaver symbolSaver;

    public void parseProject() throws Exception {
        SymbolContainer symbolContainer  = symbolBuilder.build();
        symbolSaver.save(symbolContainer);
    }

}
