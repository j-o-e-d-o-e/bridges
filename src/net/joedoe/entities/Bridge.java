package net.joedoe.entities;

import javafx.scene.shape.Line;
import net.joedoe.utils.Alignment;
import net.joedoe.utils.Direction;

public class Bridge {
    private int startRow, endRow, startColumn, endColumn;
    private Alignment alignment;

    public Bridge(GridEntity startIsle, Direction direction, GridEntity endIsle) {
        if (direction == Direction.LEFT || direction == Direction.RIGHT)
            alignment = Alignment.HORIZONTAL;
        else
            alignment = Alignment.VERTICAL;
        startRow = startIsle.getRow();
        endRow = endIsle.getRow();
        startColumn = startIsle.getColumn();
        endColumn = endIsle.getColumn();

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