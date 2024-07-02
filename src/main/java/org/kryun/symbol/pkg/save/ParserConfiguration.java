package org.kryun.symbol.pkg.save;


import org.kryun.symbol.javaparser.SymbolBuilderWithJavaParser;
import org.kryun.symbol.pkg.ProjectParser;
import org.kryun.symbol.pkg.builder.SymbolBuilderWithFile;
import org.kryun.symbol.pkg.builder.interfaces.SymbolBuilder;
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

    public static SymbolBuilder getFileSymbolBuilder(Long symbolStatusId, String filePath) {
        return new SymbolBuilderWithFile(symbolStatusId, filePath);
    }

    public static SymbolSaver getFileSymbolSaver(String projectPath, String projectName, String fileType) {
        return fileType.equals("excel") ? new SaveSymbolToExcel(projectPath, projectName): new SaveSymbolToCSV(projectPath, projectName);
    }
}
