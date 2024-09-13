package org.kryun.symbol.model.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.kryun.symbol.model.dto.interfaces.HasOwnBlock;

@Getter
@Setter
@SuperBuilder
public class ForStmtDTO implements HasOwnBlock {
    private Long forStmtId;
    @Default
    private List<Long> initializerExprIds = new ArrayList<>();
    private Long conditionExprId;
    @Default
    private List<Long> updateExprIds = new ArrayList<>();

    private Long iteratorExprId;
    private Long blockId;
    private Long ownBlockId;
    private Position position;
}
