package org.kryun.symbol.javaparser.management;

import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import java.util.logging.Logger;
import org.kryun.symbol.javaparser.model.dto.JavaParserBlockDTO;
import org.kryun.symbol.javaparser.model.dto.JavaParserClassDTO;
import org.kryun.symbol.model.dto.Position;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.kryun.symbol.pkg.IdentifierGenerator;

public class ClassManager {

    private final java.util.logging.Logger logger = Logger.getLogger(BlockManager.class.getName());

    private final List<JavaParserClassDTO> javaParserClassDTOList;

    public ClassManager() {
        this.javaParserClassDTOList = new ArrayList<>();
    }

    public List<JavaParserClassDTO> getClassDTOList() {
        return this.javaParserClassDTOList;
    }

    public void clear() {
        this.javaParserClassDTOList.clear();
    }

    private final IdentifierGenerator classIdGenerator = new IdentifierGenerator("class");

    public Map<String, Long> getIdentifierMap() {
        Map<String, Long> identifierMap = new HashMap<>();
        identifierMap.put(classIdGenerator.getSymbolType(), classIdGenerator.getLastId());
        return identifierMap;
    }


    public JavaParserClassDTO buildClass(JavaParserBlockDTO javaParserBlockDTO, Long packageId, Node node) {
        JavaParserClassDTO javaParserClassDTO = new JavaParserClassDTO();
        Long blockId = javaParserBlockDTO.getBlockId();

        boolean isInner = false;
        String outerClassName = "";

        if (javaParserBlockDTO.getBlockType().equals("ClassOrInterfaceDeclaration") || javaParserBlockDTO.getBlockType()
                .equals("EnumDeclaration")) {
            isInner = true;
            outerClassName = javaParserClassDTOList.get(javaParserClassDTOList.size() - 1).getName();
        }

        ClassOrInterfaceDeclaration classOrInterfaceDeclaration = (ClassOrInterfaceDeclaration) node;
        boolean isImplemented = false;
        String implementClass = "";
        String modifierKeyword = "";
        String accessModifierKeyword = "";
        String className = classOrInterfaceDeclaration.getNameAsString();
        String classType;

        NodeList<Modifier> modifiers = classOrInterfaceDeclaration.getModifiers();
        for (Modifier modifier : modifiers) {
            // 접근 제어자 분별
            if (modifier.getKeyword().equals(Modifier.Keyword.DEFAULT) ||
                    modifier.getKeyword().equals(Modifier.Keyword.PUBLIC) ||
                    modifier.getKeyword().equals(Modifier.Keyword.PROTECTED) ||
                    modifier.getKeyword().equals(Modifier.Keyword.PRIVATE)) {
                accessModifierKeyword = modifier.getKeyword().asString();
            } else {
                modifierKeyword = modifier.getKeyword().asString();
            }
        }

        // 인터페이스 구현체 여부
        NodeList<ClassOrInterfaceType> implementedTypes = classOrInterfaceDeclaration.getImplementedTypes();
        NodeList<ClassOrInterfaceType> extendedTypes = classOrInterfaceDeclaration.getExtendedTypes();
        List<String> implementExtendClassList = new ArrayList<>();

        if (!implementedTypes.isEmpty()) {
            isImplemented = true;

            List<String> implementClassList = implementedTypes.stream()
                    .map(ClassOrInterfaceType::toString).toList();
            implementExtendClassList.addAll(implementClassList);
        }

        if (!extendedTypes.isEmpty()) {
            isImplemented = true;
            List<String> extendsClassList = extendedTypes.stream()
                    .map(ClassOrInterfaceType::toString).toList();
            implementExtendClassList.addAll(extendsClassList);
        }
        implementClass = String.join(", ", implementExtendClassList);

        // 인터페이스 클래스 여부
        if (classOrInterfaceDeclaration.isInterface()) {
            classType = "interface";
        } else {
            classType = "class";
        }

        javaParserClassDTO.setClassId(classIdGenerator.nextId());
        javaParserClassDTO.setBlockId(blockId);
//        javaParserClassDTO.setOwnBlockProperties(ownBlock);
        javaParserClassDTO.setPackageId(packageId);
        javaParserClassDTO.setName(className);
        javaParserClassDTO.setModifier(modifierKeyword);
        javaParserClassDTO.setAccessModifier(accessModifierKeyword);
        javaParserClassDTO.setType(classType);
        javaParserClassDTO.setIsImplemented(isImplemented);
        javaParserClassDTO.setImplementClass(implementClass);
        javaParserClassDTO.setPosition(
                new Position(
                        node.getRange().get().begin.line,
                        node.getRange().get().begin.column,
                        node.getRange().get().end.line,
                        node.getRange().get().end.column));

        // Todo. Class 간에 관계(상속, 구현, Inner)를 위한 Table을 따로 생성할 것
        javaParserClassDTO.setIsInner(isInner);
        javaParserClassDTO.setOuterClass(outerClassName);

        javaParserClassDTOList.add(javaParserClassDTO);

        return javaParserClassDTO;
    }

