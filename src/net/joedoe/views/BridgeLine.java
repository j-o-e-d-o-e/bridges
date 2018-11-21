package net.joedoe.views;

import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import net.joedoe.utils.Alignment;
import net.joedoe.utils.Coordinate;

import static net.joedoe.utils.GameInfo.*;

class BridgeLine {
    private Coordinate start, end;
    private Line line = new Line();

    BridgeLine(Coordinate start, Coordinate end) {
        this.start = start;
        this.end = end;
        line.setStrokeWidth(1.5);
        line.setStroke(Color.CORAL);
        translateToLayout();
    }

    private void translateToLayout() {
        if (Alignment.getAlignment(start.getY(), end.getY()) == Alignment.HORIZONTAL) {
            int tiles = Math.abs(start.getX() - end.getX());
            line.setEndX(ONE_TILE * (tiles - 1) + BRIDGE_OVERLAP * 2);
            if (start.getX() < end.getX()) {
                line.setTranslateX(ONE_TILE - BRIDGE_OVERLAP);
                line.setTranslateY(BRIDGE_OFFSET);
            } else {
                line.setTranslateX(-ONE_TILE * (tiles - 1) - BRIDGE_OVERLAP);
                line.setTranslateY(-BRIDGE_OFFSET);
            }
        } else {
            int tiles = Math.abs(start.getY() - end.getY());
            line.setEndY(ONE_TILE * (tiles - 1) + BRIDGE_OVERLAP * 2);
            if (start.getY() < end.getY()) {
                line.setTranslateX((ONE_TILE >> 1) + BRIDGE_OFFSET);
                line.setTranslateY((ONE_TILE >> 1) * tiles);
            } else {
                line.setTranslateX((ONE_TILE >> 1) - BRIDGE_OFFSET);
                line.setTranslateY(-(ONE_TILE >> 1) * tiles);
            }
        }
    }

    int getStartX() {
        return start.getX();
    }

    int getEndX() {
        return end.getX();
    }

    int getStartY() {
        return start.getY();
    }

    int getEndY() {
        return end.getY();
    }

    void setStdColor() {
        line.setStroke(STD_COLOR);
    }

    Line getLine() {
        return line;
    }
}
