package net.joedoe.views;

import javafx.event.Event;
import net.joedoe.utils.GameManager;

class ModeEvent extends Event {
    private GameManager.Mode mode;

    ModeEvent(GameManager.Mode mode) {
        super(null);
        this.mode = mode;
    }

    GameManager.Mode getMode() {
        return mode;
    }
}
