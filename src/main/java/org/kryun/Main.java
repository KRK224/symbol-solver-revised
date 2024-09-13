package org.kryun;

import org.kryun.symbol.model.dto.SymbolStatusDTO;
import org.kryun.symbol.service.SaveAsSymbol;

public class Main {

    public static void main(String[] args) throws Exception {
        String projectPath = "/Users/krk224/Documents/Tmax/1_source/Projects";
        String rootPath = "/Users/krk224/Documents/Tmax/1_source";
        SaveAsSymbol saveAsSymbol = new SaveAsSymbol();
        SymbolStatusDTO symbolStatusDTO = saveAsSymbol.saveAsSymbol("test-px", projectPath, null, "excel");
//        SymbolStatusDTO symbolStatusDTO = saveAsSymbol.saveAsSymbol("JavaParser-AST-CodeGen", rootPath, null, "csv");


    }

}