package net.joedoe.utils;

import static net.joedoe.utils.GameInfo.*;

public class Zoom {
    private static final int ISLE_SIZE_MAX = 100;
    private static final int ISLE_SIZE_MIN = 10;

    public static void in() {
        if (ISLE_SIZE + 10 >= ISLE_SIZE_MAX) return;
        else ISLE_SIZE += 10;
        setIslesOffset();
        ONE_TILE = ISLE_SIZE + ISLE_OFFSET;
        BRIDGE_OVERLAP = (ONE_TILE - ISLE_SIZE) / 2;
        setBridgeOffset();
    }

    public static void out() {
        if (ISLE_SIZE - 10 <= ISLE_SIZE_MIN) return;
        else ISLE_SIZE -= 10;
        setIslesOffset();
        ONE_TILE = ISLE_SIZE + ISLE_OFFSET;
        BRIDGE_OVERLAP = (ONE_TILE - ISLE_SIZE) / 2;
        setBridgeOffset();
    }

    private static void setIslesOffset() {
        if (ISLE_SIZE >= 90) ISLE_OFFSET = 20;
        else if (ISLE_SIZE >= 60) ISLE_OFFSET = 16;
        else if (ISLE_SIZE >= 30) ISLE_OFFSET = 12;
        else ISLE_OFFSET = 8;
    }

    private static void setBridgeOffset() {
        if (ONE_TILE >= 80) BRIDGE_OFFSET = 5;
        if (ONE_TILE >= 60) BRIDGE_OFFSET = 4;
        if (ONE_TILE >= 40) BRIDGE_OFFSET = 3;
        else BRIDGE_OFFSET = 2;
    }
}
