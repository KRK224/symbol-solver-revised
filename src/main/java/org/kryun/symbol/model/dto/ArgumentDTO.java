package org.kryun.symbol.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArgumentDTO {

    private Long argumentId;
    private Long methodCallExprId;
    private Long expressionId;
    private Integer index;
    private String name;
    private Position position;

    @Override
    public String toString() {
        return "ArgumentDTO{" +
                "argumentId: " + argumentId +
                ", methodCallExprId: " + methodCallExprId +
                ", expressionId: " + expressionId +
                ", index: " + index +
                ", name: '" + name + '\'' +
                ", position: " + position +
                '}';
    }
}
