package net.joedoe.views;

import javafx.event.Event;
import javafx.event.EventType;

class StatusEvent extends Event {
    private String message;

    StatusEvent(EventType<? extends Event> eventType, String message) {
        super(eventType);
        this.message = message;
    }

    String getMessage() {
        return message;
    }
}
