package org.kryun.global.utils;

import java.io.File;

public class FileUtil {
    public static long getDirectorySize(String directoryPath) {
        File directory = new File(directoryPath);
        return getDirectorySize(directory);
    }

    public static long getDirectorySize(File directory) {
        long size = 0;

        // 디렉토리 내의 모든 파일 및 하위 디렉토리를 가져오기
        File[] files = directory.listFiles();
        if (files!=null) {
            for (File file : files) {
                if (file.isFile()) {
                    size += file.length();
                } else if (file.isDirectory()) {
                    size += getDirectorySize(file);
                }
            }
        }
        return size;
    }
}
