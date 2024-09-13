package org.kryun.symbol.pkg.builder;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.kryun.global.enums.symbol.ExpressionRelationEnum;
import org.kryun.symbol.model.dto.ArgumentDTO;
import org.kryun.symbol.model.dto.BlockDTO;
import org.kryun.symbol.model.dto.ClassDTO;
import org.kryun.symbol.model.dto.ExpressionDTO;
import org.kryun.symbol.model.dto.ForStmtDTO;
import org.kryun.symbol.model.dto.FullQualifiedNameDTO;
import org.kryun.symbol.model.dto.IfStmtDTO;
import org.kryun.symbol.model.dto.ImportDTO;
import org.kryun.symbol.model.dto.MemberVariableDeclarationDTO;
import org.kryun.symbol.model.dto.MethodCallExprDTO;
import org.kryun.symbol.model.dto.MethodDeclarationDTO;
import org.kryun.symbol.model.dto.PackageDTO;
import org.kryun.symbol.model.dto.ParameterDTO;
import org.kryun.symbol.model.dto.Position;
import org.kryun.symbol.model.dto.ReturnMapperDTO;
import org.kryun.symbol.model.dto.StmtVariableDeclarationDTO;
import org.kryun.symbol.model.dto.SwitchStmtDTO;
import org.kryun.symbol.model.dto.SymbolReferenceDTO;
import org.kryun.symbol.model.dto.WhileStmtDTO;
import org.kryun.symbol.pkg.builder.interfaces.SymbolContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RequiredArgsConstructor
public class CSVParser implements SymbolContainer {

    private final Logger logger = LoggerFactory.getLogger(CSVParser.class);

    private final Long symbolStatusId;
    private final String symbolDataPath;

    private final Map<String, Long> symbolIds = new LinkedHashMap<>() {
        {
            put("SymbolReferenceDTO", -1L);
            put("ArgumentDTO", -1L);
            put("BlockDTO", -1L);
            put("PackageDTO", -1L);
            put("ClassDTO", -1L);
            put("ImportDTO", -1L);
            put("ParameterDTO", -1L);
            put("ReturnMapperDTO", -1L);
            put("MethodDeclarationDTO", -1L);
            put("MethodCallExprDTO", -1L);
            put("StmtVariableDeclarationDTO", -1L);
            put("MemberVariableDeclarationDTO", -1L);
            put("ExpressionDTO", -1L);
            put("FullQualifiedNameDTO", -1L);
        }
    };
    private final List<SymbolReferenceDTO> symbolReferenceDTOList = new ArrayList<>();
    private final List<ArgumentDTO> argumentDTOList = new ArrayList<>();
    private final List<BlockDTO> blockDTOList = new ArrayList<>();
    private final List<PackageDTO> packageDTOList = new ArrayList<>();
    private final List<ClassDTO> classDTOList = new ArrayList<>();
    private final List<ImportDTO> importDTOList = new ArrayList<>();
    private final List<ParameterDTO> parameterDTOList = new ArrayList<>();
    private final List<ReturnMapperDTO> returnMapperDTOList = new ArrayList<>();
    private final List<MethodDeclarationDTO> methodDeclarationDTOList = new ArrayList<>();
    private final List<MethodCallExprDTO> methodCallExprDTOList = new ArrayList<>();
    private final List<StmtVariableDeclarationDTO> stmtVariableDeclarationDTOList = new ArrayList<>();
    private final List<MemberVariableDeclarationDTO> memberVariableDeclarationDTOList = new ArrayList<>();
    private final List<ExpressionDTO> expressionDTOList = new ArrayList<>();
    private final List<FullQualifiedNameDTO> fullQualifiedNameDTOList = new ArrayList<>();
    private final List<IfStmtDTO> ifStmtDTOList = new ArrayList<>();
    private final List<ForStmtDTO> forStmtDTOList = new ArrayList<>();
    private final List<WhileStmtDTO> whileStmtDTOList = new ArrayList<>();
    private final List<SwitchStmtDTO> switchStmtDTOList = new ArrayList<>();

    @Override
    public Map<String, Long> getSymbolIds() {
        return symbolIds;
    }

    @Override
    public List<SymbolReferenceDTO> getSymbolReferenceDTOList() {
        return symbolReferenceDTOList;
    }

    @Override
    public List<FullQualifiedNameDTO> getFullQualifiedNameDTOList() {
        return fullQualifiedNameDTOList;
    }

    @Override
    public List<? extends BlockDTO> getBlockDTOList() {
        return blockDTOList;
    }

