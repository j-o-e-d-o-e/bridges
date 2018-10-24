package net.joedoe.entities;

import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import net.joedoe.utils.Direction;

import static net.joedoe.utils.GameInfo.*;

public class Bridge extends Pane {
    private int row;
    private int column;

    public Bridge(GridEntity startIsle, GridEntity endIsle) {
//        setBorder(new Border(new BorderStroke(Color.YELLOW, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        Line line;
        if (startIsle.getRow() == endIsle.getRow() && startIsle.getColumn() > endIsle.getColumn()) { // LEFT
            row = startIsle.getRow();
            column = startIsle.getColumn() - 1;
            int length = column - endIsle.getColumn() - 1;
            line = new Line(ONE_TILE, CIRCLE_RADIUS - BRIDGE_OFFSET, -ONE_TILE * length, CIRCLE_RADIUS - BRIDGE_OFFSET);
            line.setStroke(Color.RED);
        } else if (startIsle.getRow() == endIsle.getRow() && startIsle.getColumn() < endIsle.getColumn()) { // RIGHT
            row = startIsle.getRow();
            column = startIsle.getColumn() + 1;
            int length = endIsle.getColumn() - column;
            line = new Line(0, CIRCLE_RADIUS + BRIDGE_OFFSET, ONE_TILE * length, CIRCLE_RADIUS + BRIDGE_OFFSET);
            line.setStroke(Color.PINK);
        } else if (startIsle.getColumn() == endIsle.getColumn() && startIsle.getRow() < endIsle.getRow()) { // DOWN
            row = startIsle.getRow() + 1;
            column = startIsle.getColumn();
            int length = endIsle.getRow() - row;
            line = new Line(CIRCLE_RADIUS - BRIDGE_OFFSET, 0, CIRCLE_RADIUS - BRIDGE_OFFSET, ONE_TILE * length);
            line.setStroke(Color.BLUE);
        } else { // UP
            row = startIsle.getRow() - 1;
            column = startIsle.getColumn();
            int length = row - endIsle.getRow() - 1;
            line = new Line(CIRCLE_RADIUS + BRIDGE_OFFSET, ONE_TILE, CIRCLE_RADIUS + BRIDGE_OFFSET, -ONE_TILE * length);
            line.setStroke(Color.LIGHTBLUE);
        }
        getChildren().add(line);
    }

    public int getColumn() {
        return column;
    }

    public int getRow() {
        return row;
    }
}
