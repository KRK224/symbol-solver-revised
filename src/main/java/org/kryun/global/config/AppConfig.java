package org.kryun.global.config;

import java.util.Properties;

public class AppConfig {

    private AppConfig() {
    }

    static {
        Properties properties = new Properties();
        try {
            properties.load(AppConfig.class.getClassLoader().getResourceAsStream("application.properties"));
            TARGET_PATH = properties.getProperty("target.path");
            TARGET_PROJECT = properties.getProperty("target.project");
            SYMBOL_SOURCE_PATH = properties.getProperty("symbol.source.path");
            EXTRACTED_FILE_TYPE = properties.getProperty("extracted.file.type");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // application
    public static final String APP_NAME = "symbol-solver-test";
    public static final String APP_VERSION = "0.1.0";
    // file workspace
    public static final String WORKSPACE_PATH = System.getProperty("user.dir") + "/workspace";
    public static String TARGET_PATH;
    public static String TARGET_PROJECT;
    // 이미 저장된 symbol 파일을 읽어 오는 경우
    public static String SYMBOL_SOURCE_PATH;
    public static String EXTRACTED_FILE_TYPE;

}
