package org.kryun.symbol.model.dto;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.kryun.global.enums.symbol.ExpressionRelationEnum;

@Getter
@SuperBuilder
public class ExpressionDTO {

    private Long expressionId;
    private Long parentExpressionId;
    private ExpressionRelationEnum expressionRelation;
    private String expression;
    private String expressionType;
    private Long blockId;
    private Position position;

    @Override
    public String toString() {
        return "ExpressionDTO : {" +
                "expressionId : " + expressionId +
                ", parentExpressionId : " + parentExpressionId +
                ", expressionRelation : " + expressionRelation +
                ", blockId : " + blockId +
                ", expression : " + expression +
                ", expressionType : " + expressionType +
                ", Position : '" + position + '\'' +
                "}\n";
    }
}
