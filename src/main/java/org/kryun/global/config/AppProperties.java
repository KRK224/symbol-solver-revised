package org.kryun.global.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app")
@Getter
@Setter
public class AppProperties {
    private String parentPath;
    private String targetProject;
    private String symbolSourcePath;
    private String extractedFileType;
    private String workspacePath = System.getProperty("user.dir") + "/workspace";
}
