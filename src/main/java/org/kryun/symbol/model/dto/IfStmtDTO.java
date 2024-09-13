package org.kryun.symbol.model.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.kryun.symbol.model.dto.interfaces.HasOwnBlock;

/**
 * DTO for IfStmt
 * ifStmt -> elseStmt(ifStmt or statement or block)
 * ifStmt -> thenStmt(block or statement)
 * ifStmt hierarchy is managed by block hierarchy, you can see the type of ifStmt in blockDTO
 */

@Getter
@Setter
@SuperBuilder
public class IfStmtDTO implements HasOwnBlock {
    private Long ifStmtId;
    private Long conditionExprId;
    private Long blockId;
    private Long ownBlockId;
    private Position position;
}
