package net.joedoe.views;

import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import net.joedoe.entities.Isle;

import static net.joedoe.GameInfo.OFFSET;


public class MainFrame extends BorderPane {
    private Stage window;
    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    private Grid grid;
    private Label status;

    public MainFrame(Stage window) {
        this.window = window;
        setTop(createMenuBar());
        setCenter(createBoard());
        setBottom(createControls());
    }

    private MenuBar createMenuBar() {
        MenuBar menuBar = new MenuBar();
        Menu menu = new Menu("Datei");
        MenuItem newGame = new MenuItem("Neues Rätsel");
        MenuItem reset = new MenuItem("Rätsel neu starten");
        MenuItem loadGame = new MenuItem("Rätsel laden");
        MenuItem save = new MenuItem("Rätsel speichern");
        MenuItem saveAs = new MenuItem("Rätsel speichern unter");
        MenuItem exit = new MenuItem("Beenden");
        exit.setAccelerator(new KeyCodeCombination(KeyCode.X, KeyCombination.ALT_ANY));
        exit.setOnAction(e -> close());
        menu.getItems().addAll(newGame, reset, loadGame, save, saveAs, exit);
        menuBar.getMenus().add(menu);
        return menuBar;
    }

    private Node createBoard() {
        //outerPane for padding only
        StackPane outerPane = new StackPane();
        outerPane.setPadding(new Insets(OFFSET, OFFSET, 0, OFFSET));
        Grid grid = new Grid();
        grid.setListener(this::handle);
        outerPane.getChildren().add(grid);
        return outerPane;
    }

    private Node createControls() {
        VBox vBox = new VBox();
        vBox.setPadding(new Insets(OFFSET, OFFSET, OFFSET, OFFSET));
        vBox.setSpacing(OFFSET);
        CheckBox checkBox = new CheckBox("Anzahl fehlender Brücken anzeigen");
        checkBox.setOnAction(e -> grid.setShowMissingBridges(checkBox.isSelected()));
        Button solveBtn = new Button("Automatisch lösen");
        solveBtn.setOnAction(e -> status.setText(solveBtn.getText()));
        Button nextBtn = new Button("Nächste Brücke");
        nextBtn.setOnAction(e -> status.setText(nextBtn.getText()));
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);
        hBox.setSpacing(10);
        hBox.getChildren().addAll(solveBtn, nextBtn);
        status = new Label("Das Rätsel ist noch nicht gelöst!");
        vBox.getChildren().addAll(checkBox, hBox, status);
        return vBox;
    }


    public void close() {
//        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
//        alert.setTitle("Beenden?");
//        alert.setHeaderText("Wirklich beenden?");
//        Optional<ButtonType> result = alert.showAndWait();
//        if (result.get() == ButtonType.OK)
        window.close();
//        else
//            alert.close();
    }

    private void handle(Event event) {
        Isle isle = (Isle) event.getSource();
        int bridges = isle.getBridges();
        status.setText("This isle has " + bridges + " bridges.");
    }
}