    public JavaParserClassDTO buildEnum(JavaParserBlockDTO javaParserBlockDTO, Long packageId, Node node) {
        JavaParserClassDTO javaParserClassDTO = new JavaParserClassDTO();
        Long blockId = javaParserBlockDTO.getBlockId();
        boolean isInner = false;
        String outerClassName = "";

        if (javaParserBlockDTO.getBlockType().equals("ClassOrInterfaceDeclaration") || javaParserBlockDTO.getBlockType()
                .equals("EnumDeclaration")) {
            isInner = true;
            outerClassName = javaParserClassDTOList.get(javaParserClassDTOList.size() - 1).getName();
        }

        EnumDeclaration enumDeclaration = (EnumDeclaration) node;
        boolean isImplemented = false;
        String implementClass = "";
        String modifierKeyword = "";
        String accessModifierKeyword = "";
        String className = enumDeclaration.getNameAsString();

        NodeList<Modifier> modifiers = enumDeclaration.getModifiers();
        for (Modifier modifier : modifiers) {
            // 접근 제어자 분별
            if (modifier.getKeyword().equals(Modifier.Keyword.DEFAULT) ||
                    modifier.getKeyword().equals(Modifier.Keyword.PUBLIC) ||
                    modifier.getKeyword().equals(Modifier.Keyword.PROTECTED) ||
                    modifier.getKeyword().equals(Modifier.Keyword.PRIVATE)) {
                accessModifierKeyword = modifier.getKeyword().asString();
            } else {
                modifierKeyword = modifier.getKeyword().asString();
            }
        }

        // NodeList<EnumConstantDeclaration> enumConstantDeclarations =
        // enumDeclaration.getEntries();

        // 인터페이스 구현체 여부
        NodeList<ClassOrInterfaceType> implementedTypes = enumDeclaration.getImplementedTypes();

        if (!implementedTypes.isEmpty()) {
            isImplemented = true;

            List<String> implementClassList = implementedTypes.stream()
                    .map(classOrInterfaceTypeNode -> classOrInterfaceTypeNode.toString())
                    .collect(Collectors.toList());

            implementClass = String.join(", ", implementClassList);
        }

        javaParserClassDTO.setClassId(classIdGenerator.nextId());
        javaParserClassDTO.setBlockId(blockId);
//        javaParserClassDTO.setOwnBlockProperties(ownBlock);
        javaParserClassDTO.setPackageId(packageId);
        javaParserClassDTO.setName(className);
        javaParserClassDTO.setModifier(modifierKeyword);
        javaParserClassDTO.setAccessModifier(accessModifierKeyword);
        // enumConstantDeclarations 를 class dto 에 넣을 만한 멤버 변수가 필요함.
        javaParserClassDTO.setType("enum");
        javaParserClassDTO.setIsImplemented(isImplemented);
        javaParserClassDTO.setImplementClass(implementClass);
        javaParserClassDTO.setPosition(
                new Position(
                        node.getRange().get().begin.line,
                        node.getRange().get().begin.column,
                        node.getRange().get().end.line,
                        node.getRange().get().end.column));
        javaParserClassDTO.setIsInner(isInner);
        javaParserClassDTO.setOuterClass(outerClassName);

        javaParserClassDTOList.add(javaParserClassDTO);

        return javaParserClassDTO;
    }

    public JavaParserClassDTO findClassDTOByBlock(JavaParserBlockDTO blockDTO) {
        for (int i = javaParserClassDTOList.size() - 1; i >= 0; i--) {
            JavaParserClassDTO classDTO = javaParserClassDTOList.get(i);
            if (classDTO.getOwnBlock().equals(blockDTO)) {
                return classDTO;
            }
            // 현재 파일을 벗어난 것으로 간주
            if (!classDTO.getOwnBlock().getSymbolReferenceId().equals(blockDTO.getSymbolReferenceId())) {
                break;
            }
        }
        // Todo. 못찾아서 마지막 class를 반환하는 경우 있나 검증 필요
        logger.warning("cannot find classDTO by block:: " + blockDTO.toString());
        return javaParserClassDTOList.get(javaParserClassDTOList.size() - 1);
    }
}
