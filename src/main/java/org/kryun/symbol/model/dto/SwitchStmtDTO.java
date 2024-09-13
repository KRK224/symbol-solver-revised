package org.kryun.symbol.model.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.kryun.symbol.model.dto.interfaces.HasOwnBlock;

@Getter
@Setter
@SuperBuilder
public class SwitchStmtDTO implements HasOwnBlock {
    private Long switchStmtId;
    private Long blockId;
    private Long ownBlockId;
    private Long expressionId;
    private Position position;
}
