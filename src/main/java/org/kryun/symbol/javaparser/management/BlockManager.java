package org.kryun.symbol.javaparser.management;

import com.github.javaparser.ast.Node;
import java.util.Optional;
import org.kryun.symbol.javaparser.model.dto.JavaParserBlockDTO;
import org.kryun.symbol.model.dto.Position;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.kryun.symbol.pkg.IdentifierGenerator;

public class BlockManager {

    private final List<JavaParserBlockDTO> javaParserBlockDTOList;

    public BlockManager() {
        this.javaParserBlockDTOList = new ArrayList<>();
    }

    public List<JavaParserBlockDTO> getBlockDTOList() {
        return this.javaParserBlockDTOList;
    }

    public void clear() {
        this.javaParserBlockDTOList.clear();
    }

    private final IdentifierGenerator blockIdGenerator = new IdentifierGenerator("block");

    public Map<String, Long> getIdentifierMap() {
        Map<String, Long> identifierMap = new HashMap<>();
        identifierMap.put(blockIdGenerator.getSymbolType(), blockIdGenerator.getLastId());
        return identifierMap;
    }


    public JavaParserBlockDTO buildBlock(Integer depth, Long ParentBlockId, String blockType, Node node, Long symbolReferenceId) {
        try {
            JavaParserBlockDTO javaParserBlockDTO = new JavaParserBlockDTO();

            javaParserBlockDTO.setBlockId(blockIdGenerator.nextId());
            javaParserBlockDTO.setDepth(depth);
            javaParserBlockDTO.setParentBlockId(ParentBlockId);
            javaParserBlockDTO.setBlockType(blockType);
            javaParserBlockDTO.setNode(node);
            javaParserBlockDTO.setSymbolReferenceId(symbolReferenceId);

            if (!node.getRange().isPresent()) {
                throw new Exception("Node range is not present");
            }

            if (blockType.equals("IfStmt")) {
                javaParserBlockDTO.setPosition(
                        node.getParentNode()
                                .map(n -> new Position(
                                        n.getRange().get().begin.line,
                                        n.getRange().get().begin.column,
                                        node.getRange().get().end.line,
                                        node.getRange().get().end.column))
                                .orElse(
                                        new Position(
                                                node.getRange().get().begin.line,
                                                node.getRange().get().begin.column,
                                                node.getRange().get().end.line,
                                                node.getRange().get().end.column)));
            } else {
                javaParserBlockDTO.setPosition(
                        new Position(
                                node.getRange().get().begin.line,
                                node.getRange().get().begin.column,
                                node.getRange().get().end.line,
                                node.getRange().get().end.column));
            }

            javaParserBlockDTO.setBracketPosition(
                    new Position(
                            node.getRange().get().begin.line,
                            node.getRange().get().begin.column,
                            node.getRange().get().end.line,
                            node.getRange().get().end.column));

            if (blockType.equals("SwitchEntry")) {
                node.getChildNodes().stream()
                        .filter(n -> n.getMetaModel().getTypeName().equals("BlockStmt"))
                        .findAny().ifPresentOrElse(blockStmt -> javaParserBlockDTO.setBracketPosition(
                                        new Position(
                                                blockStmt.getRange().isPresent() ? blockStmt.getRange().get().begin.line
                                                        :node.getRange().get().begin.line,
                                                blockStmt.getRange().isPresent() ? blockStmt.getRange().get().begin.column
                                                        :node.getRange().get().begin.column,
                                                blockStmt.getRange().isPresent() ? blockStmt.getRange().get().end.line
                                                        :node.getRange().get().end.line,
                                                blockStmt.getRange().isPresent() ? blockStmt.getRange().get().end.column
                                                        :node.getRange().get().end.column)),
                                () -> javaParserBlockDTO.setBracketPosition(new Position(0, 0, 0, 0)));
            }

            if (node.getMetaModel().getTypeName().equals("ExpressionStmt")) {
                javaParserBlockDTO.setBracketPosition(new Position(0, 0, 0, 0));
            }

            javaParserBlockDTOList.add(javaParserBlockDTO);

            return javaParserBlockDTO;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return javaParserBlockDTOList.get(javaParserBlockDTOList.size() - 1);
        }

    }
}
