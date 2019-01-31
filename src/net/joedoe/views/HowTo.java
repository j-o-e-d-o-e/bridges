package net.joedoe.views;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.io.File;

import static net.joedoe.utils.GameInfo.CONTAINER_OFFSET;

class HowTo extends BorderPane {
    private int count;
    private Label title;
    private ImageView view;
    private Label info;
    private String[] titles = new String[3];
    private Image[] images = new Image[3];
    private String[] infos = new String[3];

    HowTo(EventHandler<Event> listener) {
        getStylesheets().add("file:assets/css/default.css");
        ToolBar toolBar = new ToolBar("How To");
        toolBar.setListener(e -> listener.handle(new Event(null)));
        setTop(toolBar);
        setTitles();
        setImages();
        setInfos();
        setCenter(setLayout());
    }

    private void setTitles() {
        titles[0] = "Goal of the Game (1/3)";
        titles[1] = "Isles (2/3)";
        titles[2] = "Bridges (3/3)";
    }

    private void setImages() {
        for (int i = 0; i < images.length; i++) {
            Image image = new Image("file:assets" + File.separator + "images" + File.separator + "howto" + i + ".gif");
            images[i] = image;
        }
    }

    private void setInfos() {
        infos[0] = "The goal of the game is to connect all isles with each other directly or indirectly via bridges, i.e. each isle must be accessible from another isle.";
        infos[1] = "Isles need as many bridges as its number indicates. To add (or remove) a bridge, tap (long) on or near an isle in the desired direction.";
        infos[2] = "Bridges are horizontal or vertical and cannot collide with isles or other bridges. There cannot be more than two bridges between two isles.";
    }

    @SuppressWarnings("Duplicates")
    private Node setLayout() {
        StackPane outerPane = new StackPane();
        outerPane.setPadding(new Insets(CONTAINER_OFFSET, CONTAINER_OFFSET, CONTAINER_OFFSET, CONTAINER_OFFSET));

        StackPane innerPane = new StackPane();
        innerPane.setBorder(new Border(
                new BorderStroke(Color.GREY, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));

        VBox vBox = new VBox();
        vBox.setMaxWidth(300);
        vBox.setMaxHeight(450);
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(CONTAINER_OFFSET);

        title = new Label(titles[0]);
        title.setFont(new Font(14));

        StackPane viewPane = new StackPane();
        viewPane.setBorder(new Border(
                new BorderStroke(Color.GREY, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        view = new ImageView(images[0]);
        view.setPreserveRatio(true);
        view.setFitHeight(vBox.getMaxHeight());
        viewPane.getChildren().add(view);

        info = new Label(infos[0]);
        info.setAlignment(Pos.TOP_LEFT);
        info.setMinHeight(50);
        info.setWrapText(true);

        HBox controls = new HBox();
        Button prev = new Button("Prev");
        prev.setOnAction(e -> prev());
        controls.getChildren().addAll();
        Region regionMiddle = new Region();
        HBox.setHgrow(regionMiddle, Priority.ALWAYS);
        Button next = new Button("Next");
        next.setOnAction(e -> next());
        controls.getChildren().addAll(prev, regionMiddle, next);

        vBox.getChildren().addAll(title, viewPane, info, controls);
        innerPane.getChildren().add(vBox);
        outerPane.getChildren().add(innerPane);
        return outerPane;
    }

    private void prev() {
        if (count == 0) count = images.length - 1;
        else count--;
        update();
    }

    private void next() {
        count = (count + 1) % images.length;
        update();
    }

    private void update() {
        title.setText(titles[count]);
        view.setImage(images[count]);
        info.setText(infos[count]);
    }
}