    @Override
    public List<? extends PackageDTO> getPackageDTOList() {
        return packageDTOList;
    }

    @Override
    public List<? extends ClassDTO> getClassDTOList() {
        return classDTOList;
    }

    @Override
    public List<? extends ImportDTO> getImportDTOList() {
        return importDTOList;
    }

    @Override
    public List<? extends ExpressionDTO> getExpressionDTOList() {
        return expressionDTOList;
    }

    @Override
    public List<? extends MemberVariableDeclarationDTO> getMemberVariableDeclarationDTOList() {
        return memberVariableDeclarationDTOList;
    }

    @Override
    public List<? extends MethodDeclarationDTO> getMethodDeclarationDTOList() {
        return methodDeclarationDTOList;
    }

    @Override
    public List<? extends MethodCallExprDTO> getMethodCallExprDTOList() {
        return methodCallExprDTOList;
    }

    @Override
    public List<? extends StmtVariableDeclarationDTO> getStmtVariableDeclarationDTOList() {
        return stmtVariableDeclarationDTOList;
    }

    @Override
    public List<? extends ArgumentDTO> getArgumentDTOList() {
        return argumentDTOList;
    }

    @Override
    public List<? extends ParameterDTO> getParameterDTOList() {
        return parameterDTOList;
    }

    @Override
    public List<? extends ReturnMapperDTO> getReturnMapperDTOList() {
        return returnMapperDTOList;
    }

    @Override
    public List<? extends IfStmtDTO> getIfStmtDTOList() {
        return ifStmtDTOList;
    }

    @Override
    public List<? extends ForStmtDTO> getForStmtDTOList() {
        return forStmtDTOList;
    }

    @Override
    public List<? extends WhileStmtDTO> getWhileStmtDTOList() {
        return whileStmtDTOList;
    }

    @Override
    public List<? extends SwitchStmtDTO> getSwitchStmtDTOList() {
        return switchStmtDTOList;
    }


    @Override
    public void clear() {
        symbolIds.clear();
        symbolReferenceDTOList.clear();
        argumentDTOList.clear();
        blockDTOList.clear();
        packageDTOList.clear();
        classDTOList.clear();
        importDTOList.clear();
        parameterDTOList.clear();
        returnMapperDTOList.clear();
        methodDeclarationDTOList.clear();
        methodCallExprDTOList.clear();
        stmtVariableDeclarationDTOList.clear();
        memberVariableDeclarationDTOList.clear();
        expressionDTOList.clear();
        fullQualifiedNameDTOList.clear();
        ifStmtDTOList.clear();
        forStmtDTOList.clear();
        whileStmtDTOList.clear();
        switchStmtDTOList.clear();
    }

    public void parseFile() throws Exception {
        try {
            File dir = new File(symbolDataPath);
            String[] fileList = dir.list();
            if (fileList==null || fileList.length==0) {
                throw new FileNotFoundException("No files in the directory: " + symbolDataPath);
            }

            for (String fileName : fileList) {
                if (fileName.endsWith(".csv")) {
                    String symbolType = fileName.substring(0, fileName.length() - 4);
                    generateSymbol(Class.forName("org.kryun.symbol.model.dto." + symbolType));
                }
            }
        } catch (Exception e) {
            logger.error("Error in CSVParser.parseFile()", e);
            throw new Exception("Error in CSVParser.parseFile()");
        }
    }

