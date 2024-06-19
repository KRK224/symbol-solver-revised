package org.kryun.symbol.javaparser.model.dto;

import com.github.javaparser.ast.Node;
import org.kryun.symbol.model.dto.ParameterDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JavaParserParameterDTO extends ParameterDTO {

    private Node node;

    @Override
    public String toString() {
        return "ParameterDTO{" +
                "parameterId: " + getParameterId() +
                ", methodDeclarationId: " + getMethodDeclId() +
                ", fullQualifiedNameId : " + getFullQualifiedNameId() +
                ", isFullQualifiedNameIdFromDB : " + getIsFullQualifiedNameIdFromDB() +
                ", index: " + getIndex() +
                ", name: '" + getName() + '\'' +
                ", type: '" + getType() + '\'' +
                ", position: " + getPosition() +
                '}';
    }

}
