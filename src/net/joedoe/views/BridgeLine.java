package net.joedoe.views;

import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import net.joedoe.utils.Alignment;
import net.joedoe.utils.Coordinate;

import static net.joedoe.utils.GameInfo.*;

class BridgeLine {
    private Coordinate start, end;
    private Alignment alignment;
    private Line line = new Line();

    BridgeLine(Coordinate start, Coordinate end, boolean offset) {
        this.start = start;
        this.end = end;
        alignment = Alignment.getAlignment(start.getY(), end.getY());
        line.setStrokeWidth(1.5);
        line.setStroke(Color.CORAL);
        translateToLayout(offset);
    }

    private void translateToLayout(boolean offset) {
        if (alignment == Alignment.HORIZONTAL) {
            int tiles = Math.abs(start.getX() - end.getX());
            line.setEndX(ONE_TILE * (tiles - 1) + BRIDGE_OVERLAP * 2);
            if (start.getX() < end.getX())
                line.setTranslateX(ONE_TILE - BRIDGE_OVERLAP);
            else
                line.setTranslateX(-ONE_TILE * (tiles - 1) - BRIDGE_OVERLAP);
        } else {
            int tiles = Math.abs(start.getY() - end.getY());
            line.setEndY(ONE_TILE * (tiles - 1) + BRIDGE_OVERLAP * 2);
            if (start.getY() < end.getY())
                line.setTranslateY((ONE_TILE >> 1) * tiles);
            else
                line.setTranslateY(-(ONE_TILE >> 1) * tiles);
        }
        setOffset(offset);
    }

    void setOffset(boolean offset) {
        if (alignment == Alignment.HORIZONTAL) {
            if (offset) {
                if (start.getX() < end.getX())
                    line.setTranslateY(BRIDGE_OFFSET);
                else
                    line.setTranslateY(-BRIDGE_OFFSET);
            } else
                line.setTranslateY(0);
        } else {
            if (offset) {
                if (start.getY() < end.getY())
                    line.setTranslateX((ONE_TILE >> 1) + BRIDGE_OFFSET);
                else
                    line.setTranslateX((ONE_TILE >> 1) - BRIDGE_OFFSET);
            } else
                line.setTranslateX(ONE_TILE >> 1);
        }
    }

    int getStartX() {
        return start.getX();
    }

    int getStartY() {
        return start.getY();
    }

    Coordinate getStart() {
        return start;
    }

    Coordinate getEnd() {
        return end;
    }

    void setStdColor() {
        line.setStroke(STD_COLOR);
    }

    Line getLine() {
        return line;
    }
}