package org.kryun.symbol.service;

import java.sql.Timestamp;
import org.kryun.global.enums.symbol.SymbolStatusEnum;
import org.kryun.symbol.javaparser.model.exception.SaveSymbolException;
import org.kryun.symbol.model.dto.SymbolStatusDTO;
import org.kryun.symbol.pkg.ProjectParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class SaveAsSymbol {
    private final Logger logger = LoggerFactory.getLogger(SaveAsSymbol.class);
    private final ProjectParser projectParser;

    public SaveAsSymbol(ProjectParser projectParser) {
        this.projectParser = projectParser;
    }

    public SymbolStatusDTO saveAsSymbol(String projName, String projectPath, String resultPath, String fileType) throws Exception {
        SymbolStatusDTO symbolStatusDTO = new SymbolStatusDTO(1L, 1L, null, 1L, SymbolStatusEnum.ON_GOING);
        symbolStatusDTO.setSymbolStatusId(2L);
        try {
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
}
