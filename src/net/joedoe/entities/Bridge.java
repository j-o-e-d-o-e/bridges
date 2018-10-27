package net.joedoe.entities;

import net.joedoe.utils.Alignment;
import net.joedoe.utils.Direction;

public class Bridge {
    private int startRow, endRow, startColumn, endColumn;
    private Alignment alignment;

    public Bridge(GridEntity startIsle, Direction direction, GridEntity endIsle) {
        if (direction == Direction.UP) {
            alignment = Alignment.VERTICAL;
            startRow = startIsle.getRow() - 1;
            endRow = endIsle.getRow() + 1;
            startColumn = startIsle.getColumn();
            endColumn = startColumn;
        }
        if (direction == Direction.LEFT) {
            alignment = Alignment.HORIZONTAL;
            startRow = startIsle.getRow();
            endRow = startRow;
            startColumn = startIsle.getColumn() - 1;
            endColumn = endIsle.getColumn() + 1;
        }
        if (direction == Direction.DOWN) {
            alignment = Alignment.VERTICAL;
            startRow = startIsle.getRow() + 1;
            endRow = endIsle.getRow() - 1;
            startColumn = startIsle.getColumn();
            endColumn = startColumn;
        }
        if (direction == Direction.RIGHT) {
            alignment = Alignment.HORIZONTAL;
            startRow = startIsle.getRow();
            endRow = startRow;
            startColumn = startIsle.getColumn() + 1;
            endColumn = endIsle.getColumn() - 1;
        }
    }

    public int getStartRow() {
        return startRow;
    }

    public int getEndRow() {
        return endRow;
    }

    public int getStartColumn() {
        return startColumn;
    }

    public int getEndColumn() {
        return endColumn;
    }

    public Alignment getAlignment() {
        return alignment;
    }
}