package org.kryun.symbol.model.dto;


import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import org.kryun.global.enums.symbol.SymbolStatusEnum;

@Getter
@Setter
public class SymbolStatusDTO {

    private Long symbolStatusId;
    private Long projId;
    private Long refId;
    private Long tagId;
    private Long commitId;
    private SymbolStatusEnum statusEnum;
    private Timestamp createdTime;
    private Timestamp updatedTime;

    public SymbolStatusDTO() {
        this.statusEnum = SymbolStatusEnum.NULL;
    }

    public SymbolStatusDTO(Long projId, Long refId, Long tagId, Long commitId) {
        this.projId = projId;
        this.refId = refId;
        this.tagId = tagId;
        this.commitId = commitId;
        this.statusEnum = SymbolStatusEnum.NULL;
    }

    @Override
    public String toString() {
        return "SymbolStatusDTO {symbolStatusId=" + symbolStatusId +
                ", projId=" + projId +
                ", refId=" + refId +
                ", tagId=" + tagId +
                ", commitId=" + commitId +
                ", statusEnum=" + statusEnum +
                ", createdTime=" + createdTime +
                ", updatedTime=" + updatedTime +
                "}\n";
    }

}
