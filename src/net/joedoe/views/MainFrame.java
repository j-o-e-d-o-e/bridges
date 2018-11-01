package net.joedoe.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import static net.joedoe.utils.GameInfo.CONTAINER_OFFSET;

public class MainFrame extends BorderPane {
    private Stage window;
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
        newGame.setOnAction(e -> createNewGame());
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
        outerPane.setPadding(new Insets(CONTAINER_OFFSET, CONTAINER_OFFSET, 0, CONTAINER_OFFSET));
        grid = new Grid();
        grid.setStatusListener(this::handle);
        outerPane.getChildren().add(grid);
        return outerPane;
    }

    private Node createControls() {
        VBox vBox = new VBox();
        vBox.setPadding(new Insets(CONTAINER_OFFSET, CONTAINER_OFFSET, CONTAINER_OFFSET, CONTAINER_OFFSET));
        vBox.setSpacing(CONTAINER_OFFSET);
        CheckBox checkBox = new CheckBox("Anzahl fehlender Brücken anzeigen");
        checkBox.setSelected(true);
        checkBox.setOnAction(e -> grid.setShowMissingBridges(checkBox.isSelected()));
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);
        hBox.setSpacing(CONTAINER_OFFSET);
        hBox.setPrefWidth(100);
        Button solveBtn = new Button("Automatisch lösen");
        solveBtn.setMinWidth(hBox.getPrefWidth());
        solveBtn.setOnAction(e -> status.setText(solveBtn.getText()));
        Button nextBtn = new Button("Nächste Brücke");
        nextBtn.setMinWidth(hBox.getPrefWidth());
        nextBtn.setOnAction(e -> status.setText(nextBtn.getText()));
        hBox.getChildren().addAll(solveBtn, nextBtn);
        status = new Label("Das Rätsel ist noch nicht gelöst!");
        vBox.getChildren().addAll(checkBox, hBox, status);
        return vBox;
    }

    private void createNewGame() {
        NewGame newGame = new NewGame();
        newGame.initOwner(window);
        newGame.show();
    }

    private void handle(StatusEvent e) {
        status.setText(e.getMessage());
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
}