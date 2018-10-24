package net.joedoe.entities;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import net.joedoe.utils.Direction;

import java.util.ArrayList;
import java.util.List;

import static net.joedoe.GameInfo.ONE_TILE;

@SuppressWarnings("unused")
public class Bridge extends StackPane {
    private int row, column;
    private Direction direction;
    @SuppressWarnings("FieldCanBeLocal")
    private List<Line> lines = new ArrayList<>();

    public Bridge(GridEntity startIsle, GridEntity endIsle, Direction direction) {
        int startX = startIsle.getRow();
        int startY = startIsle.getColumn();
        int endX = endIsle.getRow();
        int endY = endIsle.getColumn();
        if (direction == Direction.RIGHT) {
            Line line = new Line(ONE_TILE * 5, ONE_TILE * 2, ONE_TILE, ONE_TILE * 2);
            line.setStroke(Color.RED);
            lines.add(line);
        }

//            for (int i = 0; i <= startX; i++)
//                for (int j = 0; j <= endX; j++) {
//                    Line line = new Line((startX * ONE_TILE) >> 1, (startY + j) * ONE_TILE, ONE_TILE, ONE_TILE);
//                    line.setStroke(Color.RED);
//                    lines.add(line);
//                }
//        else
//            for (int i = 0; i <= startX; i++)
//                for (int j = 0; j <= endX; j++) {
//                    Line line = new Line(startX + j, startY, ONE_TILE, ONE_TILE);
//                    line.setStroke(Color.RED);
//                    lines.add(line);
//                }
        getChildren().addAll(lines);
    }

    public int getColumn() {
        return column;
    }

    public int getRow() {
        return row;
    }
}
