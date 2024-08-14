package org.kryun.symbol.javaparser.model.dto;

import lombok.Getter;
import lombok.Setter;
import org.kryun.symbol.model.dto.ClassDTO;


@Getter
public class JavaParserClassDTO extends ClassDTO {

    // toString에 넣지 말 것
    private JavaParserBlockDTO ownBlock;

    public void setOwnBlockProperties(JavaParserBlockDTO ownBlock) {
        this.ownBlock = ownBlock;
        this.setOwnBlockId(ownBlock.getBlockId());
    }

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
