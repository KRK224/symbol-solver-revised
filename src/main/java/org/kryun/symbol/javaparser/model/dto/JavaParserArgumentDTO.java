package org.kryun.symbol.javaparser.model.dto;

import com.github.javaparser.ast.Node;
import org.kryun.symbol.model.dto.ArgumentDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JavaParserArgumentDTO extends ArgumentDTO {

    private Node node;

    @Override
    public String toString() {
        return "ArgumentDTO{" +
                "argumentId: " + getArgumentId() +
                ", methodCallExprId: " + getMethodCallExprId() +
                ", expressionId: " + getExpressionId() +
                ", index: " + getIndex() +
                ", name: '" + getName() + '\'' +
                ", position: " + getPosition() +
                '}';
    }
}
