package org.kryun.symbol.javaparser.management;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.stmt.DoStmt;
import com.github.javaparser.ast.stmt.WhileStmt;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.kryun.symbol.javaparser.model.dto.JavaParserBlockDTO;
import org.kryun.symbol.javaparser.model.dto.JavaParserExpressionDTO;
import org.kryun.symbol.javaparser.model.dto.JavaParserForStmtDTO;
import org.kryun.symbol.javaparser.model.dto.JavaParserWhileStmtDTO;
import org.kryun.symbol.model.dto.Position;
import org.kryun.symbol.pkg.IdentifierGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WhileStmtManager {
    private final static Logger logger = LoggerFactory.getLogger(WhileStmtManager.class);
    private final List<JavaParserWhileStmtDTO> javaParserWhileStmtDTOList = new ArrayList<>();
    private final IdentifierGenerator whileStmtIdGenerator = new IdentifierGenerator("while_statement");
    private final BlockManager blockManager;
    private final ExpressionManager expressionManager;

    public WhileStmtManager(BlockManager blockManager, ExpressionManager expressionManager) {
        this.blockManager = blockManager;
        this.expressionManager = expressionManager;
    }

    public List<JavaParserWhileStmtDTO> getWhileStmtDTOList() {
        return this.javaParserWhileStmtDTOList;
    }

    public void clear() {
        this.javaParserWhileStmtDTOList.clear();
    }

    public Map<String, Long> getIdentifierMap() {
        Map<String, Long> identifierMap = new LinkedHashMap<>();
        identifierMap.put(whileStmtIdGenerator.getSymbolType(), whileStmtIdGenerator.getLastId());
        return identifierMap;
    }

    public JavaParserWhileStmtDTO buildWhileOrDoWhileStatement(JavaParserBlockDTO parentBlock, Node node) {
        if (node instanceof WhileStmt) {
            return buildWhileStatement(parentBlock, (WhileStmt) node);
        } else if (node instanceof DoStmt) {
            return buildDoWhileStatement(parentBlock, (DoStmt) node);
        } else {
            logger.error("Unsupported node type: {}", node.getClass().getName());
            return null;
        }
    }

    public JavaParserWhileStmtDTO buildWhileStatement(JavaParserBlockDTO parentBlock, WhileStmt whileStmt) {
        JavaParserBlockDTO ownBlock = blockManager.buildBlock(parentBlock, whileStmt);

        Expression condition = whileStmt.getCondition();
        JavaParserExpressionDTO conditionExpr = expressionManager.buildExpressionRecursively(null, null, ownBlock.getBlockId(), condition);

        JavaParserWhileStmtDTO whileStmtDTO = JavaParserWhileStmtDTO.builder()
                .whileStmtId(whileStmtIdGenerator.nextId())
                .blockId(parentBlock.getBlockId())
                .node(whileStmt)
                .position(Position.getPositionByNode(whileStmt))
                .conditionExprId(conditionExpr.getExpressionId())
                .build();

        whileStmtDTO.setOwnBlockProperties(ownBlock);
        javaParserWhileStmtDTOList.add(whileStmtDTO);
        return whileStmtDTO;
    }

    public JavaParserWhileStmtDTO buildDoWhileStatement(JavaParserBlockDTO parentBlock, DoStmt doStmt) {
        JavaParserBlockDTO ownBlock = blockManager.buildBlock(parentBlock, doStmt);

        Expression condition = doStmt.getCondition();
        JavaParserExpressionDTO conditionExpr = expressionManager.buildExpressionRecursively(null, null, ownBlock.getBlockId(), condition);

        JavaParserWhileStmtDTO doWhileStmtDTO = JavaParserWhileStmtDTO.builder()
                .whileStmtId(whileStmtIdGenerator.nextId())
                .blockId(parentBlock.getBlockId())
                .node(doStmt)
                .position(Position.getPositionByNode(doStmt))
                .conditionExprId(conditionExpr.getExpressionId())
                .build();

        doWhileStmtDTO.setOwnBlockProperties(ownBlock);

        javaParserWhileStmtDTOList.add(doWhileStmtDTO);

        return doWhileStmtDTO;
    }
}