    public void generateSymbol(Class<?> classType) throws Exception {
        String symbolType = classType.getSimpleName();
        String dataPath = symbolDataPath + "/" + symbolType + ".csv";
        boolean hasBuilder = hasBuilder(classType);

        try (Reader in = new FileReader(dataPath)) {
            Iterator<CSVRecord> iterator = CSVFormat.EXCEL.builder().setSkipHeaderRecord(false).build().parse(in).iterator();
            List<String> headerList = iterator.next().toList();

            while (iterator.hasNext()) {
                CSVRecord record = iterator.next();
                Object instance = hasBuilder ? createInstanceByBuilder(classType, headerList, record):createInstanceByConstructor(classType, headerList, record);
                addToList(symbolType, instance);
            }

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static boolean hasBuilder(Class<?> classType) {
        try {
            Class.forName(classType.getName() + "$" + classType.getSimpleName() + "Builder");
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private Object createInstanceByConstructor(Class<?> classType, List<String> headerList, CSVRecord record) throws Exception {
        Long lastId = -1L;
        String symbolType = classType.getSimpleName();
        Object instance = classType.getDeclaredConstructor().newInstance();
        for (int i = 0; i < headerList.size(); i++) {
            String header = headerList.get(i);
            Field field = getField(classType, header);
            String value = record.get(i);
            if (value.isEmpty())
                continue;

            if (field==null) {
                System.out.println("Field not found: " + header);
                continue;
            }
            // id인 경우 저장.
            if (field.getName().equals(getIdentifierName(symbolType))) {
                lastId = Long.parseLong(value);
            }

            if (field.getType().equals(Position.class)) {
                // position인 경우
                JsonObject jsonObject = JsonParser.parseString(value).getAsJsonObject();
                Position position = new Position(jsonObject.get("beginLine").getAsInt(), jsonObject.get("beginColumn").getAsInt(), jsonObject.get("endLine").getAsInt(), jsonObject.get("endColumn").getAsInt());
                setFieldValue(instance, field, position);
            } else if (field.getType().equals(ExpressionRelationEnum.class)) {
                // expressionRelation인 경우
                setFieldValue(instance, field, ExpressionRelationEnum.getEnumBySignature(value));
            } else {
                if (field.getName().equals("symbolStatusId"))
                    setFieldValue(instance, field, String.valueOf(symbolStatusId));
                else
                    setFieldValue(instance, field, value);
            }
        }

        symbolIds.put(symbolType, lastId);

        return instance;
    }

    private Object createInstanceByBuilder(Class<?> classType, List<String> headerList, CSVRecord record) throws Exception {
        Long lastId = -1L;
        System.out.println(classType.getName());
        Class<?> builderClass = Class.forName(classType.getName() + "$" + classType.getSimpleName() + "Builder");
        Object builderInstance = classType.getMethod("builder").invoke(null);
        for (int i = 0; i < headerList.size(); i++) {
            String header = headerList.get(i);
            Field field = getField(classType, header);
            String value = record.get(i);
            if (value.isEmpty())
                continue;

            if (field==null) {
                System.out.println("Field not found: " + header);
                continue;
            }
            // id인 경우 저장.
            if (field.getName().equals(getIdentifierName(classType.getSimpleName()))) {
                lastId = Long.parseLong(value);
            }

            if (field.getType().equals(Position.class)) {
                // position인 경우
                JsonObject jsonObject = JsonParser.parseString(value).getAsJsonObject();
                Position position = new Position(jsonObject.get("beginLine").getAsInt(), jsonObject.get("beginColumn").getAsInt(), jsonObject.get("endLine").getAsInt(), jsonObject.get("endColumn").getAsInt());
                builderClass.getMethod("position", Position.class).invoke(builderInstance, position);
            } else if (field.getType().equals(ExpressionRelationEnum.class)) {
                // expressionRelation인 경우
                builderClass.getMethod("expressionRelation", ExpressionRelationEnum.class).invoke(builderInstance, ExpressionRelationEnum.getEnumBySignature(value));
            } else {
                if (field.getName().equals("symbolStatusId"))
                    builderClass.getMethod("symbolStatusId", String.class).invoke(builderInstance, String.valueOf(symbolStatusId));
                else
                    builderClass.getMethod(field.getName(), field.getType()).invoke(builderInstance, castStringToType(value, field.getType()));
            }
        }
        symbolIds.put(classType.getSimpleName(), lastId);
        return builderClass.getMethod("build").invoke(builderInstance);
    }

    public void addToList(String symbolType, Object instance) {
        switch (symbolType) {
            case "SymbolReferenceDTO" -> symbolReferenceDTOList.add((SymbolReferenceDTO) instance);
            case "ArgumentDTO" -> argumentDTOList.add((ArgumentDTO) instance);
            case "BlockDTO" -> blockDTOList.add((BlockDTO) instance);
            case "PackageDTO" -> packageDTOList.add((PackageDTO) instance);
            case "ClassDTO" -> classDTOList.add((ClassDTO) instance);
            case "ImportDTO" -> importDTOList.add((ImportDTO) instance);
            case "ParameterDTO" -> parameterDTOList.add((ParameterDTO) instance);
            case "ReturnMapperDTO" -> returnMapperDTOList.add((ReturnMapperDTO) instance);
            case "MethodDeclarationDTO" -> methodDeclarationDTOList.add((MethodDeclarationDTO) instance);
            case "MethodCallExprDTO" -> methodCallExprDTOList.add((MethodCallExprDTO) instance);
            case "StmtVariableDeclarationDTO" ->
                    stmtVariableDeclarationDTOList.add((StmtVariableDeclarationDTO) instance);
            case "MemberVariableDeclarationDTO" ->
                    memberVariableDeclarationDTOList.add((MemberVariableDeclarationDTO) instance);
            case "ExpressionDTO" -> expressionDTOList.add((ExpressionDTO) instance);
            case "FullQualifiedNameDTO" -> fullQualifiedNameDTOList.add((FullQualifiedNameDTO) instance);
            case "IfStmtDTO" -> ifStmtDTOList.add((IfStmtDTO) instance);
            case "ForStmtDTO" -> forStmtDTOList.add((ForStmtDTO) instance);
            case "WhileStmtDTO" -> whileStmtDTOList.add((WhileStmtDTO) instance);
            case "SwitchStmtDTO" -> switchStmtDTOList.add((SwitchStmtDTO) instance);
            default -> throw new IllegalArgumentException("Unsupported symbol type: " + symbolType);
        }
    }

    private static Field getField(Class<?> classType, String fieldName) {
        Class<?> clazz = classType;
        Field field = null;
        while (clazz!=null) {
            try {
                field = clazz.getDeclaredField(fieldName);
                return field;
            } catch (NoSuchFieldException e) {
                clazz = clazz.getSuperclass();
                if (!clazz.getPackage().equals(classType.getPackage())) {
                    clazz = null;
                }
            }
        }
        return field;
    }


    private static String getSetterMethodName(Field field) throws Exception {
        String fieldName = field.getName();
        String capitalizedFieldName = Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
        return "set" + capitalizedFieldName;
    }

    private static void setFieldValue(Object instance, Field field, String value) throws Exception {
        String setterMethodName = getSetterMethodName(field);
        Object castedValue = castStringToType(value, field.getType());
        instance.getClass().getMethod(setterMethodName, field.getType()).invoke(instance, castedValue);
    }

    private static void setFieldValue(Object instance, Field field, Position position) throws Exception {
        String setterMethodName = getSetterMethodName(field);
        instance.getClass().getMethod(setterMethodName, Position.class).invoke(instance, position);
    }

    private static void setFieldValue(Object instance, Field field, ExpressionRelationEnum expressionRelationEnum) throws Exception {
        String setterMethodName = getSetterMethodName(field);
        instance.getClass().getMethod(setterMethodName, ExpressionRelationEnum.class).invoke(instance, expressionRelationEnum);
    }

    private static Object castStringToType(String value, Class<?> type) {
        if (type==int.class || type==Integer.class) {
            return Integer.parseInt(value);
        } else if (type==double.class || type==Double.class) {
            return Double.parseDouble(value);
        } else if (type==boolean.class || type==Boolean.class) {
            return Boolean.parseBoolean(value);
        } else if (type==long.class || type==Long.class) {
            return Long.parseLong(value);
        } else if (type==float.class || type==Float.class) {
            return Float.parseFloat(value);
        } else if (type==short.class || type==Short.class) {
            return Short.parseShort(value);
        } else if (type==byte.class || type==Byte.class) {
            return Byte.parseByte(value);
        } else if (type==char.class || type==Character.class) {
            return value.charAt(0);
        } else if (type==String.class) {
            return value;
        } else {
            throw new IllegalArgumentException("Unsupported field type: " + type.getName());
        }
    }

    private static String getIdentifierName(String classType) {
        return switch (classType) {
            case "ArgumentDTO" -> "argumentId";
            case "BlockDTO" -> "blockId";
            case "PackageDTO" -> "packageId";
            case "ClassDTO" -> "classId";
            case "ImportDTO" -> "importId";
            case "ParameterDTO" -> "parameterId";
            case "ReturnMapperDTO" -> "returnMapperId";
            case "MethodDeclarationDTO" -> "methodDeclarationId";
            case "MethodCallExprDTO" -> "methodCallExprId";
            case "StmtVariableDeclarationDTO", "MemberVariableDeclarationDTO" -> "variableId";
            case "ExpressionDTO" -> "expressionId";
            case "FullQualifiedNameDTO" -> "fullQualifiedNameId";
            case "SymbolReferenceDTO" -> "symbolReferenceId";
            case "IfStmtDTO" -> "ifStmttId";
            case "ForStmtDTO" -> "forStmtId";
            case "WhileStmtDTO" -> "whileStmtId";
            case "SwitchStmtDTO" -> "switchStmtId";
            default -> null;
        };
    }


}
