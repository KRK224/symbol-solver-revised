package org.kryun.symbol.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberVariableDeclarationDTO extends VariableDeclarationDTO {

    private Long belongedClassId; // 변수가 포함되어 있는 클래스

    @Override
    public String toString() {
        return "MemberVariableDeclarationDTO : {" +
                "variableId : " + getVariableId() +
                ", blockId : " + getBlockId() +
                ", belongedClassId : " + getBelongedClassId() +
                ", fullQualifiedNameId : " + getFullQualifiedNameId() +
                ", name : '" + getName() + '\'' +
                ", modifier : '" + getModifier() + '\'' +
                ", accessModifier : '" + getAccessModifier() + '\'' +
                ", type : '" + getType() + '\'' +
                ", Position : '" + getPosition() +
                "}\n";
    }
}
