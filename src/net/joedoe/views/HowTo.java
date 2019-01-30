package net.joedoe.views;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import java.io.File;

import static net.joedoe.utils.GameInfo.CONTAINER_OFFSET;

class HowTo extends BorderPane {

    HowTo(EventHandler<Event> listener) {
        setStyle("-fx-background-color: #282828;");
        ToolBar toolBar = new ToolBar("HowTo");
        toolBar.setListener(e -> listener.handle(new Event(null)));
        setTop(toolBar);
        setCenter(setLayout());
    }

    private Node setLayout() {
        VBox box = new VBox();
        box.setAlignment(Pos.CENTER);
        box.setSpacing(CONTAINER_OFFSET);
        String url = "file:assets" + File.separator + "images" + File.separator + "add.gif";
        Image addBridge = new Image(url);
        ImageView view = new ImageView(addBridge);

        HBox hBox = new HBox();
        Button prev = new Button("Prev");
        prev.setOnAction(e -> System.out.println("Prev"));
        hBox.getChildren().addAll();
        Region regionMiddle = new Region();
        HBox.setHgrow(regionMiddle, Priority.ALWAYS);
        Button next = new Button("Next");
        next.setOnAction(e -> System.out.println("Next"));
        hBox.getChildren().addAll(prev, regionMiddle, next);
        box.getChildren().addAll(view, hBox);
        return box;
    }
}
