package org.kryun;

import org.kryun.global.config.AppProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication(scanBasePackages = "org.kryun")
@EnableConfigurationProperties(AppProperties.class)
public class SymbolSolverApplication {

    public static void main(String[] args) {
        SpringApplication.run(SymbolSolverApplication.class, args);
    }

}
