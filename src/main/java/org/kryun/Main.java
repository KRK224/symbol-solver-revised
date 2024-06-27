package org.kryun;

import org.kryun.global.config.AppConfig;
import org.kryun.global.enums.symbol.SymbolStatusEnum;
import org.kryun.symbol.javaparser.SymbolBuilderWithJavaParser;
import org.kryun.symbol.model.dto.SymbolStatusDTO;
import org.kryun.symbol.service.SaveAsSymbol;

public class Main {

    public static void main(String[] args) throws Exception {
        String projectPath = AppConfig.WORKSPACE_PATH;
        SaveAsSymbol saveAsSymbol = new SaveAsSymbol();
        SymbolStatusDTO symbolStatusDTO = saveAsSymbol.saveAsSymbol("blank", projectPath);
    }

}