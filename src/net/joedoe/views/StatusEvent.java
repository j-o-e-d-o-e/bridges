package net.joedoe.views;

import javafx.event.Event;
import javafx.event.EventType;

class StatusEvent extends Event {
    private String message;

    StatusEvent(String message) {
        super(null);
        this.message = message;
    }

    String getMessage() {
        return message;
    }
}
