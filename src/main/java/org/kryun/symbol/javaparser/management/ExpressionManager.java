package org.kryun.symbol.javaparser.management;

import com.github.javaparser.ast.expr.ArrayAccessExpr;
import com.github.javaparser.ast.expr.ArrayCreationExpr;
import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.BinaryExpr.Operator;
import com.github.javaparser.ast.expr.CastExpr;
import com.github.javaparser.ast.expr.ConditionalExpr;
import com.github.javaparser.ast.expr.EnclosedExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.LambdaExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.MethodReferenceExpr;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.expr.UnaryExpr;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.Type;
import org.kryun.global.enums.symbol.ExpressionRelationEnum;
import org.kryun.symbol.javaparser.model.dto.JavaParserExpressionDTO;
import org.kryun.symbol.model.dto.Position;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.kryun.symbol.pkg.IdentifierGenerator;

public class ExpressionManager {

    private final List<JavaParserExpressionDTO> javaParserExpressionDTOList;

    private final Map<Integer, JavaParserExpressionDTO> expressionDTOMap;
    private final IdentifierGenerator expressionIdGenerator = new IdentifierGenerator("expression");

    public Map<String, Long> getIdentifierMap() {
        Map<String, Long> identifierMap = new HashMap<>();
        identifierMap.put(expressionIdGenerator.getSymbolType(), expressionIdGenerator.getLastId());
        return identifierMap;
    }

    public ExpressionManager() {
        this.javaParserExpressionDTOList = new ArrayList<>();
        this.expressionDTOMap = new HashMap<>();
    }

    public List<JavaParserExpressionDTO> getExpressionDTOList() {
        return this.javaParserExpressionDTOList;
    }

    public JavaParserExpressionDTO getExpressionDTO(Integer nodeHashcode) {
        return this.expressionDTOMap.get(nodeHashcode);
    }

    public void putExpressionDTO(Integer nodeHashcode, JavaParserExpressionDTO javaParserExpressionDTO) {
        this.expressionDTOMap.computeIfAbsent(nodeHashcode, k -> javaParserExpressionDTO);
    }

    public boolean containsExpressionDTO(Integer nodeHashcode) {
        return this.expressionDTOMap.containsKey(nodeHashcode);
    }

    public void deleteExpressionDTO(Integer nodeHashcode) {
        if (containsExpressionDTO(nodeHashcode)) {
            this.expressionDTOMap.remove(nodeHashcode);
        }
    }

    public void clear() {
        this.javaParserExpressionDTOList.clear();
        this.expressionDTOMap.clear();
    }

