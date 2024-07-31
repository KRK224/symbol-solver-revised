package org.kryun.symbol.pkg;

import java.sql.Connection;
import org.kryun.symbol.javaparser.SymbolBuilderWithJavaParser;
import org.kryun.symbol.pkg.builder.SymbolBuilderWithFile;
import org.kryun.symbol.pkg.builder.interfaces.SymbolBuilder;
import org.kryun.symbol.pkg.save.SaveSymbolToCSV;
import org.kryun.symbol.pkg.save.SaveSymbolToExcel;
import org.kryun.symbol.pkg.save.interfaces.SymbolSaver;

public class ParserConfiguration {

    private ParserConfiguration() {
    }

    public static ProjectParser getProjectParser(SymbolBuilder symbolBuilder, SymbolSaver symbolSaver) {
        return new ProjectParser(symbolBuilder, symbolSaver);
    }

    public static SymbolBuilder getFileSymbolBuilder(Long symbolStatusId, String symbolDataPath) {
        return new SymbolBuilderWithFile(symbolStatusId, symbolDataPath);
    }

    public static SymbolBuilder getJavaParserSymbolBuilder(Long symbolStatusId, String projectPath, String projectName, Boolean isDependency) {
        return new SymbolBuilderWithJavaParser(symbolStatusId, projectPath, projectName, isDependency);
    }


    public static SymbolSaver getFileSymbolSaver(String projectName, String refName, String fileFormat) throws IllegalArgumentException {
        if (fileFormat.equals("csv")) {
            return new SaveSymbolToCSV(projectName, refName);
        } else if (fileFormat.equals("excel")) {
            return new SaveSymbolToExcel(projectName, refName);
        } else {
            throw new IllegalArgumentException("Invalid file format");
        }
    }
}
