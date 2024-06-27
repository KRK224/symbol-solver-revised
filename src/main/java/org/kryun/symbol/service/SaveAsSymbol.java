package org.kryun.symbol.service;

import java.sql.Connection;
import java.sql.Timestamp;
import org.kryun.global.config.AppConfig;
import org.kryun.global.enums.symbol.SymbolStatusEnum;
import org.kryun.symbol.javaparser.model.exception.PreSaveSymbolException;
import org.kryun.symbol.javaparser.model.exception.SaveSymbolException;
import org.kryun.symbol.model.dto.SymbolStatusDTO;
import org.kryun.symbol.pkg.ProjectParser;
import org.kryun.symbol.pkg.build.interfaces.SymbolBuilder;
import org.kryun.symbol.pkg.save.ParserConfiguration;
import org.kryun.symbol.pkg.save.interfaces.SymbolSaver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SaveAsSymbol {
    private final Logger logger = LoggerFactory.getLogger(SaveAsSymbol.class);

    public SymbolStatusDTO saveAsSymbol(String projName, String projectPath) throws Exception {
        SymbolStatusDTO symbolStatusDTO = new SymbolStatusDTO(1L, 1L, null, 1L);
        symbolStatusDTO.setStatusEnum(SymbolStatusEnum.ON_GOING);
        symbolStatusDTO.setSymbolStatusId(1L);
        try {
            ProjectParser projectParser = getProjectParser(symbolStatusDTO.getSymbolStatusId(), projectPath, projName, false);
            projectParser.parseProject();
            symbolStatusDTO.setStatusEnum(SymbolStatusEnum.COMPLETED);
            return symbolStatusDTO;

        } catch (Exception e) {
            logger.error("Error occurred while saving symbol", e);
            SymbolStatusEnum errorStatus = e instanceof SaveSymbolException ? ((SaveSymbolException) e).getSymbolStatusEnum():SymbolStatusEnum.ERROR;
            symbolStatusDTO.setUpdatedTime(new Timestamp(System.currentTimeMillis()));
            symbolStatusDTO.setStatusEnum(errorStatus);


            return symbolStatusDTO;
        }
    }

    private ProjectParser getProjectParser(Long symbolStatusId, String projectPath, String projectName, Boolean isDependency)
            throws Exception {
        SymbolSaver symbolSaver = ParserConfiguration.getFileSymbolSaver();

        SymbolBuilder symbolBuilder = ParserConfiguration.getJavaParserSymbolBuilder(symbolStatusId, projectPath, projectName, isDependency);

        return ParserConfiguration.getProjectParser(symbolBuilder, symbolSaver);
    }
}
