package net.joedoe.views;

import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import net.joedoe.utils.Alignment;
import net.joedoe.utils.Coordinate;

import java.util.ArrayList;
import java.util.List;

import static net.joedoe.utils.GameInfo.*;

class BridgeLine {
    private Coordinate start, end;
    private boolean doubleBridge;
        private Line line = new Line();
    private List<Line> lines = new ArrayList<>();

    BridgeLine(Coordinate start, Coordinate end) {
        this.start = start;
        this.end = end;
        line.setStrokeWidth(1.5);
        line.setStroke(Color.CORAL);
//        translateToDoubleLayout(line);
    }

    BridgeLine(Coordinate start, Coordinate end, boolean doubleBridge) {
        this.start = start;
        this.end = end;
        this.doubleBridge = doubleBridge;
        Line line = new Line();
        line.setStrokeWidth(1.5);
        line.setStroke(Color.CORAL);
        lines.add(line);
        if (doubleBridge) {
            line = new Line();
            line.setStrokeWidth(1.5);
            line.setStroke(Color.CORAL);
            lines.add(line);
            translateToDoubleLayout(lines.get(0), lines.get(1));
        } else
            translateToSingleLayout(lines.get(0));
    }

    private void translateToSingleLayout(Line line) {
        if (Alignment.getAlignment(start.getY(), end.getY()) == Alignment.HORIZONTAL) {
            int tiles = Math.abs(start.getX() - end.getX());
            line.setEndX(ONE_TILE * (tiles - 1) + BRIDGE_OVERLAP * 2);
            if (start.getX() < end.getX()) {
                line.setTranslateX(ONE_TILE - BRIDGE_OVERLAP);
            } else {
                line.setTranslateX(-ONE_TILE * (tiles - 1) - BRIDGE_OVERLAP);
            }
        } else {
            int tiles = Math.abs(start.getY() - end.getY());
            line.setEndY(ONE_TILE * (tiles - 1) + BRIDGE_OVERLAP * 2);
            if (start.getY() < end.getY()) {
                line.setTranslateX(ONE_TILE >> 1);
                line.setTranslateY((ONE_TILE >> 1) * tiles);
            } else {
                line.setTranslateX(ONE_TILE >> 1);
                line.setTranslateY(-(ONE_TILE >> 1) * tiles);
            }
        }
    }

    private void translateToDoubleLayout(Line line1, Line line2) {
        if (Alignment.getAlignment(start.getY(), end.getY()) == Alignment.HORIZONTAL) {
            int tiles = Math.abs(start.getX() - end.getX());
            line1.setEndX(ONE_TILE * (tiles - 1) + BRIDGE_OVERLAP * 2);
            line1.setTranslateX(ONE_TILE - BRIDGE_OVERLAP);
            line1.setTranslateY(BRIDGE_OFFSET);
            line2.setEndX(ONE_TILE * (tiles - 1) + BRIDGE_OVERLAP * 2);
            line2.setTranslateX(ONE_TILE - BRIDGE_OVERLAP);
            line2.setTranslateY(-BRIDGE_OFFSET);
        } else {
            int tiles = Math.abs(start.getY() - end.getY());
            line1.setEndY(ONE_TILE * (tiles - 1) + BRIDGE_OVERLAP * 2);
            line1.setTranslateX((ONE_TILE >> 1) + BRIDGE_OFFSET);
            line1.setTranslateY((ONE_TILE >> 1) * tiles);
            line2.setEndY(ONE_TILE * (tiles - 1) + BRIDGE_OVERLAP * 2);
            line2.setTranslateX((ONE_TILE >> 1) - BRIDGE_OFFSET);
            line2.setTranslateY((ONE_TILE >> 1) * tiles);
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

    public List<Line> getLines() {
        return lines;
    }

}
