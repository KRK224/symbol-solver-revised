package org.kryun.symbol.javaparser.management;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.stmt.ForEachStmt;
import com.github.javaparser.ast.stmt.ForStmt;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.kryun.symbol.javaparser.model.dto.JavaParserBlockDTO;
import org.kryun.symbol.javaparser.model.dto.JavaParserExpressionDTO;
import org.kryun.symbol.javaparser.model.dto.JavaParserForStmtDTO;
import org.kryun.symbol.model.dto.Position;
import org.kryun.symbol.pkg.IdentifierGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ForStmtManager {
    private final Logger logger = LoggerFactory.getLogger(ForStmtManager.class);
    private final List<JavaParserForStmtDTO> javaParserForStmtDTOList = new ArrayList<>();
    private final IdentifierGenerator forStmtIdGenerator = new IdentifierGenerator("for_statement");
    private final BlockManager blockManager;
    private final ExpressionManager expressionManager;

    public ForStmtManager(BlockManager blockManager, ExpressionManager expressionManager) {
        this.blockManager = blockManager;
        this.expressionManager = expressionManager;
    }

    public Map<String, Long> getIdentifierMap() {
        Map<String, Long> identifierMap = new LinkedHashMap<>();
        identifierMap.put(forStmtIdGenerator.getSymbolType(), forStmtIdGenerator.getLastId());
        return identifierMap;
    }

    public List<JavaParserForStmtDTO> getForStmtDTOList() {
        return this.javaParserForStmtDTOList;
    }

    public void clear() {
        this.javaParserForStmtDTOList.clear();
    }

    public JavaParserForStmtDTO buildForOrForEachStatement(JavaParserBlockDTO parentBlock, Node node) {
        if (node instanceof ForStmt) {
            return buildForStatement(parentBlock, (ForStmt) node);
        } else if (node instanceof ForEachStmt) {
            return buildForEachStatement(parentBlock, (ForEachStmt) node);
        } else {
            logger.error("Unsupported node type: {}", node.getClass().getName());
            return null;
        }
    }

    public JavaParserForStmtDTO buildForStatement(JavaParserBlockDTO parentBlock, ForStmt node) {
        JavaParserBlockDTO ownBlock = blockManager.buildBlock(parentBlock, node);


        Optional<Expression> compare = node.getCompare();
        NodeList<Expression> update = node.getUpdate();
        NodeList<Expression> initialization = node.getInitialization();

        List<Long> initializationExprIds = new ArrayList<>();
        List<Long> updateExprIds = new ArrayList<>();

        JavaParserForStmtDTO forStmtDTO = JavaParserForStmtDTO.builder()
                .forStmtId(forStmtIdGenerator.nextId())
                .blockId(parentBlock.getBlockId())
                .node(node)
                .position(Position.getPositionByNode(node))
                .build();

        forStmtDTO.setOwnBlockProperties(ownBlock);

        if (compare.isPresent()) {
            Expression expression = compare.get();
            JavaParserExpressionDTO compareExpr = expressionManager.buildExpressionRecursively(null, null, ownBlock.getBlockId(), expression);
            forStmtDTO.setConditionExprId(compareExpr.getExpressionId());
        }

        for (Expression expression : initialization) {
            JavaParserExpressionDTO initializationExpr = expressionManager.buildExpressionRecursively(null, null, ownBlock.getBlockId(), expression);
            initializationExprIds.add(initializationExpr.getExpressionId());
        }

        for (Expression expression : update) {
            JavaParserExpressionDTO updateExpr = expressionManager.buildExpressionRecursively(null, null, ownBlock.getBlockId(), expression);
            updateExprIds.add(updateExpr.getExpressionId());
        }

        forStmtDTO.setInitializerExprIds(initializationExprIds);
        forStmtDTO.setUpdateExprIds(updateExprIds);

        javaParserForStmtDTOList.add(forStmtDTO);

        return forStmtDTO;
    }

    public JavaParserForStmtDTO buildForEachStatement(JavaParserBlockDTO parentBlock, ForEachStmt node) {
        JavaParserBlockDTO ownBlock = blockManager.buildBlock(parentBlock, node);

        JavaParserForStmtDTO forStmtDTO = JavaParserForStmtDTO.builder()
                .forStmtId(forStmtIdGenerator.nextId())
                .blockId(parentBlock.getBlockId())
                .node(node)
                .position(Position.getPositionByNode(node))
                .build();

        forStmtDTO.setOwnBlockProperties(ownBlock);

        JavaParserExpressionDTO iterableExpr = expressionManager.buildExpressionRecursively(null, null, ownBlock.getBlockId(), node.getIterable());

        forStmtDTO.setConditionExprId(iterableExpr.getExpressionId());

        javaParserForStmtDTOList.add(forStmtDTO);

        return forStmtDTO;
    }
}
