package net.joedoe.entities;

import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import net.joedoe.utils.Direction;

import static net.joedoe.utils.GameInfo.*;

public class Bridge {
    private int startRow, endRow, startColumn, endColumn;
    private Isle endIsle;
    private Direction direction;
    private Line line;

    public Bridge(Isle startIsle, Direction direction, Isle endIsle) {
        this.direction = direction;
        this.endIsle = endIsle;
        if (direction == Direction.UP) {
            startRow = startIsle.getRow() - 1;
            endRow = endIsle.getRow() + 1;
            startColumn = startIsle.getColumn();
            endColumn = startColumn;
        }
        if (direction == Direction.LEFT) {
            startRow = startIsle.getRow();
            endRow = startRow;
            startColumn = startIsle.getColumn() - 1;
            endColumn = endIsle.getColumn() + 1;
        }
        if (direction == Direction.DOWN) {
            startRow = startIsle.getRow() + 1;
            endRow = endIsle.getRow() - 1;
            startColumn = startIsle.getColumn();
            endColumn = startColumn;
        }
        if (direction == Direction.RIGHT) {
            startRow = startIsle.getRow();
            endRow = startRow;
            startColumn = startIsle.getColumn() + 1;
            endColumn = endIsle.getColumn() - 1;
        }
        line = createLine();
    }

    private Line createLine() {
        Line line = new Line();
        line.setStrokeWidth(1.5);
        line.setStroke(Color.CORAL);
        if (direction == Direction.RIGHT) {
            int length = Math.abs(startColumn - endColumn) + 1;
            line.setEndX(ONE_TILE * length + BRIDGE_OVERLAP * 2);
            line.setTranslateX(-BRIDGE_OVERLAP);
            line.setTranslateY(BRIDGE_OFFSET);
        }
        if (direction == Direction.LEFT) {
            int length = Math.abs(startColumn - endColumn) + 1;
            line.setEndX(ONE_TILE * length + BRIDGE_OVERLAP * 2);
            line.setTranslateX(-ONE_TILE * (length - 1) - BRIDGE_OVERLAP);
            line.setTranslateY(-BRIDGE_OFFSET);
        }
        if (direction == Direction.DOWN) {
            int length = Math.abs(startRow - endRow) + 1;
            line.setEndY(ONE_TILE * length + BRIDGE_OVERLAP * 2);
            line.setTranslateX((ONE_TILE >> 1) + BRIDGE_OFFSET);
            line.setTranslateY((ONE_TILE >> 1) * (length - 1));
        }
        if (direction == Direction.UP) {
            int length = Math.abs(startRow - endRow) + 1;
            line.setEndY(ONE_TILE * length + BRIDGE_OVERLAP * 2);
            line.setTranslateX((ONE_TILE >> 1) - BRIDGE_OFFSET);
            line.setTranslateY(-(ONE_TILE >> 1) * (length - 1));
        }
        return line;
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

    public Direction getDirection() {
        return direction;
    }

    public Line getLine() {
        return line;
    }

    public void setColor(Color color) {
        line.setStroke(color);
    }

    public Isle getEndIsle() {
        return endIsle;
    }
}