    public JavaParserExpressionDTO buildExpressionRecursively(Long parentExpressionId, ExpressionRelationEnum expressionRelationEnum, Long blockId,
                                                              Expression expression) {

        JavaParserExpressionDTO existJavaParserExpressionDTO = getExpressionDTO(expression.hashCode());

        if (existJavaParserExpressionDTO!=null) {
            return existJavaParserExpressionDTO;
        }

        String expressionType = expression.getMetaModel().getTypeName();

        JavaParserExpressionDTO javaParserExpressionDTO = buildExpressionDTO(parentExpressionId, expressionRelationEnum, blockId, expression, expressionType);

        if (expressionType.equals("NameExpr") || expressionType.equals("TypeExpr")
                || expressionType.equals("ThisExpr") || expressionType.equals("SuperExpr")
                || expressionType.equals("ClassExpr") || expressionType.equals("InstanceOfExpr")
                || expressionType.equals("TextBlockLiteralExpr") || expressionType.endsWith(
                "LiteralExpr")) {
            // 종점. 명시적으로 표기용

        } else if (expressionType.equals("ArrayAccessExpr")) {

            ArrayAccessExpr arrayAccessExpr = (ArrayAccessExpr) expression;

            // ArrayAccessExpr의 name 부분을 재귀적으로 생성
            buildExpressionRecursively(javaParserExpressionDTO.getExpressionId(), ExpressionRelationEnum.ARRAY_ACCESS_NAME, blockId,
                    arrayAccessExpr.getName());


        } else if (expressionType.equals("ArrayCreationExpr")) {
            ArrayCreationExpr arrayCreationExpr = (ArrayCreationExpr) expression;
            Type elementType = arrayCreationExpr.getElementType();

            buildExpressionDTO(javaParserExpressionDTO.getExpressionId(), ExpressionRelationEnum.ARRAY_CREATION_TYPE, blockId, elementType);

        } else if (expressionType.equals("AssignExpr")) {
            AssignExpr assignExpr = (AssignExpr) expression;

            Expression target = assignExpr.getTarget();
            Expression value = assignExpr.getValue();

            buildExpressionRecursively(javaParserExpressionDTO.getExpressionId(), ExpressionRelationEnum.ASSIGN_TARGET, blockId, target);
            buildExpressionRecursively(javaParserExpressionDTO.getExpressionId(), ExpressionRelationEnum.ASSIGN_VALUE, blockId, value);

        } else if (expressionType.equals("BinaryExpr")) {
            BinaryExpr binaryExpr = (BinaryExpr) expression;

            Expression left = binaryExpr.getLeft();
            Expression right = binaryExpr.getRight();
            Operator operator = binaryExpr.getOperator();

            buildExpressionRecursively(javaParserExpressionDTO.getExpressionId(), ExpressionRelationEnum.BINARY_LEFT, blockId, left);
            buildExpressionRecursively(javaParserExpressionDTO.getExpressionId(), ExpressionRelationEnum.BINARY_RIGHT, blockId, right);
        } else if (expressionType.equals("CastExpr")) {
            CastExpr castExpr = (CastExpr) expression;
            Type type = castExpr.getType();
            buildExpressionDTO(javaParserExpressionDTO.getExpressionId(), ExpressionRelationEnum.CAST_TYPE, blockId, type);

            Expression castExpression = castExpr.getExpression();
            buildExpressionRecursively(javaParserExpressionDTO.getExpressionId(), ExpressionRelationEnum.CAST_EXPRESSION, blockId, castExpression);
        } else if (expressionType.equals("ConditionalExpr")) {

            ConditionalExpr conditionalExpr = (ConditionalExpr) expression;
            Expression thenExpr = conditionalExpr.getThenExpr();
            Expression elseExpr = conditionalExpr.getElseExpr();

            buildExpressionRecursively(javaParserExpressionDTO.getExpressionId(), ExpressionRelationEnum.CONDITIONAL_THEN, blockId, thenExpr);
            buildExpressionRecursively(javaParserExpressionDTO.getExpressionId(), ExpressionRelationEnum.CONDITIONAL_ELSE, blockId, elseExpr);
        } else if (expressionType.equals("EnclosedExpr")) {
            EnclosedExpr enclosedExpr = (EnclosedExpr) expression;
            Expression inner = enclosedExpr.getInner();
            buildExpressionRecursively(javaParserExpressionDTO.getExpressionId(), ExpressionRelationEnum.ENCLOSED_INNER, blockId, inner);

        } else if (expressionType.equals("FieldAccessExpr")) {
            // FieldAccessExpr의 scope 부분을 재귀적으로 생성
            FieldAccessExpr fieldAccessExpr = (FieldAccessExpr) expression;
            Expression scopeExpression = fieldAccessExpr.getScope();
            buildExpressionRecursively(javaParserExpressionDTO.getExpressionId(), ExpressionRelationEnum.FIELD_ACCESS_SCOPE, blockId, scopeExpression);
            SimpleName nameExpr = fieldAccessExpr.getName();
            buildExpressionDTO(javaParserExpressionDTO.getExpressionId(), ExpressionRelationEnum.FIELD_ACCESS_NAME, blockId, nameExpr);
        } else if (expressionType.equals("MethodCallExpr")) {
            // 실제 Scope안에 MethodCall이 존재하면 그 다음 Scope가 동일하기 때문에 추후에 조회 가능.
            MethodCallExpr methodCallExpr = (MethodCallExpr) expression;

            // Scope에 대해서만 저장.
            methodCallExpr.getScope().ifPresent(scope -> {
                buildExpressionRecursively(javaParserExpressionDTO.getExpressionId(), ExpressionRelationEnum.METHOD_CALL_SCOPE, blockId, scope);
            });
        } else if (expressionType.equals("UnaryExpr")) {
            UnaryExpr unaryExpr = (UnaryExpr) expression;
            Expression unaryExpression = unaryExpr.getExpression();
            buildExpressionRecursively(javaParserExpressionDTO.getExpressionId(), ExpressionRelationEnum.UNARY_EXPRESSION, blockId, unaryExpression);
        } else if (expressionType.equals("LambdaExpr")) {
            LambdaExpr lambdaExpr = (LambdaExpr) expression;

            lambdaExpr.getExpressionBody().ifPresent(expressionBody -> {
                buildExpressionRecursively(javaParserExpressionDTO.getExpressionId(), ExpressionRelationEnum.LAMBDA_EXPRESSION, blockId, expressionBody);
            });
        } else if (expressionType.equals("MethodReferenceExpr")) {
            MethodReferenceExpr methodReferenceExpr = (MethodReferenceExpr) expression;
            Expression scope = methodReferenceExpr.getScope();
            buildExpressionRecursively(javaParserExpressionDTO.getExpressionId(), ExpressionRelationEnum.METHOD_REFERENCE_SCOPE, blockId, scope);
        } else if (expressionType.equals("ObjectCreationExpr")) {
            ObjectCreationExpr objectCreationExpr = (ObjectCreationExpr) expression;
            ClassOrInterfaceType classOrInterfaceType = objectCreationExpr.getType();
            buildExpressionDTO(javaParserExpressionDTO.getExpressionId(), ExpressionRelationEnum.OBJECT_CREATION_TYPE, blockId, classOrInterfaceType);
        }
        putExpressionDTO(expression.hashCode(), javaParserExpressionDTO);
        return javaParserExpressionDTO;
    }

