package org.kryun.symbol.model.dto;


import lombok.Getter;
import lombok.Setter;
import org.kryun.symbol.model.dto.interfaces.FQNReferable;


@Getter
@Setter
public class MethodDeclarationDTO implements FQNReferable {

    private Long methodDeclId;
    private Long blockId;
    private Long belongedClassId;
    private Long fullQualifiedNameId;
    private Boolean isFullQualifiedNameIdFromDB = false;
    private String name;
    private String modifier;
    private String accessModifier;
    private Position position;

    @Override
    public String toString() {
        return "MethodDeclarationDTO{" +
                "methodDeclarationId: " + methodDeclId +
                ", blockId: " + blockId +
                ", belongedClassId: " + belongedClassId +
                ", fullQualifiedNameId : " + fullQualifiedNameId +
                ", isFullQualifiedNameIdFromDB : " + isFullQualifiedNameIdFromDB +
                ", name: '" + name + '\'' +
                ", modifier: '" + modifier + '\'' +
                ", accessModifier: '" + accessModifier + '\'' +
                ", position: " + position +
                "}\n";
    }
}
