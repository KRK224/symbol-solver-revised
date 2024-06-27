package org.kryun.symbol.model.dto;


import lombok.Getter;
import lombok.Setter;
import org.kryun.symbol.model.dto.interfaces.FQNReferable;

@Getter
@Setter
public class VariableDeclarationDTO implements FQNReferable {

    private Long variableId;
    private Long blockId;
    private Long fullQualifiedNameId;
    private Boolean isFullQualifiedNameIdFromDB = false;
    private String name;
    private String modifier;
    private String accessModifier;
    private String type;
    private Position position;
    private Long initializerExprId;

    @Override
    public String toString() {
        return "VariableDeclarationDTO : {" +
                "variableId : " + variableId +
                ", blockId : " + blockId +
                ", fullQualifiedNameId : " + fullQualifiedNameId +
                ", name : '" + name + '\'' +
                ", modifier : '" + modifier + '\'' +
                ", accessModifier : '" + accessModifier + '\'' +
                ", type : '" + type + '\'' +
                ", Position : '" + position +
                "}\n";
    }

}
