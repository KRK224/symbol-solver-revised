package org.kryun.symbol.javaparser;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.nodeTypes.NodeWithIdentifier;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.type.Type;
import org.kryun.symbol.javaparser.management.BlockManager;
import org.kryun.symbol.javaparser.management.ClassManager;
import org.kryun.symbol.javaparser.management.ExpressionManager;
import org.kryun.symbol.javaparser.management.ForStmtManager;
import org.kryun.symbol.javaparser.management.FullQualifiedNameManager;
import org.kryun.symbol.javaparser.management.IfStmtManager;
import org.kryun.symbol.javaparser.management.ImportManager;
import org.kryun.symbol.javaparser.management.MethodManager;
import org.kryun.symbol.javaparser.management.PackageManager;
import org.kryun.symbol.javaparser.management.SwitchStmtManager;
import org.kryun.symbol.javaparser.management.SymbolReferenceManager;
import org.kryun.symbol.javaparser.management.VariableManager;
import org.kryun.symbol.javaparser.management.WhileStmtManager;
import org.kryun.symbol.javaparser.model.dto.JavaParserArgumentDTO;
import org.kryun.symbol.javaparser.model.dto.JavaParserBlockDTO;
import org.kryun.symbol.javaparser.model.dto.JavaParserClassDTO;
import org.kryun.symbol.javaparser.model.dto.JavaParserExpressionDTO;
import org.kryun.symbol.javaparser.model.dto.JavaParserForStmtDTO;
import org.kryun.symbol.javaparser.model.dto.JavaParserIfStmtDTO;
import org.kryun.symbol.javaparser.model.dto.JavaParserImportDTO;
import org.kryun.symbol.javaparser.model.dto.JavaParserMemberVariableDeclarationDTO;
import org.kryun.symbol.javaparser.model.dto.JavaParserMethodCallExprDTO;
import org.kryun.symbol.javaparser.model.dto.JavaParserMethodDeclarationDTO;
import org.kryun.symbol.javaparser.model.dto.JavaParserPackageDTO;
import org.kryun.symbol.javaparser.model.dto.JavaParserParameterDTO;
import org.kryun.symbol.javaparser.model.dto.JavaParserReturnMapperDTO;
import org.kryun.symbol.javaparser.model.dto.JavaParserStmtVariableDeclarationDTO;
import org.kryun.symbol.javaparser.model.dto.JavaParserSwitchStmtDTO;
import org.kryun.symbol.javaparser.model.dto.JavaParserWhileStmtDTO;
import org.kryun.symbol.model.dto.FullQualifiedNameDTO;
import org.kryun.symbol.model.dto.Position;
import org.kryun.symbol.model.dto.SymbolReferenceDTO;
import java.sql.Connection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.kryun.symbol.pkg.LastSymbolDetector;
import org.kryun.symbol.pkg.builder.interfaces.SymbolContainer;

public class ConvertJavaParserToSymbol implements SymbolContainer {

    private final Long symbolStatusId;
    private final Boolean isDependency;
    private final LastSymbolDetector lastSymbolDetector = new LastSymbolDetector();


    private final SymbolReferenceManager symbolReferenceManager = new SymbolReferenceManager();
    private final BlockManager blockManager = new BlockManager();
    private final PackageManager packageManager = new PackageManager();
    private final ImportManager importManager = new ImportManager();
    private final FullQualifiedNameManager fullQualifiedNameManager = new FullQualifiedNameManager();
    private final ExpressionManager expressionManager = new ExpressionManager();

    private final ClassManager classManager = new ClassManager(blockManager);
    private final VariableManager variableManager = new VariableManager(expressionManager);
    private final IfStmtManager ifStmtManager = new IfStmtManager(blockManager, expressionManager);
    private final ForStmtManager forStmtManager = new ForStmtManager(blockManager, expressionManager);
    private final WhileStmtManager whileStmtManager = new WhileStmtManager(blockManager, expressionManager);
    private final SwitchStmtManager switchStmtManager = new SwitchStmtManager(blockManager, expressionManager);

    private final MethodManager methodManager;
    private final TypeResolver typeResolver;


