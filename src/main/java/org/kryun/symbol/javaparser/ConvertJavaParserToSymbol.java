package org.kryun.symbol.javaparser;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import org.kryun.symbol.javaparser.management.BlockManager;
import org.kryun.symbol.javaparser.management.ClassManager;
import org.kryun.symbol.javaparser.management.ExpressionManager;
import org.kryun.symbol.javaparser.management.FullQualifiedNameManager;
import org.kryun.symbol.javaparser.management.ImportManager;
import org.kryun.symbol.javaparser.management.MethodManager;
import org.kryun.symbol.javaparser.management.PackageManager;
import org.kryun.symbol.javaparser.management.SymbolReferenceManager;
import org.kryun.symbol.javaparser.management.VariableManager;
import org.kryun.symbol.javaparser.model.dto.JavaParserArgumentDTO;
import org.kryun.symbol.javaparser.model.dto.JavaParserBlockDTO;
import org.kryun.symbol.javaparser.model.dto.JavaParserClassDTO;
import org.kryun.symbol.javaparser.model.dto.JavaParserExpressionDTO;
import org.kryun.symbol.javaparser.model.dto.JavaParserImportDTO;
import org.kryun.symbol.javaparser.model.dto.JavaParserMemberVariableDeclarationDTO;
import org.kryun.symbol.javaparser.model.dto.JavaParserMethodCallExprDTO;
import org.kryun.symbol.javaparser.model.dto.JavaParserMethodDeclarationDTO;
import org.kryun.symbol.javaparser.model.dto.JavaParserPackageDTO;
import org.kryun.symbol.javaparser.model.dto.JavaParserParameterDTO;
import org.kryun.symbol.javaparser.model.dto.JavaParserReturnMapperDTO;
import org.kryun.symbol.javaparser.model.dto.JavaParserStmtVariableDeclarationDTO;
import org.kryun.symbol.model.dto.FullQualifiedNameDTO;
import org.kryun.symbol.model.dto.SymbolReferenceDTO;
import java.sql.Connection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.kryun.symbol.pkg.build.interfaces.SymbolContainer;

public class ConvertJavaParserToSymbol implements SymbolContainer {

    private final Long symbolStatusId;
    private final Boolean isDependency;
    private TypeResolver typeResolver;

    private final SymbolReferenceManager symbolReferenceManager = new SymbolReferenceManager();
    private final BlockManager blockManager = new BlockManager();
    private final PackageManager packageManager = new PackageManager();
    private final ImportManager importManager = new ImportManager();
    private final ClassManager classManager = new ClassManager();

    private final VariableManager variableManager = new VariableManager();
    private final MethodManager methodManager = new MethodManager();
    private final FullQualifiedNameManager fullQualifiedNameManager = new FullQualifiedNameManager();
    private final ExpressionManager expressionManager = new ExpressionManager();

    private Boolean hasPackage = false;

    public ConvertJavaParserToSymbol(Long symbolStatusId, Boolean isDependency) {
        this.isDependency = isDependency;
        this.symbolStatusId = symbolStatusId;
        this.typeResolver = new TypeResolver(symbolStatusId, fullQualifiedNameManager);
    }

    public ConvertJavaParserToSymbol(Long symbolStatusId, Boolean isDependency, Connection conn) {
        this.isDependency = isDependency;
        this.symbolStatusId = symbolStatusId;
        this.typeResolver = new TypeResolver(symbolStatusId, fullQualifiedNameManager, conn);
    }

    public List<SymbolReferenceDTO> getSymbolReferenceDTOList() {
        return symbolReferenceManager.getSymbolReferenceDTOList();
    }

    public List<JavaParserArgumentDTO> getArgumentDTOList() {
        return methodManager.getJavaParserArgumentDTOList();
    }

    public List<JavaParserBlockDTO> getBlockDTOList() {
        return blockManager.getBlockDTOList();
    }

    public List<JavaParserClassDTO> getClassDTOList() {
        return classManager.getClassDTOList();
    }

    public List<JavaParserPackageDTO> getPackageDTOList() {
        return packageManager.getPackageDTOList();
    }

    public List<JavaParserImportDTO> getImportDTOList() {
        return importManager.getImportDTOList();
    }

    public List<JavaParserParameterDTO> getParameterDTOList() {
        return methodManager.getJavaParserParameterDTOList();
    }

