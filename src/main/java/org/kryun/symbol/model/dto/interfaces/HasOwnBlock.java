package org.kryun.symbol.model.dto.interfaces;

import org.kryun.symbol.model.dto.BlockDTO;

public interface HasOwnBlock {
    Long getOwnBlockId();

    void setOwnBlockId(Long ownBlockId);

    default void setOwnBlockProperties(BlockDTO blockDTO) {
        // 상속받은 객체가 blockDTO 자체를 가지고 있을 수 있으므로 생성.
        setOwnBlockId(blockDTO.getBlockId());
    }
}
