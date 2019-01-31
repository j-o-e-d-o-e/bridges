package net.joedoe.views;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.io.File;

import static net.joedoe.utils.GameInfo.CONTAINER_OFFSET;

class NewScore extends BorderPane {
    private EventHandler<NewScoreEvent> listener;

    NewScore(String title, String score) {
        getStylesheets().add("file:assets/css/default.css");
        ToolBar toolBar = new ToolBar("New best score", false);
        setTop(toolBar);
        setCenter(setLayout(title, score));
    }

//    private void setSound() {
//        String soundUrl = "assets" + File.separator + "sounds" + File.separator + "win.wav";
//        Media sound = new Media(new File(soundUrl).toURI().toString());
//        MediaPlayer player = new MediaPlayer(sound);
//        player.play();
//    }

    @SuppressWarnings("Duplicates")
    private StackPane setLayout(String titleTxt, String scoreTxt) {
        StackPane outerPane = new StackPane();
        outerPane.setPadding(new Insets(CONTAINER_OFFSET, CONTAINER_OFFSET, CONTAINER_OFFSET, CONTAINER_OFFSET));

        StackPane innerPane = new StackPane();
        innerPane.setId("border");

        VBox box = new VBox(CONTAINER_OFFSET);
        box.setAlignment(Pos.CENTER);

        Label title = new Label(titleTxt);
        title.setFont(Font.font(20));
        title.setPadding(new Insets(0, 0, CONTAINER_OFFSET, 0));

        HBox hBox = new HBox();
        hBox.setSpacing(CONTAINER_OFFSET);
        hBox.setAlignment(Pos.CENTER);
        Label score = new Label(scoreTxt);
        TextField name = new TextField();
        Button submit = new Button("Submit");
        submit.setPrefWidth(100);
        submit.setOnAction(event -> listener.handle(new NewScoreEvent(name.getText().trim())));
        hBox.getChildren().addAll(score, name, submit);

        box.getChildren().addAll(title, hBox);
        innerPane.getChildren().addAll(box);
        outerPane.getChildren().add(innerPane);
        return outerPane;
    }

    void setNext(EventHandler<NewScoreEvent> listener) {
        this.listener = listener;
    }
}