    private Boolean hasPackage = false;

    public ConvertJavaParserToSymbol(Long symbolStatusId, Boolean isDependency) {
        this.isDependency = isDependency;
        this.symbolStatusId = symbolStatusId;
        this.methodManager = new MethodManager(blockManager, expressionManager, isDependency);
        this.typeResolver = new TypeResolver(symbolStatusId, fullQualifiedNameManager);
    }

    public ConvertJavaParserToSymbol(Long symbolStatusId, Boolean isDependency, Connection conn) {
        this.isDependency = isDependency;
        this.symbolStatusId = symbolStatusId;
        this.methodManager = new MethodManager(blockManager, expressionManager, isDependency);
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

    public List<JavaParserIfStmtDTO> getIfStmtDTOList() {
        return ifStmtManager.getIfStmtDTOList();
    }

    public List<JavaParserForStmtDTO> getForStmtDTOList() {
        return forStmtManager.getForStmtDTOList();
    }

    public List<JavaParserWhileStmtDTO> getWhileStmtDTOList() {
        return whileStmtManager.getWhileStmtDTOList();
    }

    public List<JavaParserSwitchStmtDTO> getSwitchStmtDTOList() {
        return switchStmtManager.getSwitchStmtDTOList();
    }

    public String printLastSymbol() {
        // 테스트 및 디버깅 용도
        return lastSymbolDetector.toString();
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

        lastSymbolDetector.setSrcPath(srcPath);

        Long symbolReferenceId = symbolReferenceDTO.getSymbolReferenceId();
        JavaParserBlockDTO rootBlock = blockManager.buildBlock(1, null, nodeType, cu, symbolReferenceId);

        visitAndBuild(cu, rootBlock, isDependency, typeImportMapper);
    }

    private void visitAndBuild(Node node, JavaParserBlockDTO parentJavaParserBlockDTO, Boolean isDependency, Map<String, JavaParserImportDTO> typeImportMapper) {
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
        } else if (nodeType.equals("ClassOrInterfaceDeclaration") || nodeType.equals("EnumDeclaration")) {
            JavaParserClassDTO javaParserClassDTO = classManager.buildClassOrEnum(javaParserBlockDTO, getCurrentPackageId(), node);
            javaParserBlockDTO = javaParserClassDTO.getOwnBlock();
            lastSymbolDetector.saveLastSymbol(nodeType, javaParserClassDTO.getName(), javaParserClassDTO.getPosition());
            typeResolver.resolveSymbolAndUpdateFQNDTO(node, nodeType, javaParserClassDTO, typeImportMapper);

        } else if (nodeType.equals("AnnotationDeclaration") || nodeType.equals("RecordDeclaration")) {
            javaParserBlockDTO = blockManager.buildBlock(parentJavaParserBlockDTO.getDepth() + 1, parentJavaParserBlockDTO.getBlockId(), nodeType,
                    node, parentJavaParserBlockDTO.getSymbolReferenceId());
        } else if (nodeType.equals("BlockStmt")) {
            String blockStmtType = getMyBlockTypeByParent(node, parentJavaParserBlockDTO);
            javaParserBlockDTO = blockManager.buildBlock(parentJavaParserBlockDTO.getDepth() + 1, parentJavaParserBlockDTO.getBlockId(),
                    blockStmtType, node, parentJavaParserBlockDTO.getSymbolReferenceId());
            // 실제 BlockStmt만 bracket position을 저장
            javaParserBlockDTO.setBracketPosition(javaParserBlockDTO.getPosition());
        }
        // 클래스 바로 아래에서 변수를 선언하는 멤버 필드
        else if (nodeType.equals("FieldDeclaration")) {
            JavaParserClassDTO belongedClassDTOByBlock = classManager.findClassDTOByBlock(javaParserBlockDTO);
            List<JavaParserMemberVariableDeclarationDTO> mvdDtoList =
                    variableManager.buildVariableDeclInMemberField(javaParserBlockDTO, belongedClassDTOByBlock.getClassId(), node);

            JavaParserMemberVariableDeclarationDTO firstMvdDTO = mvdDtoList.get(0);

            lastSymbolDetector.saveLastSymbol("FieldDeclaration", firstMvdDTO.getName(), firstMvdDTO.getPosition());

            typeResolver.resolveSymbolAndUpdateFQNDTO(node, nodeType, firstMvdDTO, typeImportMapper);
            if (mvdDtoList.size() > 1 && firstMvdDTO.getFullQualifiedNameId()!=null) {
                for (int i = 1; i < mvdDtoList.size(); i++) {
                    JavaParserMemberVariableDeclarationDTO mvdDto = mvdDtoList.get(i);
                    mvdDto.setFullQualifiedNameId(firstMvdDTO.getFullQualifiedNameId());
                    typeResolver.resolveSymbolAndUpdateFQNDTO(node, nodeType, mvdDto, typeImportMapper);
                }
            }
        }
        // 함수 내에서 선언하는 변수
        else if (nodeType.equals("VariableDeclarationExpr")) {
            List<JavaParserStmtVariableDeclarationDTO> stmtDtoList = variableManager.buildVariableDeclInMethod(javaParserBlockDTO, node);
            JavaParserStmtVariableDeclarationDTO firstStmtDTO = stmtDtoList.get(0);

            lastSymbolDetector.saveLastSymbol("VariableDeclarationExpr", firstStmtDTO.getName(), firstStmtDTO.getPosition());

            typeResolver.resolveSymbolAndUpdateFQNDTO(node, nodeType, firstStmtDTO, typeImportMapper);
            if (stmtDtoList.size() > 1 && firstStmtDTO.getFullQualifiedNameId()!=null) {
                for (int i = 1; i < stmtDtoList.size(); i++) {
                    JavaParserStmtVariableDeclarationDTO stmtDto = stmtDtoList.get(i);
                    stmtDto.setFullQualifiedNameId(firstStmtDTO.getFullQualifiedNameId());
                    typeResolver.resolveSymbolAndUpdateFQNDTO(node, nodeType, stmtDto, typeImportMapper);
                }
            }

        } else if (nodeType.equals("MethodDeclaration") || nodeType.equals("ConstructorDeclaration")) {
            // 함수 및 생성자 선언 시 build
            JavaParserClassDTO belongedClassDTOByBlock = classManager.findClassDTOByBlock(javaParserBlockDTO);
            JavaParserMethodDeclarationDTO mdDto = methodManager.buildMethodDeclaration(javaParserBlockDTO, belongedClassDTOByBlock.getClassId(), node);

            // 나 자신의 block type 생성
            javaParserBlockDTO = mdDto.getOwnBlock();
            lastSymbolDetector.saveLastSymbol("MethodDeclaration", mdDto.getName(), mdDto.getPosition());
            typeResolver.resolveSymbolAndUpdateFQNDTO(node, nodeType, mdDto, typeImportMapper);

        } else if (nodeType.equals("MethodCallExpr")) {
            JavaParserMethodCallExprDTO mceDto = methodManager.buildMethodCallExpr(javaParserBlockDTO, node);
            lastSymbolDetector.saveLastSymbol("MethodCallExpr", mceDto.getName(), mceDto.getPosition());
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
            lastSymbolDetector.saveLastSymbol("ObjectCreationExpr", objectCreationExpr.getTypeAsString(), new Position(node.getRange().get().begin.line, node.getRange().get().begin.column, node.getRange().get().end.line, node.getRange().get().end.column));

            // 내부에 초기화 블록이 존재할 때만 생성하도록 추가
            if (objectCreationExpr.getAnonymousClassBody().isPresent()) {
                expressionManager.buildExpressionRecursively(null, null,
                        javaParserBlockDTO.getBlockId(), (Expression) node);
                javaParserBlockDTO = blockManager.buildBlock(parentJavaParserBlockDTO.getDepth() + 1, parentJavaParserBlockDTO.getBlockId(),
                        nodeType, node, parentJavaParserBlockDTO.getSymbolReferenceId());
            }
        } else if (nodeType.equals("IfStmt")) {
            JavaParserIfStmtDTO ifStmtDTO = ifStmtManager.buildIfStatement(javaParserBlockDTO, (IfStmt) node);
            javaParserBlockDTO = ifStmtDTO.getOwnBlock();
        } else if (nodeType.equals("ForStmt") || nodeType.equals("ForEachStmt")) {
            JavaParserForStmtDTO forStmtDTO = forStmtManager.buildForOrForEachStatement(javaParserBlockDTO, node);
            javaParserBlockDTO = forStmtDTO.getOwnBlock();
        } else if (nodeType.equals("WhileStmt") || nodeType.equals("DoStmt")) {
            JavaParserWhileStmtDTO whileStmtDTO = whileStmtManager.buildWhileOrDoWhileStatement(javaParserBlockDTO, node);
            javaParserBlockDTO = whileStmtDTO.getOwnBlock();
        } else if (nodeType.equals("SwitchStmt") || nodeType.equals("SwitchEntry")) {
            JavaParserSwitchStmtDTO switchStmtDTO = switchStmtManager.buildSwitchOrCaseStatement(javaParserBlockDTO, node);
            javaParserBlockDTO = switchStmtDTO.getOwnBlock();
        } else if (Statement.class.isAssignableFrom(node.getClass())) {
            /**
             * Statement를 상속받은 노드 중, 부모가 ifStmt인 경우에 block 처리 하지 않음.
             * * while, do-while, for, forEach문 모두 자식으로 하나의 statement를 가질 수 있고 이를 body-block 처리하지 않았기 때문.
             * * 또한, thenStmt와 elseStmt에 다른 제어문이 올 수 있는데... 모든 statement의 block을 저장할 때 type을 위해 부모를 확인해야하는 번거러움이 존재.
             *
             * ifStmt의 경우, thenStmt와 elseStmt가 따로 Node 형태로 존재하지 않아, 2개의 Statement가 ifStmt Block을 부모로 갖는다.
             * 일단은 구분을 position으로 해서 비즈니스 로직 처리.
             */
        }

        expressionManager.deleteExpressionDTO(node.hashCode());

        List<Node> childNodes = node.getChildNodes();
        for (Node childNode : childNodes) {
            Class<? extends Node> type = childNode.getMetaModel().getType();
            if (Type.class.isAssignableFrom(type) || NodeWithIdentifier.class.isAssignableFrom(type) || Modifier.class.isAssignableFrom(type)) {
                continue;
            }
            visitAndBuild(childNode, javaParserBlockDTO, isDependency, typeImportMapper);
        }
    }

