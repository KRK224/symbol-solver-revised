package org.kryun.symbol.model.dto;

import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.kryun.global.enums.symbol.SymbolStatusEnum;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SymbolStatusDTO {
    private Long symbolStatusId;
    private Long symbolFileId;
    private Timestamp updatedTime;
    private Long lastSymbolId;
    private SymbolStatusEnum statusEnum;
}
