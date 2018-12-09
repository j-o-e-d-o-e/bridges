package net.joedoe.views;

import javafx.event.Event;

class StatusEvent extends Event {
    private static final long serialVersionUID = 225178481951279734L;
    private String message;

    StatusEvent(String message) {
        super(null);
        this.message = message;
    }

    String getMessage() {
        return message;
    }
}
