package org.kryun.symbol.javaparser.management;

import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.nodeTypes.NodeWithImplements;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import org.kryun.symbol.javaparser.model.dto.JavaParserBlockDTO;
import org.kryun.symbol.javaparser.model.dto.JavaParserClassDTO;
import org.kryun.symbol.model.dto.Position;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.kryun.symbol.pkg.IdentifierGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClassManager {

    private final Logger logger = LoggerFactory.getLogger(BlockManager.class.getName());

    private final List<JavaParserClassDTO> javaParserClassDTOList;
    private final BlockManager blockManager;

    public ClassManager(BlockManager blockManager) {
        this.blockManager = blockManager;
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

    public JavaParserClassDTO buildClassOrEnum(JavaParserBlockDTO parentBlockDTO, Long packageId, Node node) {
        boolean isInner = false;
        String className;
        String outerClassName = "";
        String classType;
        boolean isImplemented = false;
        List<String> modifierList = new ArrayList<>();
        List<String> accessModifierList = new ArrayList<>();
        List<String> implementExtendClassList = new ArrayList<>();


        if (parentBlockDTO.getBlockType().equals("ClassOrInterfaceDeclaration") || parentBlockDTO.getBlockType().equals("EnumDeclaration")) {
            isInner = true;
            outerClassName = findClassDTOByBlock(parentBlockDTO).getName();
        }

        TypeDeclaration<?> typeDeclaration = (TypeDeclaration<?>) node;

        className = typeDeclaration.getNameAsString();

        NodeList<Modifier> modifiers = typeDeclaration.getModifiers();
        modifiers.forEach(modifier -> {
            if (modifier.getKeyword().equals(Modifier.Keyword.DEFAULT) ||
                    modifier.getKeyword().equals(Modifier.Keyword.PUBLIC) ||
                    modifier.getKeyword().equals(Modifier.Keyword.PROTECTED) ||
                    modifier.getKeyword().equals(Modifier.Keyword.PRIVATE)) {
                accessModifierList.add(modifier.getKeyword().asString());
            } else {
                modifierList.add(modifier.getKeyword().asString());
            }
        });

        NodeWithImplements<?> nodeWithImplements = (NodeWithImplements<?>) node;
        nodeWithImplements.getImplementedTypes().forEach(classOrInterfaceType -> {
            implementExtendClassList.add(classOrInterfaceType.toString());
        });

        if (node instanceof ClassOrInterfaceDeclaration) {
            ClassOrInterfaceDeclaration classOrInterfaceDeclaration = (ClassOrInterfaceDeclaration) node;
            if (classOrInterfaceDeclaration.isInterface()) {
                classType = "interface";
            } else {
                classType = "class";
            }

            classOrInterfaceDeclaration.getExtendedTypes().forEach(classOrInterfaceType -> {
                implementExtendClassList.add(classOrInterfaceType.toString());
            });

        } else {
            classType = "enum";
        }

        if (!implementExtendClassList.isEmpty()) {
            isImplemented = true;
        }


        JavaParserClassDTO javaParserClassDTO = JavaParserClassDTO.builder()
                .classId(classIdGenerator.nextId())
                .blockId(parentBlockDTO.getBlockId())
                .packageId(packageId)
                .name(className)
                .modifier(String.join(" ", modifierList))
                .accessModifier(String.join(" ", accessModifierList))
                .type(classType)
                .isImplemented(isImplemented)
                .implementClass(String.join(", ", implementExtendClassList))
                .position(Position.getPositionByNode(node))
                .isInner(isInner)
                .outerClass(outerClassName)
                .build();

        buildAndSetOwnBlockProperties(javaParserClassDTO, parentBlockDTO, node);

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
        // Todo. ObjectCreationExpr 내부의 MethodDeclaration은 부모 class를 찾을 수 없음.
        logger.warn("cannot find classDTO by block:: " + blockDTO.toString());
        return javaParserClassDTOList.get(javaParserClassDTOList.size() - 1);
    }

    private void buildAndSetOwnBlockProperties(JavaParserClassDTO javaParserClassDTO, JavaParserBlockDTO parentBlock, Node node) {
        JavaParserBlockDTO ownBlock = blockManager.buildBlock(parentBlock, node);
        javaParserClassDTO.setOwnBlockProperties(ownBlock);
    }
}
