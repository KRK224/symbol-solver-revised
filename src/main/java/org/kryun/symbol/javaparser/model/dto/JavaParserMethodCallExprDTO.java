package org.kryun.symbol.javaparser.model.dto;

import com.github.javaparser.ast.expr.Expression;
import org.kryun.symbol.model.dto.MethodCallExprDTO;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JavaParserMethodCallExprDTO extends MethodCallExprDTO {

    private Expression nameExprNode; // Todo : global dao 할 때 쳐내야 함.
    private List<JavaParserArgumentDTO> arguments;

    @Override
    public String toString() {
        return "MethodCallExprDTO : {" +
                "methodCallExprId : " + getMethodCallExprId() +
                ", blockId : " + getBlockId() +
                ", fullQualifiedNameId : " + getFullQualifiedNameId() +
                ", isFullQualifiedNameIdFromDB : " + getIsFullQualifiedNameIdFromDB() +
                ", scopeExpressionId : " + getScopeExpressionId() +
                ", name : '" + getName() + '\'' +
                ", arguments : '" + arguments + '\'' +
                ", Position : '" + getPosition() +
                ", NameExpr : " + nameExprNode.toString() +
                "}\n";
    }


}