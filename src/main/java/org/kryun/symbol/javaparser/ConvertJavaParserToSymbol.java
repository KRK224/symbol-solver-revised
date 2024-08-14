package org.kryun.symbol.javaparser;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.nodeTypes.NodeWithIdentifier;
import com.github.javaparser.ast.stmt.DoStmt;
import com.github.javaparser.ast.stmt.ForEachStmt;
import com.github.javaparser.ast.stmt.ForStmt;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.stmt.SwitchEntry;
import com.github.javaparser.ast.stmt.SwitchStmt;
import com.github.javaparser.ast.stmt.WhileStmt;
import com.github.javaparser.ast.type.Type;
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
import org.kryun.symbol.model.dto.Position;
import org.kryun.symbol.model.dto.SymbolReferenceDTO;
import java.sql.Connection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.kryun.symbol.pkg.LastSymbolDetector;
import org.kryun.symbol.pkg.builder.interfaces.SymbolContainer;

public class ConvertJavaParserToSymbol implements SymbolContainer {

    private final Long symbolStatusId;
    private final Boolean isDependency;
    private final LastSymbolDetector lastSymbolDetector = new LastSymbolDetector();
    private TypeResolver typeResolver;

    private final SymbolReferenceManager symbolReferenceManager = new SymbolReferenceManager();
    private final BlockManager blockManager = new BlockManager();
    private final PackageManager packageManager = new PackageManager();
    private final ImportManager importManager = new ImportManager();
    private final ClassManager classManager = new ClassManager();

    private final VariableManager variableManager = new VariableManager();
    private final MethodManager methodManager;
    private final FullQualifiedNameManager fullQualifiedNameManager = new FullQualifiedNameManager();
    private final ExpressionManager expressionManager = new ExpressionManager();

    private Boolean hasPackage = false;

    public ConvertJavaParserToSymbol(Long symbolStatusId, Boolean isDependency) {
        this.isDependency = isDependency;
        this.symbolStatusId = symbolStatusId;
        this.typeResolver = new TypeResolver(symbolStatusId, fullQualifiedNameManager);
        this.methodManager = new MethodManager(expressionManager, isDependency);
    }

