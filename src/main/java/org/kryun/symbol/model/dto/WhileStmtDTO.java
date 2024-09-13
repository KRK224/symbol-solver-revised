package org.kryun.symbol.model.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.kryun.symbol.model.dto.interfaces.HasOwnBlock;

@Getter
@Setter
@SuperBuilder
public class WhileStmtDTO implements HasOwnBlock {
    private Long whileStmtId;
    private Long conditionExprId;
    private Long blockId;
    private Long ownBlockId;
    private Position position;
}
