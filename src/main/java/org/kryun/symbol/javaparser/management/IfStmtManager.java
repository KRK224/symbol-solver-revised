package org.kryun.symbol.javaparser.management;

import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.stmt.IfStmt;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.kryun.symbol.javaparser.model.dto.JavaParserBlockDTO;
import org.kryun.symbol.javaparser.model.dto.JavaParserIfStmtDTO;
import org.kryun.symbol.model.dto.Position;
import org.kryun.symbol.pkg.IdentifierGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IfStmtManager {
    private final Logger logger = LoggerFactory.getLogger(IfStmtManager.class.getName());
    private final List<JavaParserIfStmtDTO> javaParserIfStmtDTOList = new ArrayList<>();
    private final IdentifierGenerator ifStmtIdGenerator = new IdentifierGenerator("if_statement");
    private final BlockManager blockManager;
    private final ExpressionManager expressionManager;

    public IfStmtManager(BlockManager blockManager, ExpressionManager expressionManager) {
        this.blockManager = blockManager;
        this.expressionManager = expressionManager;
    }

    public Map<String, Long> getIdentifierMap() {
        Map<String, Long> identifierMap = new LinkedHashMap<>();
        identifierMap.put(ifStmtIdGenerator.getSymbolType(), ifStmtIdGenerator.getLastId());
        return identifierMap;
    }

    public List<JavaParserIfStmtDTO> getIfStmtDTOList() {
        return this.javaParserIfStmtDTOList;
    }

    public void clear() {
        this.javaParserIfStmtDTOList.clear();
    }

    public JavaParserIfStmtDTO buildIfStatement(JavaParserBlockDTO parentBlockDTO, IfStmt ifStmt) {
        JavaParserBlockDTO ownBlock = blockManager.buildBlock(parentBlockDTO, ifStmt);
        Expression condition = ifStmt.getCondition();
        Long conditionExpressionId = expressionManager.buildExpressionRecursively(null, null, ownBlock.getBlockId(), condition).getExpressionId();
        JavaParserIfStmtDTO javaParserIfStmtDTO = JavaParserIfStmtDTO.builder()
                .ifStmtId(ifStmtIdGenerator.nextId())
                .blockId(parentBlockDTO.getBlockId())
                .node(ifStmt)
                .position(Position.getPositionByNode(ifStmt))
                .conditionExprId(conditionExpressionId)
                .build();

        javaParserIfStmtDTO.setOwnBlockProperties(ownBlock);
        javaParserIfStmtDTOList.add(javaParserIfStmtDTO);
        return javaParserIfStmtDTO;
    }
}
