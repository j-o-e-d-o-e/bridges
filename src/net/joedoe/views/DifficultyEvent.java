package net.joedoe.views;

import javafx.event.Event;

class DifficultyEvent extends Event {
    private Difficulty difficulty;

    DifficultyEvent(Difficulty difficulty) {
        super(null);
        this.difficulty = difficulty;
    }

    Difficulty getDifficulty() {
        return difficulty;
    }
}