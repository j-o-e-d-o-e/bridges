package net.joedoe.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import net.joedoe.utils.FileHandler;

import java.io.*;

import static net.joedoe.utils.GameInfo.CONTAINER_OFFSET;

public class MainFrame extends BorderPane {
    private Stage window;
    private Board board;
    private FileHandler fileHandler;
    private Label status;

    public MainFrame(Stage window) {
        this.window = window;
        fileHandler = new FileHandler(window);
        setTop(createMenuBar());
        setCenter(createBoard());
        setBottom(createControls());
        setOnMouseClicked(e -> board.stopAutoSolve());
    }

    private MenuBar createMenuBar() {
        MenuBar menuBar = new MenuBar();
        Menu menu = new Menu("Datei");
        MenuItem newGame = new MenuItem("Neues Rätsel");
        newGame.setOnAction(e -> createNewGame());
        MenuItem reset = new MenuItem("Rätsel neu starten");
        reset.setOnAction(e -> board.reset());
        MenuItem loadGame = new MenuItem("Rätsel laden");
        loadGame.setOnAction(e -> fileHandler.loadGame());
        MenuItem saveGame = new MenuItem("Rätsel speichern");
        saveGame.setOnAction(e-> fileHandler.saveGame());
        MenuItem saveGameAs = new MenuItem("Rätsel speichern unter");
        saveGameAs.setOnAction(e-> fileHandler.saveGame());
        MenuItem exit = new MenuItem("Beenden");
        exit.setAccelerator(new KeyCodeCombination(KeyCode.X, KeyCombination.ALT_ANY));
        exit.setOnAction(e -> close());
        menu.getItems().addAll(newGame, reset, loadGame, saveGame, saveGameAs, exit);
        menuBar.getMenus().add(menu);
        return menuBar;
    }

    private Node createBoard() {
        board = new Board(this::handle);
        return board;
    }

    private Node createControls() {
        VBox vBox = new VBox();
        vBox.setPadding(new Insets(CONTAINER_OFFSET, CONTAINER_OFFSET, CONTAINER_OFFSET, CONTAINER_OFFSET));
        vBox.setSpacing(CONTAINER_OFFSET);
        CheckBox checkBox = new CheckBox("Anzahl fehlender Brücken anzeigen");
        checkBox.setSelected(true);
        checkBox.setOnAction(e -> board.setShowMissingBridges(checkBox.isSelected()));
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);
        hBox.setSpacing(CONTAINER_OFFSET);
        hBox.setPrefWidth(100);
        Button solveBtn = new Button("Automatisch lösen");
        solveBtn.setMinWidth(hBox.getPrefWidth());
        solveBtn.setOnAction(e -> board.startAutoSolve());
        Button nextBtn = new Button("Nächste Brücke");
        nextBtn.setMinWidth(hBox.getPrefWidth());
        nextBtn.setOnAction(e -> board.getNextBridge());
        hBox.getChildren().addAll(solveBtn, nextBtn);
        status = new Label("Noch nicht gelöst.");
        vBox.getChildren().addAll(checkBox, hBox, status);
        return vBox;
    }

    private void createNewGame() {
        board.stopAutoSolve();
        NewGameFrame newGameFrame = new NewGameFrame(board);
        newGameFrame.initOwner(window);
        newGameFrame.show();
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
        board.shutdownAutoSolve();
//        else
//            alert.close();
    }
}