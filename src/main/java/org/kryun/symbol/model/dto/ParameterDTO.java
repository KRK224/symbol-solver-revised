package org.kryun.symbol.model.dto;


import lombok.Getter;
import lombok.Setter;
import org.kryun.symbol.model.dto.interfaces.FQNReferable;

@Getter
@Setter
public class ParameterDTO implements FQNReferable {

    private Long parameterId;
    private Long methodDeclId;
    private Long fullQualifiedNameId;
    private Boolean isFullQualifiedNameIdFromDB = false;
    private Integer index;
    private String name;
    private String type;
    private Position position;
    

    @Override
    public String toString() {
        return "ParameterDTO{" +
                "parameterId: " + parameterId +
                ", methodDeclarationId: " + methodDeclId +
                ", fullQualifiedNameId : " + fullQualifiedNameId +
                ", isFullQualifiedNameIdFromDB : " + isFullQualifiedNameIdFromDB +
                ", index: " + index +
                ", name: '" + name + '\'' +
                ", type: '" + type + '\'' +
                ", position: " + position +
                '}';
    }

}
