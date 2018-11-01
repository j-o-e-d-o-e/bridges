package net.joedoe.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import static net.joedoe.utils.GameInfo.CONTAINER_OFFSET;
import static net.joedoe.utils.GameInfo.ONE_TILE;

@SuppressWarnings("WeakerAccess")
public class NewGame extends Stage {

    public NewGame() {
        setTitle("Neues Rätsel");
        Scene scene = new Scene(setLayout(), 400, 400);
        setScene(scene);
    }

    private StackPane setLayout() {
        StackPane pane = new StackPane();
        pane.setPadding(new Insets(CONTAINER_OFFSET, CONTAINER_OFFSET, CONTAINER_OFFSET, CONTAINER_OFFSET));
        VBox outerBox = new VBox();
        outerBox.setAlignment(Pos.TOP_LEFT);
        outerBox.setSpacing(CONTAINER_OFFSET);

        RadioButton autoBtn = new RadioButton("Automatische Größe und Inselanzahl");
        RadioButton customBtn = new RadioButton("Größe und/oder Inselanzahl selbst festlegen");
        Label widthLabel = new Label("Breite:");
        TextField widthTxt = new TextField();
        Label heightLabel = new Label("Höhe:");
        TextField heightTxt = new TextField();
        CheckBox checkBox = new CheckBox("Inselanzahl festlegen");
        Label islesLabel = new Label("Inseln:");
        TextField islesTxt = new TextField();

        GridPane grid = new GridPane();
//        grid.setGridLinesVisible(true);
        grid.setAlignment(Pos.CENTER_LEFT);
        grid.setPadding(new Insets(0, 0, 0, CONTAINER_OFFSET));
        grid.setVgap(CONTAINER_OFFSET);
//        grid.setHgap(CONTAINER_OFFSET);
        grid.getColumnConstraints().add(new ColumnConstraints(ONE_TILE));
        grid.getRowConstraints().add(new RowConstraints(ONE_TILE));
//        VBox innerVBox = new VBox();
        outerBox.setPadding(new Insets(0, 0, 0, CONTAINER_OFFSET));
        grid.add(widthLabel, 0, 0);
        grid.add(widthTxt, 1, 0);
        grid.add(heightLabel, 0, 1);
        grid.add(heightTxt, 1, 1);
        grid.add(checkBox, 0, 2);
        GridPane.setColumnSpan(checkBox,3);
        grid.add(islesLabel, 0, 3);
        grid.add(islesTxt, 1, 3);

        HBox buttons = new HBox();
        buttons.setSpacing(CONTAINER_OFFSET);
        Button cancelBtn = new Button("Abbrechen");
        Button confirmBtn = new Button("OK");
        buttons.getChildren().addAll(cancelBtn, confirmBtn);

        outerBox.getChildren().addAll(autoBtn, customBtn, grid, buttons);
        pane.getChildren().addAll(outerBox);
        return pane;
    }
}
