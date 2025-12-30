package org.kryun.symbol.pkg;

import org.kryun.global.config.AppProperties;
import org.kryun.symbol.javaparser.SymbolBuilderWithJavaParser;
import org.kryun.symbol.pkg.builder.SymbolBuilderWithFile;
import org.kryun.symbol.pkg.builder.interfaces.SymbolBuilder;
import org.kryun.symbol.pkg.save.SaveSymbolToCSV;
import org.kryun.symbol.pkg.save.SaveSymbolToExcel;
import org.kryun.symbol.pkg.save.interfaces.SymbolSaver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
public class ParserConfiguration {

    private final AppProperties appProperties;

    public ParserConfiguration(AppProperties appProperties) {
        this.appProperties = appProperties;
    }

    @Bean
    public SymbolBuilder symbolBuilder() {
        String symbolSourcePath = appProperties.getSymbolSourcePath();
        if (symbolSourcePath != null && !symbolSourcePath.isEmpty()) {
            return new SymbolBuilderWithFile(1L, symbolSourcePath);
        } else {
            return new SymbolBuilderWithJavaParser(1L, appProperties.getParentPath(), appProperties.getTargetProject(), false);
        }
    }

    @Bean
    public SymbolSaver symbolSaver() throws IllegalArgumentException {
        String fileFormat = appProperties.getExtractedFileType();
        if (fileFormat.equals("csv")) {
            return new SaveSymbolToCSV(appProperties.getTargetProject(), appProperties.getParentPath());
        } else if (fileFormat.equals("excel")) {
            return new SaveSymbolToExcel(appProperties.getTargetProject(), appProperties.getParentPath());
        } else {
            throw new IllegalArgumentException("Invalid file format");
        }
    }
}
