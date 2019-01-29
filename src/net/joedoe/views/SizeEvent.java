package net.joedoe.views;

import javafx.event.Event;

class SizeEvent extends Event {
    private SizeChooser.Type type;
    private int width, height, isles;


    SizeEvent(Integer width, Integer height, Integer isles) {
        super(null);
        type = SizeChooser.Type.WIDTH_HEIGHT_ISLES;
        this.width = width;
        this.height = height;
        this.isles = isles;
    }

    SizeEvent(int width, int height) {
        super(null);
        type = SizeChooser.Type.WIDTH_HEIGHT;
        this.width = width;
        this.height = height;
    }

    SizeEvent() {
        super(null);
        type = SizeChooser.Type.AUTO;
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

    public SizeChooser.Type getType() {
        return type;
    }
}