package org.kryun.symbol.javaparser.model.dto;

import org.kryun.symbol.model.dto.PackageDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JavaParserPackageDTO extends PackageDTO {

    @Override
    public String toString() {
        return "PackageDTO : {" +
                "packageId : " + getPackageId() +
                ", blockId : " + getBlockId() +
                ", name : '" + getName() + '\'' +
                ", Position : '" + getPosition() +
                "}\n";
    }
}
