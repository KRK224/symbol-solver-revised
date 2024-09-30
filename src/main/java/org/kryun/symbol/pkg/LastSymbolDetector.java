package org.kryun.symbol.pkg;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.kryun.symbol.model.dto.Position;

/**
 * find stackoverflow symbol
 */
@Getter
@Setter
@ToString
public class LastSymbolDetector {
    private String srcPath;
    private String symbolType;
    private String symbolName;
    private Position symbolPosition;

    public void saveLastSymbol(String symbolType, String symbolName, Position symbolPosition) {
        this.symbolType = symbolType;
        this.symbolName = symbolName;
        this.symbolPosition = symbolPosition;
    }
}
