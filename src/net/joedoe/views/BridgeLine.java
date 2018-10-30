package net.joedoe.views;

import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import net.joedoe.utils.Alignment;

import static net.joedoe.utils.GameInfo.*;

class BridgeLine extends Line {
    private int x1, x2, y1, y2;

    BridgeLine(int x1, int x2, int y1, int y2, Alignment alignment) {
        this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;
        setStrokeWidth(1.5);
        setStroke(Color.CORAL);
        translateToLayout(alignment);
    }

    private void translateToLayout(Alignment alignment) {
        if (alignment == Alignment.HORIZONTAL) {
            int tiles = Math.abs(x1 - x2);
            setEndX(ONE_TILE * (tiles - 1) + BRIDGE_OVERLAP * 2);
            if (x1 < x2) {
                setTranslateX(ONE_TILE - BRIDGE_OVERLAP);
                setTranslateY(BRIDGE_OFFSET);
            } else {
                setTranslateX(-ONE_TILE * (tiles - 1) - BRIDGE_OVERLAP);
                setTranslateY(-BRIDGE_OFFSET);
            }
        } else {
            int tiles = Math.abs(y1 - y2);
            setEndY(ONE_TILE * (tiles - 1) + BRIDGE_OVERLAP * 2);
            if (y1 < y2) {
                setTranslateX((ONE_TILE >> 1) + BRIDGE_OFFSET);
                setTranslateY((ONE_TILE >> 1) * tiles);
            } else {
                setTranslateX((ONE_TILE >> 1) - BRIDGE_OFFSET);
                setTranslateY(-(ONE_TILE >> 1) * tiles);
            }
        }
    }

    int getX1() {
        return x1;
    }

    int getX2() {
        return x2;
    }

    int getY1() {
        return y1;
    }

    int getY2() {
        return y2;
    }
}
