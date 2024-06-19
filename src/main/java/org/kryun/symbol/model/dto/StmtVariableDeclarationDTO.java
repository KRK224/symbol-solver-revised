package org.kryun.symbol.model.dto;

public class StmtVariableDeclarationDTO extends VariableDeclarationDTO {

    @Override
    public String toString() {
        return "StmtVariableDeclarationDTO : {" +
                "variableId : " + getVariableId() +
                ", blockId : " + getBlockId() +
                ", name : '" + getName() + '\'' +
                ", modifier : '" + getModifier() + '\'' +
                ", accessModifier : '" + getAccessModifier() + '\'' +
                ", type : '" + getType() + '\'' +
                ", Position : '" + getPosition() +
                "}\n";
    }
}
