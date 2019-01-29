package net.joedoe.views;

import javafx.event.Event;

class ModeEvent extends Event {
    private Mode mode;

    ModeEvent(Mode mode) {
        super(null);
        this.mode = mode;
    }

    Mode getMode() {
        return mode;
    }
}