    // 현재 package 선언문이 존재하지 않는 경우, DB의 이미 저장된 NULL의 ID -100L 반환
    // *** identifier 자체를 수정하면 에러 발생! ***
    private long getCurrentPackageId() {
        return hasPackage ? packageManager.getLastId():-100L;
    }

    private String getMyBlockTypeByParent(Node node, JavaParserBlockDTO parentJavaParserBlockDTO) {
        String blockType = node.getMetaModel().getTypeName();
        if (node.getParentNode().isPresent()) {
            Node parentNode = node.getParentNode().get();
            String parentNodeType = parentNode.getMetaModel().getTypeName();
            if (parentNode!=parentJavaParserBlockDTO.getNode()) {
                // 부모가 존재하지만, parentNode가 symbol Table에 저장되지 않는 type인 경우.
                blockType = parentNodeType;
            } else if (parentNodeType.equals("MethodDeclaration") || parentNodeType.equals("ConstructorDeclaration")
                    || parentNodeType.equals("SwitchEntry") || parentNodeType.equals("ForStmt") || parentNodeType.equals("ForEachStmt")
                    || parentNodeType.equals("WhileStmt") || parentNodeType.equals("DoStmt")
            ) {
                blockType = parentNodeType + "Body";
            } else if (parentNodeType.equals("IfStmt")) {
                IfStmt parentIfStmt = (IfStmt) parentNode;
                if (parentIfStmt.getThenStmt()==node) {
                    blockType = "thenStmtBody";
                } else {
                    blockType = "elseStmtBody";
                }
            }
        }
        return blockType;
    }
}
