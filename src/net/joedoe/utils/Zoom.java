package net.joedoe.utils;

import static net.joedoe.utils.GameInfo.*;
import static net.joedoe.utils.Scale.*;

public class Zoom {
    private static Scale scale = LARGE;

    public static void in() {
        scale = scale.previous();
        zoom();
    }

    public static void out() {
        scale = scale.next();
        zoom();
    }

    public static void zoom(int islesCount) {
        if (islesCount >= 75) scale = X_SMALL;
        else if (islesCount >= 50) scale = SMALL;
        else if (islesCount >= 25) scale = NORMAL;
        else scale = LARGE;
        zoom();
    }

    private static void zoom() {
        ISLE_SIZE = scale.getIsleSize();
        ISLE_OFFSET = scale.getIsleOffset();
        ONE_TILE = ISLE_SIZE + ISLE_OFFSET;
        BRIDGE_OVERLAP = (ONE_TILE - ISLE_SIZE) / 2;
        BRIDGE_OFFSET = scale.getBridgeOffset();
    }
}
