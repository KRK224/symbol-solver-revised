package org.kryun.symbol.javaparser.model.dto;

import com.github.javaparser.ast.Node;
import org.kryun.symbol.model.dto.MemberVariableDeclarationDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JavaParserMemberVariableDeclarationDTO extends MemberVariableDeclarationDTO {
    private Node node;
}
