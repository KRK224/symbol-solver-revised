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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BlockManager {

    private final Logger logger = LoggerFactory.getLogger(BlockManager.class.getName());

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

            javaParserBlockDTO.setPosition(Position.getPositionByNode(node));

            javaParserBlockDTOList.add(javaParserBlockDTO);

            return javaParserBlockDTO;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return javaParserBlockDTOList.get(javaParserBlockDTOList.size() - 1);
        }
    }

    public JavaParserBlockDTO buildBlock(JavaParserBlockDTO parentBlock, Node node) {
        return buildBlock(parentBlock.getDepth() + 1, parentBlock.getBlockId(), node.getMetaModel().getTypeName(), node, parentBlock.getSymbolReferenceId());
    }

    public JavaParserBlockDTO findParentBlock(Node node) {
        // BlockDTO의 Node는 BlockStmt가 아닐 수 있음
        Optional<Node> parentNode = node.getParentNode();
        if (parentNode.isPresent()) {
            Node parent = parentNode.get();
            // 가장 최신 blockDTO 부터 조회
            for (int i = javaParserBlockDTOList.size() - 1; i >= 0; i--) {
                JavaParserBlockDTO block = javaParserBlockDTOList.get(i);
                if (block.getNode().equals(parent)) {
                    return block;
                }
                // 현재 파일을 벗어났다고 판단, !!! CompilationUnit block 생성 시에는 호출 하지 말것 !!!
                if (block.getBlockType().equals("CompilationUnit")) {
                    break;
                }
            }
        }
        // Todo. 이 케이스가 존재하는지 반드시 확인 필요...
        logger.warn("Parent block not found, return last one:: " + node.getMetaModel().getTypeName());
        return javaParserBlockDTOList.get(javaParserBlockDTOList.size() - 1);
    }
}
