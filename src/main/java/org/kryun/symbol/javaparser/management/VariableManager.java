package org.kryun.symbol.javaparser.management;

import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import org.kryun.global.enums.symbol.ExpressionRelationEnum;
import org.kryun.symbol.javaparser.model.dto.JavaParserBlockDTO;
import org.kryun.symbol.javaparser.model.dto.JavaParserExpressionDTO;
import org.kryun.symbol.javaparser.model.dto.JavaParserMemberVariableDeclarationDTO;
import org.kryun.symbol.javaparser.model.dto.JavaParserStmtVariableDeclarationDTO;
import org.kryun.symbol.model.dto.Position;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import org.kryun.symbol.pkg.IdentifierGenerator;

@Getter
public class VariableManager {

    private final List<JavaParserMemberVariableDeclarationDTO> javaParserMemberVariableDeclarationDTOList;
    private final List<JavaParserStmtVariableDeclarationDTO> javaParserStmtVariableDeclarationDTOList;

    private final IdentifierGenerator memVarDeclIdGenerator = new IdentifierGenerator("member_var_decl");
    private final IdentifierGenerator stmtVarDeclIdGenerator = new IdentifierGenerator("stmt_var_decl");

    public Map<String, Long> getIdentifierMap() {
        Map<String, Long> identifierMap = new LinkedHashMap<>();
        identifierMap.put(memVarDeclIdGenerator.getSymbolType(), memVarDeclIdGenerator.getLastId());
        identifierMap.put(stmtVarDeclIdGenerator.getSymbolType(), stmtVarDeclIdGenerator.getLastId());
        return identifierMap;
    }

    public VariableManager() {
        this.javaParserMemberVariableDeclarationDTOList = new ArrayList<>();
        this.javaParserStmtVariableDeclarationDTOList = new ArrayList<>();

    }

    public void clear() {
        this.javaParserMemberVariableDeclarationDTOList.clear();
        this.javaParserStmtVariableDeclarationDTOList.clear();
    }

    public List<JavaParserMemberVariableDeclarationDTO> buildVariableDeclInMemberField(JavaParserBlockDTO javaParserBlockDTO, Long belongedClassId, Node node, ExpressionManager expressionManager) {
        Long blockId = javaParserBlockDTO.getBlockId();
        FieldDeclaration fieldDeclaration = (FieldDeclaration) node;
        List<JavaParserMemberVariableDeclarationDTO> variableDeclarationDTOList = new ArrayList<>();

        String modifierKeyword = "";
        String accessModifierKeyword = "";

        // 변수 제어자
        NodeList<Modifier> modifiers = fieldDeclaration.getModifiers();
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
        // 변수 이름, 타입
        NodeList<VariableDeclarator> variableDeclarators = fieldDeclaration.getVariables();
        for (int i = 0; i < variableDeclarators.size(); i++) {
            VariableDeclarator variableDeclarator = variableDeclarators.get(i);

            String type = variableDeclarator.getType().asString();
            String name = variableDeclarator.getName().asString();

            JavaParserMemberVariableDeclarationDTO variableDeclarationDTO = new JavaParserMemberVariableDeclarationDTO();
            variableDeclarationDTO.setVariableId(memVarDeclIdGenerator.nextId());
            variableDeclarationDTO.setBlockId(blockId);
            variableDeclarationDTO.setBelongedClassId(belongedClassId);
            variableDeclarationDTO.setType(type);
            variableDeclarationDTO.setName(name);
            variableDeclarationDTO.setModifier(modifierKeyword);
            variableDeclarationDTO.setAccessModifier(accessModifierKeyword);
            variableDeclarationDTO.setPosition(
                    new Position(
                            i==0 ? node.getRange().get().begin.line:variableDeclarator.getRange().get().begin.line,
                            i==0 ? node.getRange().get().begin.column:variableDeclarator.getRange().get().begin.column,
                            i==0 ? node.getRange().get().end.line:variableDeclarator.getRange().get().end.line,
                            i==0 ? node.getRange().get().end.column:variableDeclarator.getRange().get().end.column));

            javaParserMemberVariableDeclarationDTOList.add(variableDeclarationDTO);
            variableDeclarationDTOList.add(variableDeclarationDTO);
            if(variableDeclarator.getInitializer().isPresent()){
                Expression expression = variableDeclarator.getInitializer().get();
                JavaParserExpressionDTO initializerExpr = expressionManager.buildExpressionRecursively(null, ExpressionRelationEnum.VARIABLE_DECLARATOR_INIT, blockId, expression);
                variableDeclarationDTO.setInitializerExpr(initializerExpr);
                variableDeclarationDTO.setInitializerExprId(initializerExpr.getExpressionId());
            }
        }
        return variableDeclarationDTOList;
    }

    public List<JavaParserStmtVariableDeclarationDTO> buildVariableDeclInMethod(JavaParserBlockDTO javaParserBlockDTO, Node node, ExpressionManager expressionManager) {
        Long blockId = javaParserBlockDTO.getBlockId();
        VariableDeclarationExpr variableDeclarationExpr = (VariableDeclarationExpr) node;
        List<JavaParserStmtVariableDeclarationDTO> variableDeclarationDTOList = new ArrayList<>();

        String modifierKeyword = "";
        String accessModifierKeyword = "";

        // 변수 제어자
        NodeList<Modifier> modifiers = variableDeclarationExpr.getModifiers();
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
        // 변수 이름, 타입
        NodeList<VariableDeclarator> variableDeclarators = variableDeclarationExpr.getVariables();

        for (int i = 0; i < variableDeclarators.size(); i++) {
            VariableDeclarator variableDeclarator = variableDeclarators.get(i);
            String type = variableDeclarator.getType().asString();
            String name = variableDeclarator.getName().asString();
            JavaParserStmtVariableDeclarationDTO variableDeclarationDTO = new JavaParserStmtVariableDeclarationDTO();

            variableDeclarationDTO.setVariableId(stmtVarDeclIdGenerator.nextId());
            variableDeclarationDTO.setBlockId(blockId);

            variableDeclarationDTO.setType(type);
            variableDeclarationDTO.setName(name);
            variableDeclarationDTO.setModifier(modifierKeyword);
            variableDeclarationDTO.setAccessModifier(accessModifierKeyword);
            variableDeclarationDTO.setPosition(
                    new Position(
                            i==0 ? node.getRange().get().begin.line:variableDeclarator.getRange().get().begin.line,
                            i==0 ? node.getRange().get().begin.column:variableDeclarator.getRange().get().begin.column,
                            i==0 ? node.getRange().get().end.line:variableDeclarator.getRange().get().end.line,
                            i==0 ? node.getRange().get().end.column:variableDeclarator.getRange().get().end.column));
            javaParserStmtVariableDeclarationDTOList.add(variableDeclarationDTO);
            variableDeclarationDTOList.add(variableDeclarationDTO);

            if(variableDeclarator.getInitializer().isPresent()){
                Expression expression = variableDeclarator.getInitializer().get();
                JavaParserExpressionDTO initializerExpr = expressionManager.buildExpressionRecursively(null, ExpressionRelationEnum.VARIABLE_DECLARATOR_INIT, blockId, expression);
                variableDeclarationDTO.setInitializerExpr(initializerExpr);
                variableDeclarationDTO.setInitializerExprId(initializerExpr.getExpressionId());
            }
        }
        return variableDeclarationDTOList;
    }


}
