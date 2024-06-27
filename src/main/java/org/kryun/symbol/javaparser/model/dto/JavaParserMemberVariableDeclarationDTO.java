package org.kryun.symbol.javaparser.model.dto;

import com.github.javaparser.ast.Node;
import java.util.Optional;
import org.kryun.symbol.model.dto.MemberVariableDeclarationDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JavaParserMemberVariableDeclarationDTO extends MemberVariableDeclarationDTO {
    private JavaParserExpressionDTO initializerExpr;

    @Override
    public String toString() {
        return "MemberVariableDeclarationDTO : {" +
                "variableId : " + getVariableId() +
                ", blockId : " + getBlockId() +
                ", name : '" + getName() + '\'' +
                ", modifier : '" + getModifier() + '\'' +
                ", accessModifier : '" + getAccessModifier() + '\'' +
                ", initializerExpr : '" + Optional.ofNullable(initializerExpr).map(JavaParserExpressionDTO::getExpression).orElse(null) + '\'' +
                ", type : '" + getType() + '\'' +
                ", Position : '" + getPosition() +
                "}\n";
    }
}
