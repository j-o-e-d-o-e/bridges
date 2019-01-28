package net.joedoe.utils;

import static net.joedoe.utils.GameInfo.*;
import static net.joedoe.utils.Zoom.Scale.*;

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

    enum Scale {
        X_LARGE(70, 20, 4), LARGE(60, 16, 4), NORMAL(50, 12, 3), SMALL(40, 8, 3), X_SMALL(30, 4, 2);
        private int isleSize;
        private int isleOffset;
        private int bridgeOffset;
        private static Scale[] values = values();

        Scale(int isleSize, int isleOffset, int bridgeOffset) {
            this.isleSize = isleSize;
            this.isleOffset = isleOffset;
            this.bridgeOffset = bridgeOffset;
        }

        public int getIsleSize() {
            return isleSize;
        }

        public int getIsleOffset() {
            return isleOffset;
        }

        public int getBridgeOffset() {
            return bridgeOffset;
        }

        private Scale next() {
            return values[(this.ordinal() + 1) % values.length];
        }

        private Scale previous() {
            int index = this.ordinal() - 1;
            if (index < 0) return values[values.length - 1];
            else return values[index];
        }
    }
}
