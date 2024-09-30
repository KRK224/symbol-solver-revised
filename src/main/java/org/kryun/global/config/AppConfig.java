package org.kryun.global.config;

import java.util.Properties;

public class AppConfig {

    private AppConfig() {
    }

    static {
        Properties properties = new Properties();
        try {
            properties.load(AppConfig.class.getClassLoader().getResourceAsStream("application.properties"));
            PARENT_PATH = properties.getProperty("parent.path");
            TARGET_PROJECT = properties.getProperty("target.project");
            SYMBOL_SOURCE_PATH = properties.getProperty("symbol.source.path");
            EXTRACTED_FILE_TYPE = properties.getProperty("extracted.file.type");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // application
    public static final String APP_NAME = "symbol-solver-test";
    public static final String APP_VERSION = "1.0.0";
    // file workspace
    public static final String WORKSPACE_PATH = System.getProperty("user.dir") + "/workspace";
    public static String PARENT_PATH;
    public static String TARGET_PROJECT;
    // if you want to save symbol data to file, use this field.
    public static String EXTRACTED_FILE_TYPE;

    // read analyzed symbol data from file
    public static String SYMBOL_SOURCE_PATH;


}
