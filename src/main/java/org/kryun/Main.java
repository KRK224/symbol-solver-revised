package org.kryun;

import org.kryun.global.config.AppConfig;
import org.kryun.symbol.model.dto.SymbolStatusDTO;
import org.kryun.symbol.service.SaveAsSymbol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws Exception {
        SaveAsSymbol saveAsSymbol = new SaveAsSymbol();
        String parentPath = AppConfig.PARENT_PATH==null ? AppConfig.WORKSPACE_PATH:AppConfig.PARENT_PATH;
        String fileType = AppConfig.EXTRACTED_FILE_TYPE==null ? "csv":AppConfig.EXTRACTED_FILE_TYPE;
        String symbolSourcePath = AppConfig.SYMBOL_SOURCE_PATH;
        if (AppConfig.TARGET_PROJECT==null)
            throw new IllegalArgumentException("You must provide a project name");

        SymbolStatusDTO symbolStatusDTO = saveAsSymbol.saveAsSymbol(AppConfig.TARGET_PROJECT, parentPath, symbolSourcePath, fileType);
        logger.info("Symbol status: {}", symbolStatusDTO.getStatusEnum());
    }

}