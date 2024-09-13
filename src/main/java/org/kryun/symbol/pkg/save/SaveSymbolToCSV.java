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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RequiredArgsConstructor
public class SaveSymbolToCSV implements SymbolSaver {
    private final Logger logger = LoggerFactory.getLogger(SaveSymbolToCSV.class);
    private final String projectPath;
    private final String projectName;
    private String savePath;

    @Override
    public void save(SymbolContainer symbolContainer) throws Exception {
        savePath = createDirectory();
        saveCSVFile(symbolContainer.getSymbolReferenceDTOList(), SymbolReferenceDTO.class, savePath);
        saveCSVFile(symbolContainer.getFullQualifiedNameDTOList(), FullQualifiedNameDTO.class, savePath);
        saveCSVFile(symbolContainer.getBlockDTOList(), BlockDTO.class, savePath);
        saveCSVFile(symbolContainer.getPackageDTOList(), PackageDTO.class, savePath);
        saveCSVFile(symbolContainer.getClassDTOList(), ClassDTO.class, savePath);
        saveCSVFile(symbolContainer.getImportDTOList(), ImportDTO.class, savePath);
        saveCSVFile(symbolContainer.getExpressionDTOList(), ExpressionDTO.class, savePath);
        saveCSVFile(symbolContainer.getMemberVariableDeclarationDTOList(), MemberVariableDeclarationDTO.class, savePath);
        saveCSVFile(symbolContainer.getMethodDeclarationDTOList(), MethodDeclarationDTO.class, savePath);
        saveCSVFile(symbolContainer.getMethodCallExprDTOList(), MethodCallExprDTO.class, savePath);
        saveCSVFile(symbolContainer.getStmtVariableDeclarationDTOList(), StmtVariableDeclarationDTO.class, savePath);
        saveCSVFile(symbolContainer.getArgumentDTOList(), ArgumentDTO.class, savePath);
        saveCSVFile(symbolContainer.getParameterDTOList(), ParameterDTO.class, savePath);
        saveCSVFile(symbolContainer.getReturnMapperDTOList(), ReturnMapperDTO.class, savePath);
        saveCSVFile(symbolContainer.getIfStmtDTOList(), IfStmtDTO.class, savePath);
        saveCSVFile(symbolContainer.getForStmtDTOList(), ForStmtDTO.class, savePath);
        saveCSVFile(symbolContainer.getWhileStmtDTOList(), WhileStmtDTO.class, savePath);
        saveCSVFile(symbolContainer.getSwitchStmtDTOList(), SwitchStmtDTO.class, savePath);
    }

    @Override
    public String getInfo() {
        return savePath;
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
        List<String> columnList = SymbolSaver.createHeader(classType);
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
                        if (!clazz.getPackage().equals(classType.getPackage()))
                            throw new Exception("SaveSymbolToCSV::saveCSVFile::" + column + " is not found in " + classType.getSimpleName());
                        field = clazz.getDeclaredField(column);
                    }
                    String getterMethodName = SymbolSaver.getGetterMethodName(field);
                    try {
                        Object value = clazz.getDeclaredMethod(getterMethodName).invoke(data);
                        if (value!=null)
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
