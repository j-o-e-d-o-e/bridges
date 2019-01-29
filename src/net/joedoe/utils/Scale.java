package net.joedoe.utils;

public enum Scale {
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

    Scale next() {
        return values[(this.ordinal() + 1) % values.length];
    }

    Scale previous() {
        int index = this.ordinal() - 1;
        if (index < 0) return values[values.length - 1];
        else return values[index];
    }
}
