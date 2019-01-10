package net.joedoe.views;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import net.joedoe.utils.FileHandler;
import net.joedoe.utils.GameManager;
import net.joedoe.utils.Timer;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import static net.joedoe.utils.GameInfo.CONTAINER_OFFSET;
import static net.joedoe.utils.GameManager.Mode;
import static net.joedoe.utils.GameManager.getInstance;
import static net.joedoe.views.StatusEvent.Status;

/**
 * Das Hauptfenster, das das Spielfeld und die Steuerung enth‰lt.
 */
public class MainFrame extends BorderPane {
    private GameManager gameManager = getInstance();
    private Stage window;
    private Board board;
    private HBox controls;
    private Label status, mode, points;
    private ImageView coin;
    private Timer timer;

    /**
     * Erzeugt das Hauptfenster und die darin enthaltene Men¸leiste, Spielsteuerung
     * sowie das Spielfeld.
     *
     * @param window schlieﬂt die Appplikation und erzeugt neue Dialoge
     */
    public MainFrame(Stage window) {
        this.window = window;
        status = new Label();
        timer = new Timer();
        timer.setListener(() -> Platform.runLater(this::getTime));
        try {
            FileHandler.loadUser();
        } catch (IOException e) {
            setAlert("User data could not be loaded.");
        }
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
        Menu newGame = new Menu("New game");
        MenuItem levelMode = new MenuItem("Level mode");
        levelMode.setOnAction(e -> board.createNewGame(gameManager.getLevel()));
        MenuItem timeMode = new MenuItem("Time mode");
        timeMode.setOnAction(e -> {
            gameManager.setTimeMode();
            board.createNewGame(5);
            mode.setText("Time mode");
            coin.setImage(new Image("file:assets" + File.separator + "images" + File.separator + "timer.png"));
            points.setText(timer.getStartTime());
            controls.setVisible(false);
            if (!timer.isRunning()) timer.start();
        });
        MenuItem freeMode = new MenuItem("Free mode");
        freeMode.setOnAction(e -> createFreeGame());
        newGame.getItems().addAll(levelMode, timeMode, freeMode);
        MenuItem reset = new MenuItem("Restart");
        reset.setOnAction(e -> {
            gameManager.resetPoints();
            points.setText(String.valueOf(gameManager.getPoints()));
            board.reset();
        });
        MenuItem loadGame = new MenuItem("Load puzzle");
        loadGame.setOnAction(e -> loadGame());
        MenuItem saveGame = new MenuItem("Save puzzle");
        saveGame.setOnAction(e -> saveGame());
        MenuItem tutorial = new MenuItem("Tutorial");
        tutorial.setOnAction(e -> showTutorial());
        MenuItem exit = new MenuItem("Quit");
        exit.setOnAction(e -> close());
        menu.getItems().addAll(newGame, reset, loadGame, saveGame, tutorial, exit);
        menuBar.getMenus().add(menu);
        return menuBar;
    }

    private Node createTopBar() {
        HBox hBoxLevel = new HBox();
        hBoxLevel.setAlignment(Pos.CENTER);
        hBoxLevel.setSpacing(CONTAINER_OFFSET);
        mode = new Label("Level " + gameManager.getLevel() + "/25");
        mode.setFont(new Font(15));
        hBoxLevel.getChildren().add(mode);
        HBox hBox1 = new HBox();
        hBox1.setAlignment(Pos.CENTER);
        hBox1.setSpacing(CONTAINER_OFFSET);
        coin = new ImageView();
        Image image = new Image("file:assets" + File.separator + "images" + File.separator + "coin.png");
        coin.setImage(image);
        coin.setPreserveRatio(true);
        coin.setFitHeight(15);
        points = new Label(Integer.toString(gameManager.getPoints()));
        points.setMinWidth(50);
        points.setFont(new Font(15));
        Label spacer = new Label("                               ");
        hBox1.getChildren().addAll(spacer, coin, points);

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
        box.getChildren().addAll(hBoxLevel, region1, hBox1, region2, hBox2);
        return box;
    }

