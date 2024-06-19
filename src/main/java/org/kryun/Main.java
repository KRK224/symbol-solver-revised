package org.kryun;

import org.kryun.global.config.AppConfig;
import org.kryun.global.enums.symbol.SymbolStatusEnum;
import org.kryun.symbol.javaparser.ProjectParser;
import org.kryun.symbol.model.dto.SymbolStatusDTO;

public class Main {

    public static void main(String[] args) throws Exception {
//        Long timeout = 60 *10L;
        SymbolStatusDTO symbolStatusDTO = new SymbolStatusDTO(1L, 1L, 1L, 1L);
        symbolStatusDTO.setStatusEnum(SymbolStatusEnum.ON_GOING);
        symbolStatusDTO.setSymbolStatusId(1L);

        String projName = "blank";
        String projectPath = AppConfig.WORKSPACE_PATH + "/" + projName + "/";

        ProjectParser projectParser = new ProjectParser(projectPath, projName, symbolStatusDTO, false);


        // Connection 넘겨주기
        projectParser.parseProject();
        symbolStatusDTO.setStatusEnum(SymbolStatusEnum.COMPLETED);
    }

}