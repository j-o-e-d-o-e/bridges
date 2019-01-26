package net.joedoe.views;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;

class Rules extends BorderPane {

    Rules(EventHandler<Event> listener) {
        setStyle("-fx-background-color: #282828;");
        ToolBar toolBar = new ToolBar("Rules");
        toolBar.setListener(e -> listener.handle(new Event(null)));
        setTop(toolBar);
        setCenter(setLayout());
    }

    private Node setLayout() {
//        TableView

        Button ok = new Button("Ok");
        ok.setOnAction(e -> System.out.println("RULES!"));
        setCenter(ok);
        return ok;
    }
}