    private void showTutorial() {
        board.stopAutoSolve();
        Tutorial tutorial = new Tutorial();
        tutorial.initOwner(window);
        tutorial.show();
    }

    private Node createBoard() {
        board = new Board(this::handle, gameManager.getLevel());
        board.setPointListener(this::handlePoints);
        return board;
    }

    private Node createControls() {
        VBox vBox = new VBox();
        vBox.setPadding(new Insets(CONTAINER_OFFSET, CONTAINER_OFFSET, CONTAINER_OFFSET, CONTAINER_OFFSET));
        vBox.setSpacing(CONTAINER_OFFSET);
        CheckBox checkBox = new CheckBox("Show missing bridges");
        checkBox.setSelected(true);
        checkBox.setOnAction(e -> board.setShowMissingBridges(checkBox.isSelected()));
        controls = new HBox();
        controls.setAlignment(Pos.CENTER);
        controls.setSpacing(CONTAINER_OFFSET);
        controls.setPrefWidth(100);
        Button solveBtn = new Button("_Solve auto");
        solveBtn.setMnemonicParsing(true);
        solveBtn.setMinWidth(controls.getPrefWidth());
        solveBtn.setOnAction(e -> {
            if (board.autoSolverIsRunning()) board.stopAutoSolve();
            else board.startAutoSolve();
        });
        Button nextBtn = new Button("_Next bridge");
        nextBtn.setMnemonicParsing(true);
        nextBtn.setMinWidth(controls.getPrefWidth());
        nextBtn.setOnAction(e -> board.getNextBridge());
        controls.getChildren().addAll(solveBtn, nextBtn);
        vBox.getChildren().addAll(checkBox, controls, status);
        return vBox;
    }

    /**
     * Erzeugt Dialog-Fenster, um ein neues Spiel zu generieren.
     */
    private void createFreeGame() {
        board.stopAutoSolve();
        NewGameFrame newGameFrame = new NewGameFrame(board);
        newGameFrame.initOwner(window);
        newGameFrame.show();
    }

    private void loadGame() {
        try {
            FileHandler.loadGame();
        } catch (IOException | IllegalArgumentException e) {
            setAlert("Puzzle could not be loaded.");
            return;
        }
        board.setLoadedGrid();
    }

    private void saveGame() {
        board.saveGame();
        try {
            FileHandler.saveGame();
        } catch (IOException e) {
            setAlert("Puzzle could not be saved.");
        }
    }

    private void setAlert(String text) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Save/Load");
        alert.setHeaderText(text);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) alert.close();
    }

    private void handle(StatusEvent e) {
        Status status = e.getStatus();
        this.status.setText(status.getText());
        if (status == Status.SOLVED) {
            if (gameManager.getMode() == Mode.LEVEL) {
                solved("Level " + gameManager.getLevel() + " solved.");
                gameManager.increaseLevel();
                gameManager.savePoints();
                mode.setText("Level " + gameManager.getLevel() + "/25");
                board.createNewGame(gameManager.getLevel());
            }
            if (gameManager.getMode() == Mode.TIME) {
                timer.stop();
                solved("Puzzle solved in " + points.getText() + ".");
            } else {
                solved("Solved.");
            }
        }
    }

    private void handlePoints(PointEvent e) {
        points.setText(e.getPoints());
    }

    private void solved(String text) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Solved!");
        alert.setHeaderText(text);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.CANCEL) alert.close();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            alert.close();
        }
    }

    private void getTime() {
        points.setText(timer.getTime());
    }

    /**
     * Schlieﬂt die Applikation.
     */
    public void close() {
        try {
            FileHandler.saveUser();
        } catch (IOException e) {
            setAlert("User data could not be saved.");
            return;
        }
        window.close();
        board.shutdownAutoSolve();
        if (timer.isRunning()) timer.shutdown();
    }
}