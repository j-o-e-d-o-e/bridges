package net.joedoe.views;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;

class HighScorePane extends BorderPane {
    private EventHandler<Event> listener;

    HighScorePane() {
        Button ok = new Button("Ok");
        ok.setOnAction(e -> listener.handle(e));
        setCenter(ok);
    }

    void setListener(EventHandler<Event> listener) {
        this.listener = listener;
    }
}
