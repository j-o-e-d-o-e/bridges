package net.joedoe.views;

import javafx.event.Event;

class NewScoreEvent extends Event {
    private String name;

    NewScoreEvent(String name) {
        super(null);
        this.name = name;
    }

    String getName() {
        return name;
    }
}
