package org.kryun.symbol.pkg.save;

import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.kryun.symbol.model.dto.*;
import org.kryun.symbol.pkg.builder.interfaces.SymbolContainer;
import org.kryun.symbol.pkg.save.interfaces.SymbolSaver;
import org.kryun.symbol.pkg.save.interfaces.SymbolSaverToFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RequiredArgsConstructor
public class SaveSymbolToCSV implements SymbolSaver {
    private final Logger logger = LoggerFactory.getLogger(SaveSymbolToCSV.class);
    private final String projectPath;
    private final String projectName;


    @Override
    public void save(SymbolContainer symbolContainer) throws Exception {
        String directory = createDirectory();
        saveCSVFile(symbolContainer.getSymbolReferenceDTOList(), SymbolReferenceDTO.class, directory);
        saveCSVFile(symbolContainer.getFullQualifiedNameDTOList(), FullQualifiedNameDTO.class, directory);
        saveCSVFile(symbolContainer.getBlockDTOList(), BlockDTO.class, directory);
        saveCSVFile(symbolContainer.getPackageDTOList(), PackageDTO.class, directory);
        saveCSVFile(symbolContainer.getClassDTOList(), ClassDTO.class, directory);
        saveCSVFile(symbolContainer.getImportDTOList(), ImportDTO.class, directory);
        saveCSVFile(symbolContainer.getExpressionDTOList(), ExpressionDTO.class, directory);
        saveCSVFile(symbolContainer.getMemberVariableDeclarationDTOList(), MemberVariableDeclarationDTO.class, directory);
        saveCSVFile(symbolContainer.getMethodDeclarationDTOList(), MethodDeclarationDTO.class, directory);
        saveCSVFile(symbolContainer.getMethodCallExprDTOList(), MethodCallExprDTO.class, directory);
        saveCSVFile(symbolContainer.getStmtVariableDeclarationDTOList(), StmtVariableDeclarationDTO.class, directory);
        saveCSVFile(symbolContainer.getArgumentDTOList(), ArgumentDTO.class, directory);
        saveCSVFile(symbolContainer.getParameterDTOList(), ParameterDTO.class, directory);
        saveCSVFile(symbolContainer.getReturnMapperDTOList(), ReturnMapperDTO.class, directory);
    }

    private String createDirectory() throws Exception {
        try {
            LocalDateTime now = LocalDateTime.now();
            String formatedNow = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HHmmss"));
            String directoryPath = projectPath + "/result" + "/" + projectName + "/csv/" + formatedNow;
            File directory = new File(directoryPath);
            if (!directory.exists()) {
                boolean mkdir = directory.mkdirs();
                if (mkdir) {
                    logger.info("SaveSymbolToCSV::createDirectory::" + directoryPath + " is created");
                } else {
                    throw new Exception("SaveSymbolToCSV::createDirectory::" + directoryPath + " is not created");
                }
            } else {
                logger.info("SaveSymbolToCSV::createDirectory::" + directoryPath + " is already exist");
            }
            return directoryPath;
        } catch (Exception e) {
            logger.error("SaveSymbolToCSV::createDirectory::" + e.getMessage());
            throw e;
        }
    }

    private void saveCSVFile(List<?> dataList, Class<?> classType, String directoryPath) throws Exception {
        String fileName = classType.getSimpleName();
        List<String> columnList = SymbolSaverToFile.createHeader(classType);
        CSVFormat csvFormat = CSVFormat.EXCEL.builder().setHeader(columnList.toArray(new String[0])).build();
        try (CSVPrinter csvPrinter = new CSVPrinter(new FileWriter(directoryPath + "/" + fileName + ".csv"), csvFormat)) {
            for (Object data : dataList) {
                List<String> row = new ArrayList<>();
                Class<?> clazz = classType;
                for (String column : columnList) {
                    Field field;
                    try {
                        field = clazz.getDeclaredField(column);
                    } catch (NoSuchFieldException e) {
                        clazz = clazz.getSuperclass();
                        if(!clazz.getPackage().equals(classType.getPackage()))
                            throw new Exception("SaveSymbolToCSV::saveCSVFile::" + column + " is not found in " + classType.getSimpleName());
                        field = clazz.getDeclaredField(column);
                    }
                    String getterMethodName = SymbolSaverToFile.getGetterMethodName(field);
                    try {
                        Object value = clazz.getDeclaredMethod(getterMethodName).invoke(data);
                        if(value !=null)
                            row.add(value.toString());
                        else
                            row.add("");
                    } catch (Exception e) {
                        logger.error("Error while creating csv raw: {}", e.getMessage());
                    }
                }
                csvPrinter.printRecord(row);
            }
        } catch (Exception e) {
            logger.error("SaveSymbolToCSV::saveCSVFile::" + e.getMessage());
        }

    }
}
