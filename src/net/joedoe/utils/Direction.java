package net.joedoe.utils;

public enum Direction {
    UP, LEFT, DOWN, RIGHT;

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

    public Alignment convertToAlignment() {
        if (this == Direction.RIGHT || this == Direction.LEFT) {
            return Alignment.HORIZONTAL;
        } else
            return Alignment.VERTICAL;
    }
}
