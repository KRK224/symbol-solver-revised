package org.kryun.symbol.javaparser.model.dto;

import org.kryun.symbol.model.dto.ExpressionDTO;
import lombok.experimental.SuperBuilder;


@SuperBuilder
public class JavaParserExpressionDTO extends ExpressionDTO {
    @Override
    public String toString() {
        return "ExpressionDTO : {" +
                "expressionId : " + getExpressionId() +
                ", parentExpressionId : " + getParentExpressionId() +
                ", expressionRelation : " + getExpressionRelation() +
                ", blockId : " + getBlockId() +
                ", expression : " + getExpression() +
                ", expressionType : " + getExpressionType() +
                ", Position : '" + getPosition() + '\'' +
                "}\n";
    }
}
