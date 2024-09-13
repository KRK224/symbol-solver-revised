package org.kryun.symbol.javaparser.management;

import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.expr.AnnotationExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.type.Type;
import org.kryun.symbol.javaparser.model.dto.JavaParserArgumentDTO;
import org.kryun.symbol.javaparser.model.dto.JavaParserBlockDTO;
import org.kryun.symbol.javaparser.model.dto.JavaParserExpressionDTO;
import org.kryun.symbol.javaparser.model.dto.JavaParserMethodCallExprDTO;
import org.kryun.symbol.javaparser.model.dto.JavaParserMethodDeclarationDTO;
import org.kryun.symbol.javaparser.model.dto.JavaParserParameterDTO;
import org.kryun.symbol.javaparser.model.dto.JavaParserReturnMapperDTO;
import org.kryun.symbol.model.dto.Position;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import org.kryun.symbol.pkg.IdentifierGenerator;

@Getter
public class MethodManager {

    private final List<JavaParserMethodDeclarationDTO> javaParserMethodDeclarationDTOList;
    private final List<JavaParserMethodCallExprDTO> javaParserMethodCallExprDTOList;

    private final List<JavaParserParameterDTO> javaParserParameterDTOList;
    private final List<JavaParserArgumentDTO> javaParserArgumentDTOList;
    private final List<JavaParserReturnMapperDTO> javaParserReturnMapperDTOList;
    private final BlockManager blockManager;

    private final IdentifierGenerator methodDeclarationIdGenerator = new IdentifierGenerator("method_decl");

    private final IdentifierGenerator parameterIdGenerator = new IdentifierGenerator("parameter");
    private final IdentifierGenerator argumentIdGenerator = new IdentifierGenerator("argument");
    private final IdentifierGenerator returnMapperIdGenerator = new IdentifierGenerator("return_mapper");
    private final IdentifierGenerator methodCallExprIdGenerator = new IdentifierGenerator("method_call_expr");

    private final ExpressionManager expressionManager;
    private final Boolean isDependency;

    public Map<String, Long> getIdentifierMap() {
        Map<String, Long> identifierMap = new LinkedHashMap<>();
        identifierMap.put(methodDeclarationIdGenerator.getSymbolType(), methodDeclarationIdGenerator.getLastId());
        identifierMap.put(parameterIdGenerator.getSymbolType(), parameterIdGenerator.getLastId());
        identifierMap.put(argumentIdGenerator.getSymbolType(), argumentIdGenerator.getLastId());
        identifierMap.put(returnMapperIdGenerator.getSymbolType(), returnMapperIdGenerator.getLastId());
        identifierMap.put(methodCallExprIdGenerator.getSymbolType(), methodCallExprIdGenerator.getLastId());
        return identifierMap;
    }

    // private final Map<String, Long> symbolIds;

    public MethodManager(BlockManager blockManager, ExpressionManager expressionManager, Boolean isDependency) {
        this.blockManager = blockManager;
        this.javaParserMethodDeclarationDTOList = new ArrayList<>();
        this.javaParserMethodCallExprDTOList = new ArrayList<>();
        this.javaParserParameterDTOList = new ArrayList<>();
        this.javaParserArgumentDTOList = new ArrayList<>();
        this.javaParserReturnMapperDTOList = new ArrayList<>();
        this.expressionManager = expressionManager;
        this.isDependency = isDependency;
    }

    public void clear() {
        this.javaParserMethodDeclarationDTOList.clear();
        this.javaParserMethodCallExprDTOList.clear();
        this.javaParserParameterDTOList.clear();
        this.javaParserArgumentDTOList.clear();
        this.javaParserReturnMapperDTOList.clear();
    }

