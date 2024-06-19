package org.kryun.symbol.model.dto;

import lombok.Getter;
import lombok.Setter;
import org.kryun.symbol.model.dto.interfaces.FQNReferable;


@Getter
@Setter
public class MethodCallExprDTO implements FQNReferable {

    private String name;
    private Long blockId;
    private Long methodCallExprId;
    private Long fullQualifiedNameId;
    private Boolean isFullQualifiedNameIdFromDB = false;
    private Position position;
    private Long expressionId; // 내가 다른 methodCall의 scope or AssignExpr의 Value인 경우만 존재
    private Long scopeExpressionId;
    private String nameExpr;

    @Override
    public String toString() {
        return "MethodCallExprDTO : {" +
                "methodCallExprId : " + methodCallExprId +
                ", blockId : " + blockId +
                ", fullQualifiedNameId : " + fullQualifiedNameId +
                ", scopeExpressionId : " + scopeExpressionId +
                ", name : '" + name + '\'' +
                ", Position : '" + position +
                ", NameExpr : " + nameExpr +
                "}\n";
    }
}