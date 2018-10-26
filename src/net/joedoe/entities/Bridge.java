package net.joedoe.entities;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import net.joedoe.utils.Direction;

import static net.joedoe.utils.GameInfo.*;

public class Bridge extends StackPane {
    @SuppressWarnings("FieldCanBeLocal")
    private int row;
    @SuppressWarnings("FieldCanBeLocal")
    private int column;

    public Bridge(GridEntity startIsle, Direction direction, GridEntity endIsle) {
//        setBorder(new Border(new BorderStroke(Color.YELLOW, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        Line line;
        if (direction == Direction.LEFT) {
            row = startIsle.getRow();
            column = startIsle.getColumn() - 1;
            int length = column - endIsle.getColumn() - 1;
            line = new Line(ONE_TILE + BRIDGE_EXTRA, (ONE_TILE >> 1) - BRIDGE_OFFSET, -ONE_TILE * length - BRIDGE_EXTRA, (ONE_TILE >> 1) - BRIDGE_OFFSET);
            line.setStroke(Color.RED);
        } else if (direction == Direction.RIGHT) {
            row = startIsle.getRow();
            column = startIsle.getColumn() + 1;
            int length = endIsle.getColumn() - column;
            line = new Line(-BRIDGE_EXTRA, (ONE_TILE >> 1) + BRIDGE_OFFSET, ONE_TILE * length + BRIDGE_EXTRA, (ONE_TILE >> 1) + BRIDGE_OFFSET);
            line.setStroke(Color.RED);
        } else if (direction == Direction.DOWN) {
            row = startIsle.getRow() + 1;
            column = startIsle.getColumn();
            int length = endIsle.getRow() - row;
            line = new Line((ONE_TILE >> 1) - BRIDGE_OFFSET, -BRIDGE_EXTRA, (ONE_TILE >> 1) - BRIDGE_OFFSET, ONE_TILE * length + BRIDGE_EXTRA);
            line.setStroke(Color.BLUE);
        } else if (direction == Direction.UP) { // UP
            row = startIsle.getRow() - 1;
            column = startIsle.getColumn();
            int length = row - endIsle.getRow() - 1;
            line = new Line((ONE_TILE >> 1) + BRIDGE_OFFSET, ONE_TILE + BRIDGE_EXTRA, (ONE_TILE >> 1) + BRIDGE_OFFSET, -ONE_TILE * length - BRIDGE_EXTRA);
            line.setStroke(Color.LIGHTBLUE);
        } else
            return;
        getChildren().add(line);
    }

    public int getColumn() {
        return column;
    }

    public int getRow() {
        return row;
    }
}