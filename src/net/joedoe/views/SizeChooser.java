package net.joedoe.views;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

import java.util.Optional;

import static net.joedoe.utils.GameInfo.*;

/**
 * Dialog zur Generierung eines neuen Spiels.
 */
public class SizeChooser extends BorderPane {
    private RadioButton autoBtn;
    private Label heightLabel, widthLabel, islesLabel;
    private TextField heightTxt, widthTxt, islesTxt;
    private CheckBox checkBox;
    private EventHandler<SizeEvent> listener;

    /**
     * Erzeugt einen Dialog, in dem der Nutzer Angaben bzgl. Breite und H�he des
     * Spielfelds sowie Anzahl der zu platzierenden Inseln t�tigt, um ein neues
     * Spiel zu erzeugen.
     *
     * @param board Spielfeld, an das die Daten des generierten Spiels zur�ckgegeben
     *              werden
     */
    public SizeChooser(EventHandler<Event> listener) {
        getStylesheets().addAll("file:assets/css/default.css", "file:assets/css/size.css");
        ToolBar toolBar = new ToolBar("Free mode");
        toolBar.setListener(e -> listener.handle(new Event(null)));
        setTop(toolBar);
        setCenter(setLayout(listener));
    }

    @SuppressWarnings("Duplicates")
    private StackPane setLayout(EventHandler<Event> listener) {
        StackPane outerPane = new StackPane();
        outerPane.setPadding(new Insets(CONTAINER_OFFSET, CONTAINER_OFFSET, CONTAINER_OFFSET, CONTAINER_OFFSET));

        StackPane innerPane = new StackPane();
        innerPane.setId("border");

        Label title = new Label("Choose a size:");
        title.setFont(Font.font(20));
        title.setPadding(new Insets(0, 0, CONTAINER_OFFSET, 0));

        ToggleGroup radios = new ToggleGroup();
        autoBtn = new RadioButton("Generate randomly");
        autoBtn.setSelected(true);
        autoBtn.setToggleGroup(radios);
        RadioButton customBtn = new RadioButton("Set size and number of isles yourself");
        customBtn.setToggleGroup(radios);
        radios.selectedToggleProperty().addListener(e -> {
            if (autoBtn.isSelected()) {
                heightLabel.setDisable(true);
                heightTxt.setDisable(true);
                widthLabel.setDisable(true);
                widthTxt.setDisable(true);
                checkBox.setDisable(true);
                checkBox.setSelected(false);
                islesLabel.setDisable(true);
                islesTxt.setDisable(true);
            } else {
                heightLabel.setDisable(false);
                heightTxt.setDisable(false);
                widthLabel.setDisable(false);
                widthTxt.setDisable(false);
                checkBox.setDisable(false);
            }
        });
        heightLabel = new Label("Height:");
        heightLabel.setDisable(true);
        heightTxt = new TextField();
        heightTxt.setDisable(true);

        widthLabel = new Label("Width:");
        widthTxt = new TextField();
        widthTxt.setDisable(true);
        widthLabel.setDisable(true);

        checkBox = new CheckBox("Set number of isles");
        checkBox.setDisable(true);
        checkBox.setOnAction(e -> {
            if (islesTxt.isDisabled()) {
                islesLabel.setDisable(false);
                islesTxt.setDisable(false);
            } else {
                islesLabel.setDisable(true);
                islesTxt.setDisable(true);
            }
        });
        islesLabel = new Label("Isles:");
        islesLabel.setDisable(true);
        islesTxt = new TextField();
        islesTxt.setDisable(true);

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.getColumnConstraints().add(new ColumnConstraints(50));
        grid.setVgap(10);
        grid.add(autoBtn, 0, 0);
        GridPane.setColumnSpan(autoBtn, 4);
        grid.add(customBtn, 0, 1);
        GridPane.setColumnSpan(customBtn, 4);
        grid.add(widthLabel, 0, 2);
        grid.add(widthTxt, 1, 2);
        grid.add(heightLabel, 0, 3);
        grid.add(heightTxt, 1, 3);
        grid.add(checkBox, 0, 4);
        GridPane.setColumnSpan(checkBox, 4);
        grid.add(islesLabel, 0, 5);
        grid.add(islesTxt, 1, 5);

        HBox buttons = new HBox(CONTAINER_OFFSET);
        buttons.setAlignment(Pos.CENTER);
        buttons.setPrefWidth(100);
        Button cancelBtn = new Button("Cancel");
        cancelBtn.setMinWidth(buttons.getPrefWidth());
        cancelBtn.setOnAction(e -> listener.handle(new Event(null)));
        Button confirmBtn = new Button("OK");
        confirmBtn.setOnAction(e -> handleInput());
        confirmBtn.setMinWidth(buttons.getPrefWidth());
        buttons.getChildren().addAll(cancelBtn, confirmBtn);

        VBox box = new VBox(CONTAINER_OFFSET);
        box.setAlignment(Pos.CENTER);
        box.getChildren().addAll(title, grid, buttons);
        innerPane.getChildren().addAll(box);
        outerPane.getChildren().add(innerPane);
        return outerPane;
    }

    private void handleInput() {
        if (autoBtn.isSelected()) listener.handle(new SizeEvent());
        else {
            if (!checkBox.isSelected()) {
                try {
                    int width = Integer.parseInt(widthTxt.getText().trim());
                    int height = Integer.parseInt(heightTxt.getText().trim());
                    listener.handle(new SizeEvent(width, height));
                } catch (IllegalArgumentException e) {
                    setAlert("Width and height must be \u2265 " + MIN_WIDTH + " and \u2264 " + MAX_WIDTH + ".");
                }
            } else {
                try {
                    int width = Integer.parseInt(widthTxt.getText().trim());
                    int height = Integer.parseInt(heightTxt.getText().trim());
                    int isles = Integer.parseInt(islesTxt.getText().trim());
                    listener.handle(new SizeEvent(width, height, isles));
                } catch (IllegalArgumentException e) {
                    setAlert("Width and height must be \u2265 " + MIN_WIDTH + " and \u2264 " + MAX_WIDTH
                            + ".\nNumber of isles must be \u2265 " + MIN_ISLES_COUNT
                            + " and \u2264 Width * Height * 0.2.");
                }
            }
        }
    }

    private void setAlert(String text) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Invalid input");
        alert.setHeaderText(text);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) alert.close();
    }

    void setNext(EventHandler<SizeEvent> listener) {
        this.listener = listener;
    }
}