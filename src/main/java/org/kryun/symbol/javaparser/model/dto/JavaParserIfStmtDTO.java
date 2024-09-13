package org.kryun.symbol.javaparser.model.dto;

import com.github.javaparser.ast.Node;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.kryun.symbol.model.dto.BlockDTO;
import org.kryun.symbol.model.dto.IfStmtDTO;

@Getter
@SuperBuilder
public class JavaParserIfStmtDTO extends IfStmtDTO {
    private JavaParserBlockDTO ownBlock;
    private Node node;

    // set OwnBlockDTO.
    @Override
    public void setOwnBlockProperties(BlockDTO blockDTO) {
        this.ownBlock = blockDTO instanceof JavaParserBlockDTO ? (JavaParserBlockDTO) blockDTO:null;
        this.setOwnBlockId(blockDTO.getBlockId());
    }
}