    public JavaParserMethodDeclarationDTO buildMethodDeclaration(JavaParserBlockDTO parentBlock, Long belongedClassId, Node node) {
        Long methodDeclarationId = methodDeclarationIdGenerator.nextId();
        Long blockId = parentBlock.getBlockId();
        JavaParserMethodDeclarationDTO javaParserMethodDeclarationDTO = new JavaParserMethodDeclarationDTO();
        JavaParserBlockDTO ownBlock = blockManager.buildBlock(parentBlock, node);
        javaParserMethodDeclarationDTO.setOwnBlockProperties(ownBlock);
        JavaParserReturnMapperDTO javaParserReturnMapperDTO = new JavaParserReturnMapperDTO();
        List<Node> childNodes = node.getChildNodes();
        List<JavaParserParameterDTO> localJavaParserParameterDTOList = new ArrayList<>();

        String modifierKeyword = "";
        String accessModifierKeyword = "";
        String methodName = "";

        int parameterIndex = 1;

        for (Node childNode : childNodes) {
            String childNodeTypeName = childNode.getMetaModel().getTypeName();
            if (childNodeTypeName.equals("Modifier")) {
                Modifier modifier = (Modifier) childNode;
                // 접근 제어자 분별
                if (modifier.getKeyword().equals(Modifier.Keyword.DEFAULT) ||
                        modifier.getKeyword().equals(Modifier.Keyword.PUBLIC) ||
                        modifier.getKeyword().equals(Modifier.Keyword.PROTECTED) ||
                        modifier.getKeyword().equals(Modifier.Keyword.PRIVATE)) {
                    accessModifierKeyword = modifier.getKeyword().asString();
                } else {
                    modifierKeyword = modifier.getKeyword().asString();
                }
            } else if (childNodeTypeName.equals("SimpleName")) {
                SimpleName simpleName = (SimpleName) childNode;
                methodName = simpleName.asString();
            } else if (childNodeTypeName.equals("Parameter")) {
                JavaParserParameterDTO javaParserParameterDTO = new JavaParserParameterDTO();
                Parameter parameterNode = (Parameter) childNode;
                Type parameterType = parameterNode.getType();
                String type = parameterType.asString();
                String name = parameterNode.getName().asString();
                boolean isGeneric = false;
                if (parameterType.isClassOrInterfaceType()) {
                    if (parameterType.asClassOrInterfaceType().getTypeArguments().isPresent()) {
                        isGeneric = true;
                    }
                }

                javaParserParameterDTO.setParameterId(parameterIdGenerator.nextId());
                javaParserParameterDTO.setMethodDeclId(methodDeclarationId);
                javaParserParameterDTO.setIndex(parameterIndex++);
                javaParserParameterDTO.setName(name);
                javaParserParameterDTO.setType(type);
                javaParserParameterDTO.setNode(parameterNode);
                javaParserParameterDTO.setPosition(Position.getPositionByNode(parameterNode));

                javaParserParameterDTOList.add(javaParserParameterDTO);
                localJavaParserParameterDTOList.add(javaParserParameterDTO);

            } else if (childNodeTypeName.matches("(.*)Type")) {
                Type returnType = ((Type) childNode).clone();
                // annotation 및 comment 제거 과정
                for (AnnotationExpr annotation : returnType.getAnnotations()) {
                    returnType.remove(annotation);
                }
                returnType.getComment().ifPresent(returnType::remove);

                String returnValueTypeName = returnType.toString();
                boolean isGeneric = false;

                if (returnType.isClassOrInterfaceType()) {
                    if (returnType.asClassOrInterfaceType().getTypeArguments().isPresent()) {
                        isGeneric = true;
                    }
                }

                javaParserReturnMapperDTO.setReturnMapperId(returnMapperIdGenerator.nextId());
                javaParserReturnMapperDTO.setMethodDeclId(methodDeclarationId);
                javaParserReturnMapperDTO.setType(returnValueTypeName);
                javaParserReturnMapperDTO.setNode(childNode);
                javaParserReturnMapperDTO.setPosition(Position.getPositionByNode(childNode));
                javaParserReturnMapperDTOList.add(javaParserReturnMapperDTO);
            }
        }

        javaParserMethodDeclarationDTO.setMethodDeclId(methodDeclarationId);
        javaParserMethodDeclarationDTO.setBlockId(blockId);
//        javaParserMethodDeclarationDTO.setOwnBlockProperties(ownBlock);
        javaParserMethodDeclarationDTO.setBelongedClassId(belongedClassId);
        javaParserMethodDeclarationDTO.setName(methodName);
        javaParserMethodDeclarationDTO.setModifier(modifierKeyword);
        javaParserMethodDeclarationDTO.setAccessModifier(accessModifierKeyword);
        // add to methodDeclarationDTO
        javaParserMethodDeclarationDTO.setReturnMapper(javaParserReturnMapperDTO);
        javaParserMethodDeclarationDTO.setParameters(localJavaParserParameterDTOList);
        javaParserMethodDeclarationDTO.setPosition(Position.getPositionByNode(node));

        javaParserMethodDeclarationDTOList.add(javaParserMethodDeclarationDTO);
        return javaParserMethodDeclarationDTO;
    }

