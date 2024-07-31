package org.kryun.symbol.pkg.builder;

import org.kryun.symbol.pkg.builder.interfaces.SymbolBuilder;
import org.kryun.symbol.pkg.builder.interfaces.SymbolContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SymbolBuilderWithFile implements SymbolBuilder {
    private final Logger logger = LoggerFactory.getLogger(SymbolBuilderWithFile.class);

    private final Long symbolStatusId;
    private final String symbolDataPath;
    private final CSVParser csvParser;

    public SymbolBuilderWithFile(Long symbolStatusId, String symbolDataPath) {
        this.symbolStatusId = symbolStatusId;
        this.symbolDataPath = symbolDataPath;
        this.csvParser = new CSVParser(symbolStatusId, symbolDataPath);
    }

    @Override
    public SymbolContainer build() throws Exception {
        try {
            csvParser.parseFile();
            return csvParser;
        } catch (Exception e) {
            logger.error("Error in SymbolBuilderWithFile.build()", e);
            throw new Exception("Error in SymbolBuilderWithFile.build()");
        }
    }
}
