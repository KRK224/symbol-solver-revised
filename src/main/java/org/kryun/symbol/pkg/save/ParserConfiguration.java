package org.kryun.symbol.pkg.save;


import org.kryun.symbol.javaparser.SymbolBuilderWithJavaParser;
import org.kryun.symbol.pkg.ProjectParser;
import org.kryun.symbol.pkg.build.interfaces.SymbolBuilder;
import org.kryun.symbol.pkg.save.interfaces.SymbolSaver;

public class ParserConfiguration {

    private ParserConfiguration() {
    }

    public static ProjectParser getProjectParser(SymbolBuilder symbolBuilder, SymbolSaver symbolSaver) {
        return new ProjectParser(symbolBuilder, symbolSaver);
    }

    public static SymbolBuilder getJavaParserSymbolBuilder(Long symbolStatusId, String projectPath, String projectName, Boolean isDependency) {
        return new SymbolBuilderWithJavaParser(symbolStatusId, projectPath, projectName, isDependency);
    }

    public static SymbolSaver getFileSymbolSaver() {
        return new SaveSymbolToFile();
    }
}