    public ConvertJavaParserToSymbol(Long symbolStatusId, Boolean isDependency, Connection conn) {
        this.isDependency = isDependency;
        this.symbolStatusId = symbolStatusId;
        this.typeResolver = new TypeResolver(symbolStatusId, fullQualifiedNameManager, conn);
        this.methodManager = new MethodManager(expressionManager, isDependency);
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

    public String printLastSymbol() {
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
        } else if (nodeType.equals("ClassOrInterfaceDeclaration")) {
            JavaParserClassDTO javaParserClassDTO = classManager.buildClass(javaParserBlockDTO, getCurrentPackageId(), node);
            // 나 자신의 class block type 생성
            javaParserBlockDTO = blockManager.buildBlock(parentJavaParserBlockDTO.getDepth() + 1, parentJavaParserBlockDTO.getBlockId(), nodeType, node, parentJavaParserBlockDTO.getSymbolReferenceId());
            javaParserClassDTO.setOwnBlockProperties(javaParserBlockDTO);

            lastSymbolDetector.saveLastSymbol("ClassOrInterfaceDeclaration", javaParserClassDTO.getName(), javaParserClassDTO.getPosition());

            typeResolver.resolveSymbolAndUpdateFQNDTO(node, nodeType, javaParserClassDTO, typeImportMapper);

        } else if (nodeType.equals("EnumDeclaration")) {
            JavaParserClassDTO enumDTO = classManager.buildEnum(javaParserBlockDTO, getCurrentPackageId(), node);

            javaParserBlockDTO = blockManager.buildBlock(parentJavaParserBlockDTO.getDepth() + 1, parentJavaParserBlockDTO.getBlockId(), nodeType, node, parentJavaParserBlockDTO.getSymbolReferenceId());
            lastSymbolDetector.saveLastSymbol("EnumDeclaration", enumDTO.getName(), enumDTO.getPosition());
            typeResolver.resolveSymbolAndUpdateFQNDTO(node, nodeType, enumDTO, typeImportMapper);

        } else if (nodeType.equals("AnnotationDeclaration") || nodeType.equals("RecordDeclaration")) {
            javaParserBlockDTO = blockManager.buildBlock(parentJavaParserBlockDTO.getDepth() + 1, parentJavaParserBlockDTO.getBlockId(), nodeType,
                    node, parentJavaParserBlockDTO.getSymbolReferenceId());
        } else if (nodeType.equals("BlockStmt")) {

            String blockStmtType = nodeType;
            // 부모가 존재하는 경우.
            if (node.getParentNode().isPresent()) {
                Node parentNode = node.getParentNode().get();
                String parentNodeType = parentNode.getMetaModel().getTypeName();

                if (parentNode!=javaParserBlockDTO.getNode()) { // 부모가 존재하지만, parentNode가 symbol Table에 저장되지 않는 type인 경우.
                    System.out.println("Debugging - parentNode is not equal to javaParserBlockDTO.getNode()");
                    blockStmtType = parentNodeType; // 우선 기존처럼 부모 type으로 가지고 있도록 수정(try, catchClause 등)
                } else {
                    // 현재 하나의 BlockStmt로 취급되는 symbol이 부모인 경우
                    // Todo. 만약 언어 및 parser별 타입을 통일하여 재정의시, javaParser Node의 type이 아닌, parentblockDTO type을 조건문에 사용할 것
                    /**
                     * * 현재 언어 및 parser 그리고 symbol 타입별로 block({})이 존재하거나 아닌 경우가 다 달라 BlockStmt 대신 body로 변경함.
                     * ** ClassOrInterfaceDeclaration, EnumDeclaration, SwitchStmt 등의 경우 javaParser에서는 blockStmt 자식이 존재하지 않아 조건문에서 제외
                     * *** 따라서 다른 파서나 언어에서 아래 코드 사용시에 주의할 것
                     */
                    if (parentNodeType.equals("MethodDeclaration") || parentNodeType.equals("ConstructorDeclaration")
                            || parentNodeType.equals("SwitchEntry") || parentNodeType.equals("ForStmt") || parentNodeType.equals("ForEachStmt")
                            || parentNodeType.equals("WhileStmt") || parentNodeType.equals("DoStmt")
                    ) {
                        blockStmtType = parentNodeType + "Body";
                    } else if (parentNodeType.equals("IfStmt")) {
                        // Todo. IfStmtManager method 사용할 것.
                        IfStmt parentIfStmt = (IfStmt) parentNode;
                        if (parentIfStmt.getThenStmt()==node) {
                            blockStmtType = "thenStmt";
                        } else {
                            blockStmtType = "elseStmt";
                        }
                    }
                }
            }
            javaParserBlockDTO = blockManager.buildBlock(parentJavaParserBlockDTO.getDepth() + 1, parentJavaParserBlockDTO.getBlockId(),
                    blockStmtType, node, parentJavaParserBlockDTO.getSymbolReferenceId());
        }
        // 클래스 바로 아래에서 변수를 선언하는 멤버 필드
        else if (nodeType.equals("FieldDeclaration")) {
            JavaParserClassDTO belongedClassDTOByBlock = classManager.findClassDTOByBlock(javaParserBlockDTO);
            List<JavaParserMemberVariableDeclarationDTO> mvdDtoList = variableManager.buildVariableDeclInMemberField(javaParserBlockDTO,
                    belongedClassDTOByBlock.getClassId(), node, expressionManager);

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
            List<JavaParserStmtVariableDeclarationDTO> stmtDtoList = variableManager.buildVariableDeclInMethod(javaParserBlockDTO, node, expressionManager);
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
            javaParserBlockDTO = blockManager.buildBlock(parentJavaParserBlockDTO.getDepth() + 1, parentJavaParserBlockDTO.getBlockId(), nodeType, node, parentJavaParserBlockDTO.getSymbolReferenceId());
            mdDto.setOwnBlockProperties(javaParserBlockDTO);

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
            System.out.println("Debugging - IfStmt");
            IfStmt ifStmtNode = (IfStmt) node;
            Statement thenStmt = ifStmtNode.getThenStmt();
            if (!thenStmt.isBlockStmt()) {
                String typeName = thenStmt.getMetaModel().getTypeName();
                String parentTypeName = thenStmt.getParentNode().get().getMetaModel().getTypeName();
                System.out.println("Debugging - ThenStmt Type: " + typeName);
                System.out.println("parentTypeName = " + parentTypeName);
            }
            Optional<Statement> elseStmt = ifStmtNode.getElseStmt();
            if (elseStmt.isPresent()) {
                System.out.println("Debugging - ElseStmt is present");
                if (elseStmt.get().isIfStmt()) {
                    System.out.println("Debugging - ElseStmt is IfStmt");
                } else if (elseStmt.get().isBlockStmt()) {
                    System.out.println("Debugging - ElseStmt is blockStmt");
                } else {
                    Statement statement = elseStmt.get();
                    String typeName = statement.getMetaModel().getTypeName();
                    String parentTypeName = statement.getParentNode().get().getMetaModel().getTypeName();
                    System.out.println("Debugging - ElseStmt Type: " + typeName);
                    System.out.println("parentTypeName = " + parentTypeName);
                }
            }
        } else if (nodeType.equals("SwitchStmt")) {
            System.out.println("Debugging - SwitchStmt");
            SwitchStmt switchStmt = (SwitchStmt) node;
            NodeList<SwitchEntry> entries = switchStmt.getEntries();
            for (SwitchEntry entry : entries) {
                System.out.println("Debugging - SwitchEntry");
                // entry 의 label은 Expression
                NodeList<Expression> labels = entry.getLabels();
                NodeList<Statement> statements = entry.getStatements();
                for (Statement statement : statements) {
                    System.out.println("Debugging - Statement");
                }
            }
        } else if (nodeType.equals("ForStmt")) {
            ForStmt forStmt = (ForStmt) node;
            Statement body = forStmt.getBody();
            NodeList<Expression> initialization = forStmt.getInitialization();
            NodeList<Expression> update = forStmt.getUpdate();
            Optional<Expression> compare = forStmt.getCompare();
        } else if (nodeType.equals("ForEachStmt")) {
            ForEachStmt forEachStmt = (ForEachStmt) node;
            Statement body = forEachStmt.getBody();
            Expression iterable = forEachStmt.getIterable();
            VariableDeclarationExpr variable = forEachStmt.getVariable();
        } else if (nodeType.equals("WhileStmt")) {
            WhileStmt whileStmt = (WhileStmt) node;
            // body는 block일 수도 있고 아닐 수도 있다.. => while_stmt_body type으로 생성할 것
            Statement body = whileStmt.getBody();
            Expression condition = whileStmt.getCondition();
        } else if (nodeType.equals("DoStmt")) {
            DoStmt doStmt = (DoStmt) node;
            Statement body = doStmt.getBody();
            Expression condition = doStmt.getCondition();
        } else if (Statement.class.isAssignableFrom(node.getClass())) {
            // if 문 내부의 Statement (1줄인 경우 block 생성 필요)
            // switch 문의 경우 여러 줄이 switchEntry 블록을 부모로 저장됨 (DFS)
            if (node.getParentNode().isPresent() && node.getParentNode().get().getMetaModel().getTypeName().equals("IfStmt")) {
                // Todo. node를 받아서 부모가 if인지 판별 후 현재 node가 then인지 else인지 판별하는 메소드를 ifStmtManager에 추가 => Optional<String>을 반환해서 empty이면 부모가 ifStmt가 아니다.
                IfStmt parentIfStmt = (IfStmt) node.getParentNode().get();
                if (parentIfStmt.getThenStmt()==node) {
                    javaParserBlockDTO = blockManager.buildBlock(parentJavaParserBlockDTO.getDepth() + 1, parentJavaParserBlockDTO.getBlockId(),
                            "thenStmt", node, parentJavaParserBlockDTO.getSymbolReferenceId());
                } else {
                    javaParserBlockDTO = blockManager.buildBlock(parentJavaParserBlockDTO.getDepth() + 1, parentJavaParserBlockDTO.getBlockId(),
                            "elseStmt", node, parentJavaParserBlockDTO.getSymbolReferenceId());
                }
            }
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
}
