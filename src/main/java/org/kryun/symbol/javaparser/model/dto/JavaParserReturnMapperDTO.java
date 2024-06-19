package org.kryun.symbol.javaparser.model.dto;

import com.github.javaparser.ast.Node;
import org.kryun.symbol.model.dto.ReturnMapperDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JavaParserReturnMapperDTO extends ReturnMapperDTO {
    private Node node;  // Todo. 이거 필요한지 알아보기...

    @Override
    public String toString() {
        return "ReturnMapperDTO{" +
                "returnMapperId: " + getReturnMapperId() +
                ", methodDeclId: " + getMethodDeclId() +
                ", fullQualifiedNameId : " + getFullQualifiedNameId() +
                ", isFullQualifiedNameIdFromDB : " + getIsFullQualifiedNameIdFromDB() +
                ", type: '" + getType() + '\'' +
                ", position: " + getPosition() +
                '}';
    }

}
