package org.kryun.symbol.javaparser.management;

import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.type.Type;
import org.kryun.symbol.javaparser.model.dto.JavaParserBlockDTO;
import org.kryun.symbol.javaparser.model.dto.JavaParserMemberVariableDeclarationDTO;
import org.kryun.symbol.javaparser.model.dto.JavaParserStmtVariableDeclarationDTO;
import org.kryun.symbol.model.dto.Position;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.Getter;

// initializer에 value 안들어감, Initializer의 type 제대로 못가져옴
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

    public JavaParserMemberVariableDeclarationDTO buildVariableDeclInMemberField(JavaParserBlockDTO javaParserBlockDTO, Long belongedClassId, Node node) {
        Long blockId = javaParserBlockDTO.getBlockId();
        JavaParserMemberVariableDeclarationDTO variableDeclarationDTO = new JavaParserMemberVariableDeclarationDTO();
        FieldDeclaration fieldDeclaration = (FieldDeclaration) node;

        String modifierKeyword = "";
        String accessModifierKeyword = "";
        Type variableType = null;
        String type = "";
        String name = "";
        boolean isGeneric = false;
        Optional<Node> initializer = Optional.empty();

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
        for (VariableDeclarator variableDeclarator : variableDeclarators) {
            type = variableDeclarator.getType().asString();
            name = variableDeclarator.getName().asString();

        }

        variableDeclarationDTO.setVariableId(memVarDeclIdGenerator.nextId());
        variableDeclarationDTO.setBlockId(blockId);
        variableDeclarationDTO.setBelongedClassId(belongedClassId);
        variableDeclarationDTO.setType(type);
        variableDeclarationDTO.setName(name);
        variableDeclarationDTO.setModifier(modifierKeyword);
        variableDeclarationDTO.setAccessModifier(accessModifierKeyword);
        variableDeclarationDTO.setNode(node);
        variableDeclarationDTO.setPosition(
                new Position(
                        node.getRange().get().begin.line,
                        node.getRange().get().begin.column,
                        node.getRange().get().end.line,
                        node.getRange().get().end.column));

        javaParserMemberVariableDeclarationDTOList.add(variableDeclarationDTO);
        return variableDeclarationDTO;
    }

    public JavaParserStmtVariableDeclarationDTO buildVariableDeclInMethod(JavaParserBlockDTO javaParserBlockDTO, Node node) {
        Long blockId = javaParserBlockDTO.getBlockId();
        JavaParserStmtVariableDeclarationDTO variableDeclarationDTO = new JavaParserStmtVariableDeclarationDTO();
        VariableDeclarationExpr variableDeclarationExpr = (VariableDeclarationExpr) node;

        String modifierKeyword = "";
        String accessModifierKeyword = "";
        Type variableType = null;
        String type = "";
        String name = "";
        boolean isGeneric = false;

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
        for (VariableDeclarator variableDeclarator : variableDeclarators) {

            type = variableDeclarator.getType().asString();
            name = variableDeclarator.getName().asString();

        }

        variableDeclarationDTO.setVariableId(stmtVarDeclIdGenerator.nextId());
        variableDeclarationDTO.setBlockId(blockId);

        variableDeclarationDTO.setType(type);
        variableDeclarationDTO.setName(name);
        variableDeclarationDTO.setModifier(modifierKeyword);
        variableDeclarationDTO.setAccessModifier(accessModifierKeyword);
        variableDeclarationDTO.setNode(node);
        variableDeclarationDTO.setPosition(
                new Position(
                        node.getRange().get().begin.line,
                        node.getRange().get().begin.column,
                        node.getRange().get().end.line,
                        node.getRange().get().end.column));

        javaParserStmtVariableDeclarationDTOList.add(variableDeclarationDTO);
        return variableDeclarationDTO;
    }

}
