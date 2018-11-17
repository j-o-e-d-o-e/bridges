package net.joedoe.views;

import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import net.joedoe.utils.Alignment;

import static net.joedoe.utils.GameInfo.*;

class BridgeLine extends Line {
    private int xStart, xEnd, yStart, yEnd;

    BridgeLine(int yStart, int xStart, int yEnd, int xEnd) {
        this.yStart = yStart;
        this.xStart = xStart;
        this.yEnd = yEnd;
        this.xEnd = xEnd;
        setStrokeWidth(1.5);
        setStroke(Color.CORAL);
        translateToLayout();
    }

    private void translateToLayout() {
        if (Alignment.getAlignment(yStart, yEnd) == Alignment.HORIZONTAL) {
            int tiles = Math.abs(xStart - xEnd);
            setEndX(ONE_TILE * (tiles - 1) + BRIDGE_OVERLAP * 2);
            if (xStart < xEnd) {
                setTranslateX(ONE_TILE - BRIDGE_OVERLAP);
                setTranslateY(BRIDGE_OFFSET);
            } else {
                setTranslateX(-ONE_TILE * (tiles - 1) - BRIDGE_OVERLAP);
                setTranslateY(-BRIDGE_OFFSET);
            }
        } else {
            int tiles = Math.abs(yStart - yEnd);
            setEndY(ONE_TILE * (tiles - 1) + BRIDGE_OVERLAP * 2);
            if (yStart < yEnd) {
                setTranslateX((ONE_TILE >> 1) + BRIDGE_OFFSET);
                setTranslateY((ONE_TILE >> 1) * tiles);
            } else {
                setTranslateX((ONE_TILE >> 1) - BRIDGE_OFFSET);
                setTranslateY(-(ONE_TILE >> 1) * tiles);
            }
        }
    }

    int getXStart() {
        return xStart;
    }

    int getXEnd() {
        return xEnd;
    }

    int getYStart() {
        return yStart;
    }

    int getYEnd() {
        return yEnd;
    }
}
