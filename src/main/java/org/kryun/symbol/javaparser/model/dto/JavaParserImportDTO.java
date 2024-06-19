package org.kryun.symbol.javaparser.model.dto;

import org.kryun.symbol.model.dto.ImportDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JavaParserImportDTO extends ImportDTO {

    @Override
    public String toString() {
        return "ImportDTO :{" +
                "importId : " + getImportId() +
                ", blockId : " + getBlockId() +
                ", name : '" + getName() + '\'' +
                ", Position : '" + getPosition() +
                "}\n";
    }
}
