package net.joedoe.views;

import javafx.event.Event;

import static net.joedoe.views.Size.*;

class SizeEvent extends Event {
    private Size size = WIDTH_HEIGHT_ISLES;
    private int width, height, isles;

    SizeEvent(Integer width, Integer height, Integer isles) {
        super(null);
        this.width = width;
        this.height = height;
        this.isles = isles;
    }

    SizeEvent(int width, int height) {
        super(null);
        size = WIDTH_HEIGHT;
        this.width = width;
        this.height = height;
    }

    SizeEvent() {
        super(null);
        size = AUTO;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getIsles() {
        return isles;
    }

    public Size getSize() {
        return size;
    }
}