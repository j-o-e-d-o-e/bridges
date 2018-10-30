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

    public static Direction calculcateDirection(int x1, int x2, int y1, int y2) {
        if (x1 == x2)
            if (y1 < y2)
                return Direction.UP;
            else
                return Direction.DOWN;
        else if (x1 < x2)
            return Direction.LEFT;
        else
            return Direction.RIGHT;
    }

    public Alignment convertToAlignment() {
        if (this == Direction.RIGHT || this == Direction.LEFT) {
            return Alignment.HORIZONTAL;
        } else
            return Alignment.VERTICAL;
    }
}
