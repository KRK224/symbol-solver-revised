package org.kryun.symbol.javaparser.model.dto;

import com.github.javaparser.ast.Node;
import org.kryun.symbol.model.dto.BlockDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JavaParserBlockDTO extends BlockDTO {

    private Node node;

    @Override
    public String toString() {
        return "BlockDTO : {" +
                "blockId : " + getBlockId() +
                ", parentBlockId : " + getParentBlockId() +
                ", depth : " + getDepth() +
                ", blockType : '" + getBlockType() + '\'' +
                // ", srcPath : '" + srcPath + "\"" +
                ", Position : '" + getPosition() + '\'' +
                ", Position : '" + getBracketPosition() + '\'' +
                // "}\n";
                // ", \nblockNode : \n" + blockNode +
                ", symbolReferenceId : " + getSymbolReferenceId() +
                "}\n";
    }
}
