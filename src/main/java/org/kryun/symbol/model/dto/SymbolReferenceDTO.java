package org.kryun.symbol.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SymbolReferenceDTO {

    private Long symbolReferenceId;
    private Long symbolStatusId;
    private String srcPath;

    @Override
    public String toString() {
        return "SymbolReferenceDTO : {" +
                "symbolReferenceId : " + symbolReferenceId + "\n" +
                ", symbolStatusId : " + symbolStatusId + "\n" +
                ", srcPath : " + srcPath + "\n" +
                "} \n";
    }
}
