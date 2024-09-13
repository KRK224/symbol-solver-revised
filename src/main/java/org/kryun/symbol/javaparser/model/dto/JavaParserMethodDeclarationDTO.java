package org.kryun.symbol.javaparser.model.dto;

import com.github.javaparser.ast.Node;
import org.kryun.symbol.model.dto.MethodDeclarationDTO;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JavaParserMethodDeclarationDTO extends MethodDeclarationDTO {

    private JavaParserReturnMapperDTO returnMapper;
    private List<JavaParserParameterDTO> parameters;
    private JavaParserBlockDTO ownBlock;

    public void setOwnBlockProperties(JavaParserBlockDTO ownBlock) {
        this.ownBlock = ownBlock;
        this.setOwnBlockId(ownBlock.getBlockId());
    }

    @Override
    public String toString() {
        return "MethodDeclarationDTO{" +
                "methodDeclarationId: " + getMethodDeclId() +
                ", blockId: " + getBlockId() +
                ", belongedClassId: " + getBelongedClassId() +
                ", fullQualifiedNameId : " + getFullQualifiedNameId() +
                ", isFullQualifiedNameIdFromDB : " + getIsFullQualifiedNameIdFromDB() +
                ", name: '" + getName() + '\'' +
                ", modifier: '" + getModifier() + '\'' +
                ", accessModifier: '" + getAccessModifier() + '\'' +
                ", returnMappers: " + returnMapper +
                ", parameters: " + parameters +
                ", position: " + getPosition() +
                "}\n";
    }
}
