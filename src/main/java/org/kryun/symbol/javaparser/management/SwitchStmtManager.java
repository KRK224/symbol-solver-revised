package org.kryun.symbol.javaparser.management;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.stmt.SwitchEntry;
import com.github.javaparser.ast.stmt.SwitchStmt;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.kryun.symbol.javaparser.model.dto.JavaParserBlockDTO;
import org.kryun.symbol.javaparser.model.dto.JavaParserExpressionDTO;
import org.kryun.symbol.javaparser.model.dto.JavaParserSwitchStmtDTO;
import org.kryun.symbol.model.dto.Position;
import org.kryun.symbol.pkg.IdentifierGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SwitchStmtManager {
    private final Logger logger = LoggerFactory.getLogger(SwitchStmtManager.class.getName());
    private final List<JavaParserSwitchStmtDTO> javaParserSwitchStmtDTOList = new ArrayList<>();
    private final IdentifierGenerator switchStmtIdGenerator = new IdentifierGenerator("switch_statement");
    private final BlockManager blockManager;
    private final ExpressionManager expressionManager;

    public SwitchStmtManager(BlockManager blockManager, ExpressionManager expressionManager) {
        this.blockManager = blockManager;
        this.expressionManager = expressionManager;
    }

    public List<JavaParserSwitchStmtDTO> getSwitchStmtDTOList() {
        return this.javaParserSwitchStmtDTOList;
    }

    public void clear() {
        this.javaParserSwitchStmtDTOList.clear();
    }

    public Map<String, Long> getidentifierMap() {
        Map<String, Long> identifierMap = new LinkedHashMap<>();
        identifierMap.put(switchStmtIdGenerator.getSymbolType(), switchStmtIdGenerator.getLastId());
        return identifierMap;
    }

    public JavaParserSwitchStmtDTO buildSwitchOrCaseStatement(JavaParserBlockDTO parentBlock, Node node) {
        if (node instanceof SwitchStmt) {
            return buildSwitchStatement(parentBlock, (SwitchStmt) node);
        } else if (node instanceof SwitchEntry) {
            return buildSwitchEntry(parentBlock, (SwitchEntry) node);
        } else {
            logger.error("Unsupported node type: {}", node.getClass().getName());
            return null;
        }
    }

    // SwitchStmt는 BlockStmt 대신 SwitchEntry를 자식으로 가짐
    // 자기 자신을 가상의 Block 취급.
    public JavaParserSwitchStmtDTO buildSwitchStatement(JavaParserBlockDTO parentBlock, SwitchStmt switchStmt) {
        JavaParserBlockDTO ownBlock = blockManager.buildBlock(parentBlock, switchStmt);
        JavaParserExpressionDTO switchSelectorExpr = expressionManager.buildExpressionRecursively(null, null, ownBlock.getBlockId(), switchStmt.getSelector());
        JavaParserSwitchStmtDTO javaParserSwitchStmtDTO = JavaParserSwitchStmtDTO.builder()
                .switchStmtId(switchStmtIdGenerator.nextId())
                .blockId(parentBlock.getBlockId())
                .node(switchStmt)
                .position(Position.getPositionByNode(switchStmt))
                .expressionId(switchSelectorExpr.getExpressionId())
                .build();

        javaParserSwitchStmtDTO.setOwnBlockProperties(ownBlock);

        javaParserSwitchStmtDTOList.add(javaParserSwitchStmtDTO);
        return javaParserSwitchStmtDTO;
    }

    // SwitchEntry 내부에 블록이 없더라도 SwitchEntry 자체가 블록 취급되어 내부 statement를 가질 수 있음
    // 비즈니스 로직에서 SwitchEntry 자식에 block 존재 여부를 확인하는 로직 필요
    public JavaParserSwitchStmtDTO buildSwitchEntry(JavaParserBlockDTO parentBlock, SwitchEntry switchEntry) {
        JavaParserBlockDTO ownBlock = blockManager.buildBlock(parentBlock, switchEntry);
        NodeList<Expression> labels = switchEntry.getLabels();

        JavaParserSwitchStmtDTO switchEntryDTO = JavaParserSwitchStmtDTO.builder()
                .switchStmtId(switchStmtIdGenerator.nextId())
                .blockId(parentBlock.getBlockId())
                .node(switchEntry)
                .position(Position.getPositionByNode(switchEntry))
                .build();

        switchEntryDTO.setOwnBlockProperties(ownBlock);

        if (!labels.isEmpty()) {
            Expression label = labels.get(0);
            JavaParserExpressionDTO labelExpr = expressionManager.buildExpressionRecursively(null, null, ownBlock.getBlockId(), label);
            switchEntryDTO.setExpressionId(labelExpr.getExpressionId());
        }

        javaParserSwitchStmtDTOList.add(switchEntryDTO);

        return switchEntryDTO;
    }
}
