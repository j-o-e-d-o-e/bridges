package net.joedoe.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import net.joedoe.utils.FileHandler;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import static net.joedoe.utils.GameInfo.CONTAINER_OFFSET;

/**
 * Das Hauptfenster, das das Spielfeld und die Steuerung enthält.
 */
public class MainFrame extends BorderPane {
    private Stage window;
    private Board board;
    private Label status;
    private FileChooser fileChooser;
    private String directory;
    private String filepath;

    /**
     * Erzeugt das Hauptfenster und die darin enthaltene Menüleiste, Spielsteuerung
     * sowie das Spielfeld.
     *
     * @param window schließt die Appplikation und erzeugt neue Dialoge
     */
    public MainFrame(Stage window) {
        this.window = window;
        status = new Label();
        fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Bridges (*.bgs)", "*.bgs");
        fileChooser.getExtensionFilters().add(extFilter);
        setTop(createTop());
        setCenter(createBoard());
        setBottom(createControls());
    }

    private Node createTop() {
        VBox box = new VBox();
        box.getChildren().addAll(createMenuBar(), createTopBar());
        return box;
    }

    private Node createMenuBar() {
        MenuBar menuBar = new MenuBar();
        Menu menu = new Menu("\u2630");
        MenuItem newGame = new MenuItem("Neues Rätsel");
        newGame.setOnAction(e -> createNewGame());
        MenuItem reset = new MenuItem("Rätsel neu starten");
        reset.setOnAction(e -> board.reset());
        MenuItem loadGame = new MenuItem("Rätsel laden");
        loadGame.setOnAction(e -> loadGame());
        MenuItem saveGame = new MenuItem("Rätsel speichern");
        saveGame.setOnAction(e -> saveGame());
        MenuItem saveGameAs = new MenuItem("Rätsel speichern unter");
        saveGameAs.setOnAction(e -> saveGameAs());
        MenuItem tutorial = new MenuItem("Tutorial");
        tutorial.setOnAction(e -> showTutorial());
        MenuItem exit = new MenuItem("Beenden");
        exit.setOnAction(e -> close());
        menu.getItems().addAll(newGame, reset, loadGame, saveGame, saveGameAs, tutorial, exit);
        menuBar.getMenus().add(menu);
        return menuBar;
    }

    private Node createTopBar() {
        HBox hBox1 = new HBox();
        hBox1.setAlignment(Pos.CENTER);
        hBox1.setSpacing(CONTAINER_OFFSET);
        ImageView imageView = new ImageView();
        Image image = new Image("file:assets" + File.separator + "images" + File.separator + "coin.png");
        imageView.setImage(image);
        imageView.setPreserveRatio(true);
        imageView.setFitHeight(15);
        Label points = new Label("0");
        points.setFont(new Font(15));
        hBox1.getChildren().addAll(imageView, points);

        HBox hBox2 = new HBox();
        hBox2.setMinWidth(100);
        hBox2.setAlignment(Pos.CENTER_RIGHT);
        hBox2.setSpacing(CONTAINER_OFFSET);
        Button zoomIn = new Button("\uD83D\uDD0D+");
        zoomIn.setOnAction(e -> board.zoomIn());
        Button zoomOut = new Button("\uD83D\uDD0D-");
        zoomOut.setOnAction(e -> board.zoomOut());
        Button sound = new Button("\uD83D\uDD0A");
        sound.setMinWidth(30);
        sound.setOnAction(e -> {
            String txt = sound.getText();
            if (txt.equals("\uD83D\uDD07")) sound.setText("\uD83D\uDD0A");
            else sound.setText("\uD83D\uDD07");
            board.setSound();
        });
        hBox2.getChildren().addAll(zoomIn, zoomOut, sound);

        Region region1 = new Region();
        HBox.setHgrow(region1, Priority.ALWAYS);
        Region region2 = new Region();
        HBox.setHgrow(region2, Priority.ALWAYS);

        HBox box = new HBox();
        box.setPadding(new Insets(CONTAINER_OFFSET, CONTAINER_OFFSET, 0, CONTAINER_OFFSET));
        box.getChildren().addAll(region1, hBox1, region2, hBox2);
        return box;
    }

    private void showTutorial() {
        board.stopAutoSolve();
        Tutorial tutorial = new Tutorial();
        tutorial.initOwner(window);
        tutorial.show();
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
        Button solveBtn = new Button("_Automatisch lösen");
        solveBtn.setMnemonicParsing(true);
        solveBtn.setMinWidth(hBox.getPrefWidth());
        solveBtn.setOnAction(e -> {
            if (board.autoSolverIsRunning()) board.stopAutoSolve();
            else board.startAutoSolve();
        });
        Button nextBtn = new Button("_Nächste Brücke");
        nextBtn.setMnemonicParsing(true);
        nextBtn.setMinWidth(hBox.getPrefWidth());
        nextBtn.setOnAction(e -> board.getNextBridge());
        hBox.getChildren().addAll(solveBtn, nextBtn);
        vBox.getChildren().addAll(checkBox, hBox, status);
        return vBox;
    }

    /**
     * Erzeugt Dialog-Fenster, um ein neues Spiel zu generieren.
     */
    private void createNewGame() {
        board.stopAutoSolve();
        NewGameFrame newGameFrame = new NewGameFrame(board);
        newGameFrame.initOwner(window);
        newGameFrame.show();
    }

    private void loadGame() {
        fileChooser.setTitle("Datei öffnen");
        if (directory != null) fileChooser.setInitialDirectory(new File(directory));
        File file = fileChooser.showOpenDialog(window);
        if (file != null) {
            directory = file.getParent();
            String filepath = file.toString();
            try {
                FileHandler.loadGame(filepath);
            } catch (IOException | IllegalArgumentException e) {
                setAlert("Datei konnte nicht geladen werden.");
                return;
            }
            board.setLoadedGrid();
        }
    }

    private void saveGame() {
        if (filepath == null) {
            saveGameAs();
        } else {
            board.saveGame();
            try {
                FileHandler.saveGame(filepath);
            } catch (IOException e) {
                setAlert("Datei konnte nicht gespeichert werden.");
            }
        }
    }

    private void saveGameAs() {
        fileChooser.setTitle("Datei speichern");
        if (directory != null) fileChooser.setInitialDirectory(new File(directory));
        File file = fileChooser.showSaveDialog(window);
        if (file != null) {
            directory = file.getParent();
            filepath = file.toString();
            board.saveGame();
            try {
                FileHandler.saveGame(filepath);
            } catch (IOException e) {
                setAlert("Datei konnte nicht gespeichert werden.");
            }
        }
    }

    private void setAlert(String text) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Dateiein-/ausgabe");
        alert.setHeaderText(text);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) alert.close();
    }

    private void handle(StatusEvent e) {
        status.setText(e.getStatus());
    }

    /**
     * Schließt die Applikation.
     */
    public void close() {
        window.close();
        board.shutdownAutoSolve();
    }
}