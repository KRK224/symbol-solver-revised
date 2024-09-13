package org.kryun.symbol.model.dto;

import com.github.javaparser.ast.Node;

public class Position {
    public int beginLine;
    public int beginColumn;

    public int endLine;
    public int endColumn;

    public Position(int beginLine, int beginColumn, int endLine, int endColumn) {
        this.beginLine = beginLine;
        this.beginColumn = beginColumn;
        this.endLine = endLine;
        this.endColumn = endColumn;
    }

    public static Position getPositionByNode(Node node) {
        if (node.getRange().isPresent())
            return new Position(node.getRange().get().begin.line,
                    node.getRange().get().begin.column,
                    node.getRange().get().end.line,
                    node.getRange().get().end.column);
        return new Position(0, 0, 0, 0);
    }

    @Override
    public String toString() {
        return "{" +
                "beginLine: " + beginLine +
                ", beginColumn: " + beginColumn +
                ", endLine: " + endLine +
                ", endColumn: " + endColumn +
                "}";
    }
}
