package org.kryun.symbol.model.dto;

import lombok.Builder.Default;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.kryun.symbol.model.dto.interfaces.FQNReferable;

@Getter
@Setter
@SuperBuilder
public class ClassDTO implements FQNReferable {

    private Long classId;
    @Default
    private Long packageId = -100L;
    private Long fullQualifiedNameId;
    @Default
    private Boolean isFullQualifiedNameIdFromDB = false;
    private String name;
    private String modifier;
    private String accessModifier;
    private Long blockId;
    private Long ownBlockId;
    private String type;
    @Default
    private Boolean isImplemented = false;
    private String implementClass;
    @Default
    private Boolean isInner = false;
    private String outerClass;
    private Position position;


    @Override
    public String toString() {
        return "ClassDTO : {" +
                "classId : " + classId +
                ", packageId : " + packageId +
                ", blockId : " + blockId +
                ", ownBlockId : " + ownBlockId +
                ", fullQualifiedNameId : " + fullQualifiedNameId +
                ", isFullQualifiedNameIdFromDB : " + isFullQualifiedNameIdFromDB +
                ", name : '" + name + '\'' +
                ", modifier : '" + modifier + '\'' +
                ", accessModifier : '" + accessModifier + '\'' +
                ", type : '" + type + '\'' +
                ", isImplemented : " + isImplemented +
                ", implementClass : '" + implementClass + '\'' +
                ", Position : '" + position +
                "}\n";
    }

}