    public List<JavaParserReturnMapperDTO> getReturnMapperDTOList() {
        return methodManager.getJavaParserReturnMapperDTOList();
    }

    public List<JavaParserMemberVariableDeclarationDTO> getMemberVariableDeclarationDTOList() {
        return variableManager.getJavaParserMemberVariableDeclarationDTOList();
    }

    public List<JavaParserStmtVariableDeclarationDTO> getStmtVariableDeclarationDTOList() {
        return variableManager.getJavaParserStmtVariableDeclarationDTOList();
    }

    public List<JavaParserMethodDeclarationDTO> getMethodDeclarationDTOList() {
        return methodManager.getJavaParserMethodDeclarationDTOList();
    }

    public List<JavaParserMethodCallExprDTO> getMethodCallExprDTOList() {
        return methodManager.getJavaParserMethodCallExprDTOList();
    }

    public List<FullQualifiedNameDTO> getFullQualifiedNameDTOList() {
        return fullQualifiedNameManager.getFullQualifiedNameDTOList();
    }

    public List<JavaParserExpressionDTO> getExpressionDTOList() {
        return expressionManager.getExpressionDTOList();
    }

    public Map<String, Long> getSymbolIds() {
        Map<String, Long> symbolIds = new LinkedHashMap<>();
        symbolIds.putAll(symbolReferenceManager.getIdentifierMap());
        symbolIds.putAll(blockManager.getIdentifierMap());
        symbolIds.putAll(packageManager.getIdentifierMap());
        symbolIds.putAll(importManager.getIdentifierMap());
        symbolIds.putAll(classManager.getIdentifierMap());
        symbolIds.putAll(variableManager.getIdentifierMap());
        symbolIds.putAll(methodManager.getIdentifierMap());
        symbolIds.putAll(fullQualifiedNameManager.getIdentifierMap());
        symbolIds.putAll(expressionManager.getIdentifierMap());

        return symbolIds;
    }

    public void clear() {
        symbolReferenceManager.clear();
        blockManager.clear();
        packageManager.clear();
        importManager.clear();
        classManager.clear();
        variableManager.clear();
        methodManager.clear();
        fullQualifiedNameManager.clear();
        typeResolver.clear();
        expressionManager.clear();
    }

    public void visit(CompilationUnit cu, String srcPath) {
        Map<String, JavaParserImportDTO> typeImportMapper = new HashMap<>();
        String nodeType = cu.getMetaModel().getTypeName();
        SymbolReferenceDTO symbolReferenceDTO = symbolReferenceManager
                .buildSymbolReference(symbolStatusId, srcPath);
        Long symbolReferenceId = symbolReferenceDTO.getSymbolReferenceId();
        JavaParserBlockDTO rootBlock = blockManager.buildBlock(1, null, nodeType, cu, symbolReferenceId);

        visitAndBuild(cu, rootBlock, isDependency, typeImportMapper);
    }

