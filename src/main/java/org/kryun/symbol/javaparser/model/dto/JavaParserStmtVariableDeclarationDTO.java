package org.kryun.symbol.javaparser.model.dto;

import com.github.javaparser.ast.Node;
import org.kryun.symbol.model.dto.StmtVariableDeclarationDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JavaParserStmtVariableDeclarationDTO extends StmtVariableDeclarationDTO {
    private Node node;  // Todo. 이거 필요한지 알아보기...

    @Override
    public String toString() {
        return "StmtVariableDeclarationDTO : {" +
                "variableId : " + getVariableId() +
                ", blockId : " + getBlockId() +
                ", name : '" + getName() + '\'' +
                "', nodeType: '" + node.getMetaModel().getTypeName() +
                ", modifier : '" + getModifier() + '\'' +
                ", accessModifier : '" + getAccessModifier() + '\'' +
                ", type : '" + getType() + '\'' +
                ", Position : '" + getPosition() +
                "}\n";
    }
}
