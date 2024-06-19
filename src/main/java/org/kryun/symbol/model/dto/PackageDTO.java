package org.kryun.symbol.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PackageDTO {

    private Long packageId;
    private Long blockId;
    private String name;
    private Position position;

    @Override
    public String toString() {
        return "PackageDTO : {" +
                "packageId : " + packageId +
                ", blockId : " + blockId +
                ", name : '" + name + '\'' +
                ", Position : '" + position +
                "}\n";
    }
}
