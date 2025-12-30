package org.kryun;

import org.kryun.global.config.AppProperties;
import org.kryun.symbol.model.dto.SymbolStatusDTO;
import org.kryun.symbol.service.SaveAsSymbol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("!test")
public class CommandLineAppStartupRunner {

    private static final Logger logger = LoggerFactory.getLogger(CommandLineAppStartupRunner.class);

    @Bean
    public CommandLineRunner run(SaveAsSymbol saveAsSymbol, AppProperties appProperties) {
        return args -> {
            String parentPath = appProperties.getParentPath() == null ? appProperties.getWorkspacePath() : appProperties.getParentPath();
            String fileType = appProperties.getExtractedFileType() == null ? "csv" : appProperties.getExtractedFileType();
            String symbolSourcePath = appProperties.getSymbolSourcePath();
            if (appProperties.getTargetProject() == null || appProperties.getTargetProject().equals("your-project-name"))
                throw new IllegalArgumentException("You must provide a project name in application.properties");

            SymbolStatusDTO symbolStatusDTO = saveAsSymbol.saveAsSymbol(appProperties.getTargetProject(), parentPath, symbolSourcePath, fileType);
            logger.info("Symbol status: {}", symbolStatusDTO.getStatusEnum());
        };
    }
}
