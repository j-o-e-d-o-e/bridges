package net.joedoe.utils;

public enum Alignment {
    HORIZONTAL, VERTICAL;


    public static Alignment getAlignment(int startY, int endY) {
        if (startY == endY)
            return Alignment.HORIZONTAL;
        else
            return Alignment.VERTICAL;
    }
}
