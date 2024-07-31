package org.kryun;

import org.kryun.global.config.AppConfig;
import org.kryun.global.enums.symbol.SymbolStatusEnum;
import org.kryun.symbol.javaparser.SymbolBuilderWithJavaParser;
import org.kryun.symbol.model.dto.SymbolStatusDTO;
import org.kryun.symbol.service.SaveAsSymbol;

public class Main {

    public static void main(String[] args) throws Exception {
        String projectPath = "/Users/krk224/Documents/Tmax/1_source/Projects";
        SaveAsSymbol saveAsSymbol = new SaveAsSymbol();
        SymbolStatusDTO symbolStatusDTO = saveAsSymbol.saveAsSymbol("test-px", projectPath, null, "excel");
//        SymbolStatusDTO symbolStatusDTO = saveAsSymbol.saveAsSymbol("blank", projectPath, "/Users/krk224/Documents/Tmax/1_source/symbol-solver-revised/workspace/result/blank/csv/2024-07-02_154142", "excel");
    }

}