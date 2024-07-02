package org.kryun.symbol.pkg.save;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.kryun.symbol.model.dto.ArgumentDTO;
import org.kryun.symbol.model.dto.BlockDTO;
import org.kryun.symbol.model.dto.ClassDTO;
import org.kryun.symbol.model.dto.ExpressionDTO;
import org.kryun.symbol.model.dto.FullQualifiedNameDTO;
import org.kryun.symbol.model.dto.ImportDTO;
import org.kryun.symbol.model.dto.MemberVariableDeclarationDTO;
import org.kryun.symbol.model.dto.MethodCallExprDTO;
import org.kryun.symbol.model.dto.MethodDeclarationDTO;
import org.kryun.symbol.model.dto.PackageDTO;
import org.kryun.symbol.model.dto.ParameterDTO;
import org.kryun.symbol.model.dto.ReturnMapperDTO;
import org.kryun.symbol.model.dto.StmtVariableDeclarationDTO;
import org.kryun.symbol.model.dto.SymbolReferenceDTO;
import org.kryun.symbol.pkg.builder.interfaces.SymbolContainer;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.kryun.symbol.pkg.save.interfaces.SymbolSaverToFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RequiredArgsConstructor
public class SaveSymbolToExcel implements SymbolSaverToFile {
    private final Logger logger = LoggerFactory.getLogger(SaveSymbolToExcel.class);
    private final String projectPath;
    private final String projectName;

    @Override
    public void save(SymbolContainer symbolContainer) throws Exception {
        try {
            XSSFWorkbook wb = new XSSFWorkbook();

            createExcelSheet(wb, symbolContainer.getSymbolReferenceDTOList(), SymbolReferenceDTO.class);
            createExcelSheet(wb, symbolContainer.getFullQualifiedNameDTOList(), FullQualifiedNameDTO.class);
            createExcelSheet(wb, symbolContainer.getBlockDTOList(), BlockDTO.class);
            createExcelSheet(wb, symbolContainer.getPackageDTOList(), PackageDTO.class);
            createExcelSheet(wb, symbolContainer.getClassDTOList(), ClassDTO.class);
            createExcelSheet(wb, symbolContainer.getImportDTOList(), ImportDTO.class);
            createExcelSheet(wb, symbolContainer.getExpressionDTOList(), ExpressionDTO.class);
            createExcelSheet(wb, symbolContainer.getMemberVariableDeclarationDTOList(), MemberVariableDeclarationDTO.class);
            createExcelSheet(wb, symbolContainer.getMethodDeclarationDTOList(), MethodDeclarationDTO.class);
            createExcelSheet(wb, symbolContainer.getMethodCallExprDTOList(), MethodCallExprDTO.class);
            createExcelSheet(wb, symbolContainer.getStmtVariableDeclarationDTOList(), StmtVariableDeclarationDTO.class);
            createExcelSheet(wb, symbolContainer.getArgumentDTOList(), ArgumentDTO.class);
            createExcelSheet(wb, symbolContainer.getParameterDTOList(), ParameterDTO.class);
            createExcelSheet(wb, symbolContainer.getReturnMapperDTOList(), ReturnMapperDTO.class);

            saveExcelFile(wb);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    private void saveExcelFile(XSSFWorkbook wb) throws Exception {
        try {
            // Create Excel file
            LocalDateTime now = LocalDateTime.now();
            String formatedNow = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HHmmss_"));

            File resultDirectory = new File(projectPath + "/result" + "/" + projectName);
            if (!resultDirectory.exists()) {
                boolean mkdirs = resultDirectory.mkdirs();
                if (mkdirs) {
                    logger.info("SaveSymbolToExcel::createDirectory::" + resultDirectory.getAbsolutePath() + " is created");
                } else {
                    throw new Exception("SaveSymbolToExcel::createDirectory: " + resultDirectory.getAbsolutePath() + " is not created");
                }
            } else {
                logger.info("Folder already exists");
            }

            String path = resultDirectory.getAbsolutePath() + "/" + formatedNow + projectName + ".xlsx";
            FileOutputStream fileOut = new FileOutputStream(path);
            wb.write(fileOut);
            wb.close();

        } catch (Exception e) {
            logger.error("Error while creating Excel file: {}", e.getMessage());
            throw e;
        }
    }

    private void createExcelSheet(XSSFWorkbook wb, List<?> dataList, Class<?> classType) throws Exception {
        String sheetName = classType.getSimpleName();
        System.out.println("sheetName = " + sheetName);
        System.out.println("clazz = " + classType);
        List<String> columnList = SymbolSaverToFile.createHeader(classType);
        System.out.println("columnList = " + columnList);

        // Create Excel title
        XSSFSheet sheet = createTitle(wb, columnList, sheetName);

        // Create Excel body
        for (int i = 0; i < dataList.size(); i++) {
            Row bodyRow = sheet.createRow(i + 1);
            Object data = dataList.get(i);
            Class<?> clazz = classType;
            for (int j = 0; j < columnList.size(); j++) {
                Cell cell = bodyRow.createCell(j);
                Field field;
                try {
                    field = clazz.getDeclaredField(columnList.get(j));
                } catch (NoSuchFieldException noSuchFieldException) {
                    // 부모 클래스의 필드인 경우
                    clazz = clazz.getSuperclass();
                    field = clazz.getDeclaredField(columnList.get(j));
                }
                String getterMethodName = SymbolSaverToFile.getGetterMethodName(field);
                try {
                    Object invoke = clazz.getDeclaredMethod(getterMethodName).invoke(data);
                    if (invoke!=null)
                        cell.setCellValue(invoke.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.error("Error while creating Excel body: {}", e.getMessage());
                }
            }
        }
    }

    private XSSFSheet createTitle(XSSFWorkbook wb, List<String> columnList, String sheetName) {
        XSSFSheet sheet = wb.createSheet(sheetName);
        CellStyle headStyle = wb.createCellStyle();

        XSSFFont title_font = wb.createFont();
        title_font.setFontName(HSSFFont.FONT_ARIAL);
        title_font.setFontHeightInPoints((short) 12);
        title_font.setBold(true);

        headStyle.setFont(title_font);
        headStyle.setAlignment(HorizontalAlignment.CENTER);
        headStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        XSSFRow titleRow = sheet.createRow(0);
        // 타이틀 컬럼 값 지정
        for (int i = 0; i < columnList.size(); i++) {
            XSSFCell cell = titleRow.createCell(i);
            cell.setCellStyle(headStyle);
            cell.setCellValue(columnList.get(i));
        }

        return sheet;
    }

}