    public JavaParserMethodCallExprDTO buildMethodCallExpr(JavaParserBlockDTO javaParserBlockDTO, Node node) {
        Long methodCallExprId = methodCallExprIdGenerator.nextId();
        Long blockId = javaParserBlockDTO.getBlockId();
        JavaParserMethodCallExprDTO javaParserMethodCallExprDTO = new JavaParserMethodCallExprDTO();
        MethodCallExpr methodCallExpr = (MethodCallExpr) node;
        List<JavaParserArgumentDTO> localJavaParserArgumentDTOList = new ArrayList<>();

        String methodName = "";
        int argumentIndex = 1;

        methodName = methodCallExpr.getName().getIdentifier();
        JavaParserExpressionDTO myJavaParserExpressionDTO = expressionManager.getExpressionDTO(
                methodCallExpr.hashCode());

        // 내가 다른 MethodCall의 Scope에 존재할 경우만 저장.
        {
            if (myJavaParserExpressionDTO!=null) {
                javaParserMethodCallExprDTO.setExpressionId(myJavaParserExpressionDTO.getExpressionId());
            }
        }

        methodCallExpr.getScope()
                .ifPresent((expression -> {
                    javaParserMethodCallExprDTO.setNameExprNode(expression);
                    JavaParserExpressionDTO scopeJavaParserExpressionDTO = expressionManager.buildExpressionRecursively(
                            null, null, blockId, expression);
                    // scope 저장.
                    javaParserMethodCallExprDTO.setScopeExpressionId(scopeJavaParserExpressionDTO.getExpressionId());
                }));


        NodeList<Expression> arguments = methodCallExpr.getArguments();

        for (Expression arg : arguments) {
            JavaParserArgumentDTO javaParserArgumentDTO = new JavaParserArgumentDTO();
            javaParserArgumentDTO.setIndex(argumentIndex++);
            javaParserArgumentDTO.setName(arg.toString());
            javaParserArgumentDTO.setArgumentId(argumentIdGenerator.nextId());
            javaParserArgumentDTO.setMethodCallExprId(methodCallExprId);

            JavaParserExpressionDTO javaParserExpressionDTO = expressionManager.buildExpressionRecursively(
                    null, null, blockId, arg);

            javaParserArgumentDTO.setExpressionId(javaParserExpressionDTO.getExpressionId());
            javaParserArgumentDTO.setPosition(Position.getPositionByNode(arg));
            javaParserArgumentDTOList.add(javaParserArgumentDTO);
            localJavaParserArgumentDTOList.add(javaParserArgumentDTO);
        }
        javaParserMethodCallExprDTO.setPosition(Position.getPositionByNode(methodCallExpr));

        javaParserMethodCallExprDTO.setMethodCallExprId(methodCallExprId);
        javaParserMethodCallExprDTO.setBlockId(blockId);
        javaParserMethodCallExprDTO.setName(methodName);
        javaParserMethodCallExprDTO.setArguments(localJavaParserArgumentDTOList);

        javaParserMethodCallExprDTOList.add(javaParserMethodCallExprDTO);
        return javaParserMethodCallExprDTO;
    }
}