    private JavaParserExpressionDTO buildExpressionDTO(Long parentExpressionId, ExpressionRelationEnum expressionRelationEnum, Long blockId,
                                                       Expression expression, String expressionType) {

        Long expressionId = expressionIdGenerator.nextId();
        JavaParserExpressionDTO javaParserExpressionDTO = JavaParserExpressionDTO.builder()
                .expressionId(expressionId)
                .parentExpressionId(parentExpressionId)
                .expressionRelation(expressionRelationEnum)
                .expression(expression.toString())
                .expressionType(expressionType)
                .blockId(blockId)
                .position(new Position(expression.getRange().get().begin.line,
                        expression.getRange().get().begin.column,
                        expression.getRange().get().end.line,
                        expression.getRange().get().end.column))
                .build();

        javaParserExpressionDTOList.add(javaParserExpressionDTO);
        putExpressionDTO(expression.hashCode(), javaParserExpressionDTO);

        return javaParserExpressionDTO;
    }

    private JavaParserExpressionDTO buildExpressionDTO(Long parentExpressionId, ExpressionRelationEnum expressionRelationEnum, Long blockId, Type type) {

        Long expressionId = expressionIdGenerator.nextId();

        JavaParserExpressionDTO javaParserExpressionDTO = JavaParserExpressionDTO.builder()
                .expressionId(expressionId)
                .parentExpressionId(parentExpressionId)
                .expressionRelation(expressionRelationEnum)
                .expression(type.toString())
                .expressionType("TypeExpr")
                .blockId(blockId)
                .position(new Position(type.getRange().get().begin.line,
                        type.getRange().get().begin.column,
                        type.getRange().get().end.line,
                        type.getRange().get().end.column))
                .build();
        javaParserExpressionDTOList.add(javaParserExpressionDTO);
        putExpressionDTO(type.hashCode(), javaParserExpressionDTO);
        return javaParserExpressionDTO;
    }

    private JavaParserExpressionDTO buildExpressionDTO(Long parentExpressionId, ExpressionRelationEnum expressionRelationEnum, Long blockId, SimpleName simpleName) {

        Long expressionId = expressionIdGenerator.nextId();

        JavaParserExpressionDTO javaParserExpressionDTO = JavaParserExpressionDTO.builder()
                .expressionId(expressionId)
                .parentExpressionId(parentExpressionId)
                .expressionRelation(expressionRelationEnum)
                .expression(simpleName.asString())
                .expressionType("NameExpr")
                .blockId(blockId)
                .position(new Position(simpleName.getRange().get().begin.line,
                        simpleName.getRange().get().begin.column,
                        simpleName.getRange().get().end.line,
                        simpleName.getRange().get().end.column))
                .build();

        javaParserExpressionDTOList.add(javaParserExpressionDTO);
        putExpressionDTO(simpleName.hashCode(), javaParserExpressionDTO);

        return javaParserExpressionDTO;
    }


}