    private void visitAndBuild(Node node, JavaParserBlockDTO parentJavaParserBlockDTO, Boolean isDependency,
                               Map<String, JavaParserImportDTO> typeImportMapper) {
        JavaParserBlockDTO javaParserBlockDTO = parentJavaParserBlockDTO;
        String nodeType = node.getMetaModel().getTypeName();

        if (nodeType.equals("PackageDeclaration")) {
            packageManager.buildPackage(javaParserBlockDTO.getBlockId(), node);
            hasPackage = true;
        } else if (nodeType.equals("ImportDeclaration")) {
            JavaParserImportDTO javaParserImportDTO = importManager.buildImport(javaParserBlockDTO.getBlockId(), node);
            if (!(javaParserImportDTO.getIsAsterisk() || javaParserImportDTO.getIsStatic())) {
                // static도 아니고, asterisk도 아닌 경우
                typeImportMapper.put(javaParserImportDTO.getClassName(), javaParserImportDTO);
            }
        } else if (nodeType.equals("ClassOrInterfaceDeclaration")) {
            JavaParserClassDTO javaParserClassDTO = classManager.buildClass(javaParserBlockDTO, packageManager.getLastId(), node);

            javaParserBlockDTO = blockManager.buildBlock(parentJavaParserBlockDTO.getDepth() + 1, parentJavaParserBlockDTO.getBlockId(), nodeType,
                    node, parentJavaParserBlockDTO.getSymbolReferenceId());

            typeResolver.resolveSymbolAndUpdateFQNDTO(node, nodeType, javaParserClassDTO, typeImportMapper);

        } else if (nodeType.equals("EnumDeclaration")) {
            JavaParserClassDTO enumDTO = classManager.buildEnum(javaParserBlockDTO, packageManager.getLastId(), node);

            javaParserBlockDTO = blockManager.buildBlock(parentJavaParserBlockDTO.getDepth() + 1, parentJavaParserBlockDTO.getBlockId(), nodeType,
                    node, parentJavaParserBlockDTO.getSymbolReferenceId());

            typeResolver.resolveSymbolAndUpdateFQNDTO(node, nodeType, enumDTO, typeImportMapper);

        } else if (nodeType.equals("AnnotationDeclaration") || nodeType.equals(
                "RecordDeclaration") || nodeType.equals("SwitchStmt")
                || nodeType.equals(
                "SwitchEntry")) {
            javaParserBlockDTO = blockManager.buildBlock(parentJavaParserBlockDTO.getDepth() + 1, parentJavaParserBlockDTO.getBlockId(), nodeType,
                    node, parentJavaParserBlockDTO.getSymbolReferenceId());
        } else if (nodeType.equals("BlockStmt")) {

            String blockStmtType = nodeType;

            Optional<Node> parentNode = node.getParentNode();
            // 부모가 존재하는 경우
            if (parentNode.isPresent()) {
                Node parentNodeValue = parentNode.get();
                String parentNodeType = parentNodeValue.getMetaModel().getTypeName();
                // Else 판별
                if (parentNodeType.equals("IfStmt")) {
                    List<Node> siblings = parentNodeValue.getChildNodes();
                    if (node.equals(siblings.get(siblings.size() - 1))) {
                        blockStmtType = "ElseStmt";
                    }
                } else {
                    blockStmtType = parentNodeType;
                }
            }

            if (blockStmtType.equals("SwitchEntry")) {
                // SwitchEntry안에 별도의 Block이 존재하는 경우 중복 생성 방지

            } else {
                javaParserBlockDTO = blockManager.buildBlock(parentJavaParserBlockDTO.getDepth() + 1, parentJavaParserBlockDTO.getBlockId(),
                        blockStmtType,
                        node, parentJavaParserBlockDTO.getSymbolReferenceId());
            }
        }
        // block이 없는 if문 처리
        else if (nodeType.equals("ExpressionStmt")) {
            Node parentNode = node.getParentNode().get();
            String parentNodeType = parentNode.getMetaModel().getTypeName();

            if (parentNodeType.equals("IfStmt")) {
                String type = "IfStmt";
                List<Node> siblings = parentNode.getChildNodes();
                long blockStmtCnt = siblings.stream()
                        .filter(x -> x.getMetaModel().getTypeName().equals("BlockStmt"))
                        .count();
                long expressionStmtCnt = siblings.stream()
                        .filter(x -> x.getMetaModel().getTypeName().equals("ExpressionStmt"))
                        .count();
                long ifStmtCnt = siblings.stream()
                        .filter(x -> x.getMetaModel().getTypeName().equals("IfStmt"))
                        .count();

                if ((blockStmtCnt + expressionStmtCnt + ifStmtCnt) >= 2
                        && node.equals(siblings.get(siblings.size() - 1))) {
                    type = "ElseStmt";
                }
                javaParserBlockDTO = blockManager.buildBlock(parentJavaParserBlockDTO.getDepth() + 1, parentJavaParserBlockDTO.getBlockId(),
                        type, node, parentJavaParserBlockDTO.getSymbolReferenceId());
            }
        }
        // 클래스 바로 아래에서 변수를 선언하는 멤버 필드
        else if (nodeType.equals("FieldDeclaration")) {
            JavaParserClassDTO belongedJavaParserClassDTO = classManager.getClassDTOList()
                    .get(classManager.getClassDTOList().size() - 1);
            List<JavaParserMemberVariableDeclarationDTO> mvdDtoList = variableManager.buildVariableDeclInMemberField(javaParserBlockDTO,
                    belongedJavaParserClassDTO.getClassId(), node, expressionManager);

            JavaParserMemberVariableDeclarationDTO firstMvdDTO = mvdDtoList.get(0);
            typeResolver.resolveSymbolAndUpdateFQNDTO(node, nodeType, firstMvdDTO, typeImportMapper);
            if(mvdDtoList.size() > 1 && firstMvdDTO.getFullQualifiedNameId() != null) {
                for (int i = 1; i < mvdDtoList.size(); i++) {
                    JavaParserMemberVariableDeclarationDTO mvdDto = mvdDtoList.get(i);
                    mvdDto.setFullQualifiedNameId(firstMvdDTO.getFullQualifiedNameId());
                    typeResolver.resolveSymbolAndUpdateFQNDTO(node, nodeType, mvdDto, typeImportMapper);
                }
            }

        }
        // 함수 내에서 선언하는 변수
        else if (nodeType.equals("VariableDeclarationExpr")) {
            List<JavaParserStmtVariableDeclarationDTO> stmtDtoList = variableManager.buildVariableDeclInMethod(javaParserBlockDTO, node, expressionManager);
            JavaParserStmtVariableDeclarationDTO firstStmtDTO = stmtDtoList.get(0);
            typeResolver.resolveSymbolAndUpdateFQNDTO(node, nodeType, firstStmtDTO, typeImportMapper);
            if(stmtDtoList.size() > 1 && firstStmtDTO.getFullQualifiedNameId() != null) {
                for (int i = 1; i < stmtDtoList.size(); i++) {
                    JavaParserStmtVariableDeclarationDTO stmtDto = stmtDtoList.get(i);
                    stmtDto.setFullQualifiedNameId(firstStmtDTO.getFullQualifiedNameId());
                    typeResolver.resolveSymbolAndUpdateFQNDTO(node, nodeType, stmtDto, typeImportMapper);
                }
            }

        } else if (nodeType.equals("MethodDeclaration") || nodeType.equals(
                "ConstructorDeclaration")) {
            // 내부에 BlockStmt 가 존재하여 별도의 Block 을 생성하지는 않음

            // 함수 및 생성자 선언 시 build
            JavaParserClassDTO belongedJavaParserClassDTO = classManager.getClassDTOList()
                    .get(classManager.getClassDTOList().size() - 1);
            JavaParserMethodDeclarationDTO mdDto = methodManager.buildMethodDeclaration(javaParserBlockDTO, belongedJavaParserClassDTO.getClassId(),
                    node);
            typeResolver.resolveSymbolAndUpdateFQNDTO(node, nodeType, mdDto, typeImportMapper);

        } else if (nodeType.equals("MethodCallExpr")) {
            JavaParserMethodCallExprDTO mceDto = methodManager.buildMethodCallExpr(javaParserBlockDTO, node, expressionManager);
            if (!isDependency) {
                // Dependency가 아닌 경우에만 동작
                typeResolver.resolveSymbolAndUpdateFQNDTO(node, nodeType, mceDto, typeImportMapper);
            }

        } else if (nodeType.equals("AssignExpr")) {
            // test
            expressionManager.buildExpressionRecursively(null, null,
                    javaParserBlockDTO.getBlockId(), (Expression) node);
        } else if (nodeType.equals("ObjectCreationExpr")) {
            ObjectCreationExpr objectCreationExpr = (ObjectCreationExpr) node;
            // 내부에 초기화 블록이 존재할 때만 생성하도록 추가
            if (objectCreationExpr.getAnonymousClassBody().isPresent()) {
                expressionManager.buildExpressionRecursively(null, null,
                        javaParserBlockDTO.getBlockId(), (Expression) node);
                javaParserBlockDTO = blockManager.buildBlock(parentJavaParserBlockDTO.getDepth() + 1, parentJavaParserBlockDTO.getBlockId(),
                        nodeType, node, parentJavaParserBlockDTO.getSymbolReferenceId());
            }
        }

        expressionManager.deleteExpressionDTO(node.hashCode());

        List<Node> childNodes = node.getChildNodes();
        for (Node childNode : childNodes) {
            if (!childNode.getMetaModel().getTypeName().equals("SimpleName") ||
                    !childNode.getMetaModel().getTypeName().equals("Modifier")) {
                visitAndBuild(childNode, javaParserBlockDTO, isDependency, typeImportMapper);
            }
        }
    }
}
