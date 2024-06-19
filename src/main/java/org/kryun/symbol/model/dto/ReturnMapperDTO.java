package org.kryun.symbol.model.dto;

import lombok.Getter;
import lombok.Setter;
import org.kryun.symbol.model.dto.interfaces.FQNReferable;

@Getter
@Setter
public class ReturnMapperDTO implements FQNReferable {

    private Long returnMapperId;
    private Long methodDeclId;
    private Long fullQualifiedNameId;
    private Boolean isFullQualifiedNameIdFromDB = false;
    private String type;
    private Position position;


    @Override
    public String toString() {
        return "ReturnMapperDTO{" +
                "returnMapperId: " + returnMapperId +
                ", methodDeclId: " + methodDeclId +
                ", fullQualifiedNameId : " + fullQualifiedNameId +
                ", type: '" + type + '\'' +
                ", position: " + position +
                '}';
    }

}
