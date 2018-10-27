package net.joedoe.views;

import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import net.joedoe.entities.Bridge;
import net.joedoe.utils.Direction;

import static net.joedoe.utils.GameInfo.*;

class LineFactory {

    static Line createLine(Bridge bridge, Direction direction) {
        Line line = null;
        if (direction == Direction.RIGHT) {
            //plus one since start and end points are diminished by one in bridge constructor
            int length = Math.abs(bridge.getStartColumn() - bridge.getEndColumn()) + 1;
            //endX = length + bridge_extra on right and left side
            line = new Line(0, 0, ONE_TILE * length + BRIDGE_EXTRA * 2, 0);
            line.setTranslateX(-BRIDGE_EXTRA); // bridge extra on the left side, manipulate starting point/origin
            line.setTranslateY(BRIDGE_OFFSET);
            line.setStroke(Color.RED);
        }
        if (direction == Direction.LEFT) {
            int length = Math.abs(bridge.getStartColumn() - bridge.getEndColumn()) + 1;
            line = new Line(0, 0, ONE_TILE * length + BRIDGE_EXTRA * 2, 0);
            //set origin from right isle to left isle and minus bridge_extra
            //initially, origin is in tile to the left of the right isle
            //from there, step length-1 tiles to the left and minus bridge_extra for starting point
            line.setTranslateX(-BRIDGE_EXTRA - ONE_TILE * (length - 1));
            line.setTranslateY(-BRIDGE_OFFSET);
            line.setStroke(Color.PURPLE);
        }
        if (direction == Direction.DOWN) {
            int length = Math.abs(bridge.getStartRow() - bridge.getEndRow()) + 1;
            line = new Line(0, 0, 0, ONE_TILE * length + BRIDGE_EXTRA * 2);
            //set from 0 on left border of tile to middle of the tile minus offset
            line.setTranslateX((ONE_TILE >> 1) + BRIDGE_OFFSET);
            line.setTranslateY((ONE_TILE >> 1) * (length - 1));
            line.setStroke(Color.BLUE);
        }
        if (direction == Direction.UP) {
            int length = Math.abs(bridge.getStartRow() - bridge.getEndRow()) + 1;
            line = new Line(0, 0, 0, ONE_TILE * length + BRIDGE_EXTRA * 2);
            line.setTranslateX((ONE_TILE >> 1) - BRIDGE_OFFSET);
            line.setTranslateY(-(ONE_TILE >> 1) * (length - 1));
            line.setStroke(Color.DARKBLUE);
        }
        return line;
    }
}
