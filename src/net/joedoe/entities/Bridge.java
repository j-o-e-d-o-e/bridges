package net.joedoe.entities;

import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import net.joedoe.utils.Direction;

import static net.joedoe.utils.GameInfo.*;

public class Bridge {
    private int row;
    private int column;
    private Line line = new Line();

    public Bridge(GridEntity startIsle, Direction direction, GridEntity endIsle) {
//        if (direction == Direction.LEFT) {
//            row = endIsle.getRow();
//            column = endIsle.getColumn() - 1;
//            int length = startIsle.getColumn() - column - 1;
//            line.setTranslateX(ONE_TILE + CIRCLE_RADIUS * 2 + BRIDGE_EXTRA);
//            line.setTranslateY(-BRIDGE_OFFSET);
//            line.setStartY((ONE_TILE >> 1) + BRIDGE_OFFSET);
//            line.setEndX(ONE_TILE * length - CIRCLE_RADIUS * 2);
//            line.setEndY((ONE_TILE >> 1) + BRIDGE_OFFSET);
//            line.setStroke(Color.RED);
//        }
        if (direction == Direction.LEFT) {
            row = startIsle.getRow();
            column = startIsle.getColumn();
            int length = column - endIsle.getColumn();
            line.setTranslateX(BRIDGE_EXTRA - ONE_TILE);
            line.setTranslateY(-BRIDGE_OFFSET);
            line.setStartY((ONE_TILE >> 1) + BRIDGE_OFFSET);
            line.setEndX(-ONE_TILE * length + CIRCLE_RADIUS *2);
            line.setEndY((ONE_TILE >> 1) + BRIDGE_OFFSET);
            line.setStroke(Color.RED);
        } else if (direction == Direction.RIGHT) {
            row = startIsle.getRow();
            column = startIsle.getColumn();
            int length = endIsle.getColumn() - column;
            line.setTranslateX(ONE_TILE - BRIDGE_EXTRA);
            line.setTranslateY(BRIDGE_OFFSET);
            line.setStartY((ONE_TILE >> 1) + BRIDGE_OFFSET);
            line.setEndX(ONE_TILE * length - CIRCLE_RADIUS * 2);
            line.setEndY((ONE_TILE >> 1) + BRIDGE_OFFSET);
            line.setStroke(Color.PURPLE);
        } else if (direction == Direction.DOWN) {
            row = startIsle.getRow() + 1;
            column = startIsle.getColumn();
            int length = endIsle.getRow() - row;
            line = new Line((ONE_TILE >> 1) - BRIDGE_OFFSET, -BRIDGE_EXTRA, (ONE_TILE >> 1) - BRIDGE_OFFSET, ONE_TILE * length + BRIDGE_EXTRA);
            line.setTranslateX(ONE_TILE - BRIDGE_EXTRA - CIRCLE_RADIUS - BRIDGE_OFFSET);
            line.setStroke(Color.BLUE);
        } else if (direction == Direction.UP) {
            row = startIsle.getRow() - 1;
            column = startIsle.getColumn();
            int length = row - endIsle.getRow() - 1;
            line = new Line((ONE_TILE >> 1) + BRIDGE_OFFSET, ONE_TILE + BRIDGE_EXTRA, (ONE_TILE >> 1) + BRIDGE_OFFSET, -ONE_TILE * length - BRIDGE_EXTRA);
            line.setTranslateX(BRIDGE_OFFSET * 2+ CIRCLE_RADIUS);
            line.setStroke(Color.DARKBLUE);
        }
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public Line getLine() {
        return line;
    }
}