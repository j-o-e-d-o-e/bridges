package net.joedoe.views;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;

class HighScoreFrame extends BorderPane {
    private CustomMenuBar menuBar;
    private EventHandler<Event> listener;

    HighScoreFrame(CustomMenuBar menuBar) {
        this.menuBar = menuBar;
        setTop(menuBar);
        Button ok = new Button("Ok");
        ok.setOnAction(e -> listener.handle(e));
        setCenter(ok);
    }

    void setListener(EventHandler<Event> listener) {
        this.listener = listener;
    }
}
