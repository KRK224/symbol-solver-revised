package org.kryun.symbol.javaparser.model.dto;

import org.kryun.symbol.model.dto.ClassDTO;

public class JavaParserClassDTO extends ClassDTO {


    @Override
    public String toString() {
        return "ClassDTO : {" +
                "classId : " + getClassId() +
                ", packageId : " + getPackageId() +
                ", blockId : " + getBlockId() +
                ", fullQualifiedNameId : " + getFullQualifiedNameId() +
                ", name : '" + getName() + '\'' +
                ", modifier : '" + getModifier() + '\'' +
                ", accessModifier : '" + getAccessModifier() + '\'' +
                ", type : '" + getType() + '\'' +
                ", isImplemented : " + getIsImplemented() +
                ", implementClass : '" + getImplementClass() + '\'' +
                ", Position : '" + getPosition() +
                "}\n";
    }

}
