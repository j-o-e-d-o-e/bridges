package net.joedoe.utils;

import java.util.Arrays;
import java.util.List;

public enum Direction {
    UP, LEFT, DOWN, RIGHT;

    @SuppressWarnings("unused")
    public Direction getOpposite() {
        if (this == Direction.UP)
            return Direction.DOWN;
        if (this == Direction.LEFT)
            return Direction.RIGHT;
        if (this == Direction.DOWN)
            return Direction.UP;
        else
            return Direction.LEFT;
    }

    @SuppressWarnings("unused")
    public Alignment convertToAlignment() {
        if (this == Direction.RIGHT || this == Direction.LEFT) {
            return Alignment.HORIZONTAL;
        } else
            return Alignment.VERTICAL;
    }
}
