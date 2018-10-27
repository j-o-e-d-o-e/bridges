package net.joedoe.entities;

import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import net.joedoe.utils.Direction;

import static net.joedoe.utils.GameInfo.*;

public class Bridge {
    private int startRow, endRow, startColumn, endColumn;
    private Direction direction;
    private Line line;

    public Bridge(GridEntity startIsle, Direction direction, GridEntity endIsle) {
        this.direction = direction;
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
        line.setStrokeWidth(2);
        if (direction == Direction.RIGHT) {
            int length = Math.abs(startColumn - endColumn) + 1;
            line.setEndX(ONE_TILE * length + BRIDGE_EXTRA * 2);
            line.setTranslateX(-BRIDGE_EXTRA);
            line.setTranslateY(BRIDGE_OFFSET);
            line.setStroke(Color.RED);
        }
        if (direction == Direction.LEFT) {
            int length = Math.abs(startColumn - endColumn) + 1;
            line.setEndX(ONE_TILE * length + BRIDGE_EXTRA * 2);
            line.setTranslateX(-ONE_TILE * (length - 1) - BRIDGE_EXTRA);
            line.setTranslateY(-BRIDGE_OFFSET);
            line.setStroke(Color.PURPLE);
        }
        if (direction == Direction.DOWN) {
            int length = Math.abs(startRow - endRow) + 1;
            line.setEndY(ONE_TILE * length + BRIDGE_EXTRA * 2);
            line.setTranslateX((ONE_TILE >> 1) + BRIDGE_OFFSET);
            line.setTranslateY((ONE_TILE >> 1) * (length - 1));
            line.setStroke(Color.BLUE);
        }
        if (direction == Direction.UP) {
            int length = Math.abs(startRow - endRow) + 1;
            line.setEndY(ONE_TILE * length + BRIDGE_EXTRA * 2);
            line.setTranslateX((ONE_TILE >> 1) - BRIDGE_OFFSET);
            line.setTranslateY(-(ONE_TILE >> 1) * (length - 1));
            line.setStroke(Color.DARKBLUE);
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

    @SuppressWarnings("WeakerAccess")
    public Direction getDirection() {
        return direction;
    }

    public Line getLine() {
        return line;
    }
}