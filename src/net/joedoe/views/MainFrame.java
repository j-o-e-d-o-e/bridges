package net.joedoe.views;

import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import static net.joedoe.GameInfo.OFFSET;


public class MainFrame extends BorderPane {
    private Stage window;
    @SuppressWarnings("FieldCanBeLocal")
    private Grid grid;
    private Label status;

    public MainFrame(Stage window) {
        this.window = window;
        setTop(createMenuBar());
        setCenter(createGrid());
        setBottom(createControls());
    }

    private MenuBar createMenuBar() {
        MenuBar menuBar = new MenuBar();
        Menu menu = new Menu("Datei");
        menu.setAccelerator(new KeyCodeCombination(KeyCode.D, KeyCombination.ALT_ANY));
        MenuItem newGame = new MenuItem("Neues Rätsel");
        newGame.setAccelerator(new KeyCodeCombination(KeyCode.N, KeyCombination.ALT_ANY));
        MenuItem reset = new MenuItem("Rätsel neu starten");
        reset.setAccelerator(new KeyCodeCombination(KeyCode.R, KeyCombination.ALT_ANY));
        MenuItem loadGame = new MenuItem("Rätsel laden");
        loadGame.setAccelerator(new KeyCodeCombination(KeyCode.L, KeyCombination.ALT_ANY));
        MenuItem save = new MenuItem("Rätsel speichern");
        save.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.ALT_ANY));
        MenuItem saveAs = new MenuItem("Rätsel speichern unter");
        saveAs.setAccelerator(new KeyCodeCombination(KeyCode.A, KeyCombination.ALT_ANY));
        MenuItem exit = new MenuItem("Beenden");
        exit.setAccelerator(new KeyCodeCombination(KeyCode.B, KeyCombination.ALT_ANY));
        exit.setOnAction(e -> close());
        menu.getItems().addAll(newGame, reset, loadGame, save, saveAs, exit);
        menuBar.getMenus().add(menu);
        return menuBar;
    }


    private StackPane createGrid() {
        StackPane stackPane = new StackPane();
        stackPane.setPadding(new Insets(OFFSET, OFFSET, 0, OFFSET));
        grid = new Grid();
        grid.setListener(this::handle);
        stackPane.getChildren().add(grid);
        return stackPane;
    }

    private VBox createControls() {
        VBox vBox = new VBox();
        vBox.setPadding(new Insets(OFFSET, OFFSET, OFFSET, OFFSET));
        vBox.setSpacing(OFFSET);
        CheckBox checkBox = new CheckBox("Anzahl fehlender Brücken anzeigen");
        checkBox.setOnAction(e -> grid.setShowMissingBridges(checkBox.isSelected()));
        Button solve = new Button("Automatisch lösen");
        solve.setOnAction(e -> status.setText(solve.getText()));
        Button next = new Button("Nächste Brücke");
        next.setOnAction(e -> status.setText(next.getText()));
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);
        hBox.setSpacing(10);
        hBox.getChildren().addAll(solve, next);
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
