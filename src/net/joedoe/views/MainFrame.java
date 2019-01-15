package net.joedoe.views;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import net.joedoe.utils.FileHandler;
import net.joedoe.utils.GameManager;
import net.joedoe.utils.Timer;
import net.joedoe.views.DifficultyFrame.Difficulty;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import static net.joedoe.utils.GameInfo.CONTAINER_OFFSET;
import static net.joedoe.utils.GameManager.Mode;
import static net.joedoe.utils.GameManager.getInstance;
import static net.joedoe.views.StatusEvent.Status;

/**
 * Das Hauptfenster, das das Spielfeld und die Steuerung enth�lt.
 */
public class MainFrame extends BorderPane {
    private GameManager gameManager = getInstance();
    private Stage window;
    private CustomMenuBar menuBar;
    private Board board;
    private HBox controls;
    private Label status, mode, infoLabel;
    private ImageView infoImg;
    private Timer timer;

    /**
     * Erzeugt das Hauptfenster und die darin enthaltene Men�leiste, Spielsteuerung
     * sowie das Spielfeld.
     *
     * @param window schlie�t die Appplikation und erzeugt neue Dialoge
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
        menuBar = new CustomMenuBar(this);
        box.getChildren().addAll(menuBar, createTopBar());
        return box;
    }

    void createNewGame() {
        board.createNewGame(gameManager.getLevel());
    }

    void reset() {
        gameManager.resetPoints();
        infoLabel.setText(String.valueOf(gameManager.getPoints()));
        board.reset();
    }

    void showHighScore() {
        HighScoreFrame highScoreFrame = new HighScoreFrame(menuBar);
        Scene main = window.getScene();
        highScoreFrame.setListener(e -> window.setScene(main));
        Scene highScore = new Scene(highScoreFrame, 768, 1024);
        window.setScene(highScore);
    }

    void chooseDifficulty() {
        DifficultyFrame difficultyFrame = new DifficultyFrame();
        difficultyFrame.setListener(e -> {
            Difficulty difficulty = e.getDifficulty();
            createTimeGame(difficulty.getLevel());
        });
        difficultyFrame.initOwner(window);
        difficultyFrame.show();
    }

    private void createTimeGame(int level) {
        board.stopAutoSolve();
        gameManager.setTimeMode();
        mode.setText("Time mode");
        infoImg.setImage(new Image("file:assets" + File.separator + "images" + File.separator + "timer.png"));
        controls.setVisible(false);
        board.createNewGame(level);
        infoLabel.setText(timer.getStartTime());
        if (!timer.isRunning()) timer.start();
    }

    private Node createTopBar() {
        HBox modeBox = new HBox(CONTAINER_OFFSET);
        modeBox.setAlignment(Pos.CENTER_LEFT);
        modeBox.setMinWidth(200);
        mode = new Label("Level " + gameManager.getLevel() + "/25");
        mode.setFont(new Font(14));
        modeBox.getChildren().add(mode);

        HBox infoBox = new HBox(CONTAINER_OFFSET);
        infoBox.setAlignment(Pos.CENTER);
        infoBox.setMinWidth(200);
        infoImg = new ImageView();
        Image image = new Image("file:assets" + File.separator + "images" + File.separator + "coin.png");
        infoImg.setImage(image);
        infoImg.setPreserveRatio(true);
        infoImg.setFitHeight(15);
        infoLabel = new Label(Integer.toString(gameManager.getPoints()));
        infoLabel.setFont(new Font(14));
        infoBox.getChildren().addAll(infoImg, infoLabel);

        HBox controlsBox = new HBox(CONTAINER_OFFSET);
        controlsBox.setAlignment(Pos.CENTER_RIGHT);
        controlsBox.setMinWidth(200);
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
        controlsBox.getChildren().addAll(zoomIn, zoomOut, sound);

        Region regionLeft = new Region();
        HBox.setHgrow(regionLeft, Priority.ALWAYS);
        Region regionRight = new Region();
        HBox.setHgrow(regionRight, Priority.ALWAYS);

        HBox box = new HBox();
        box.setPadding(new Insets(CONTAINER_OFFSET, CONTAINER_OFFSET, 0, CONTAINER_OFFSET));
        box.getChildren().addAll(modeBox, regionLeft, infoBox, regionRight, controlsBox);
        return box;
    }

    void showTutorial() {
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
        VBox vBox = new VBox(CONTAINER_OFFSET);
        vBox.setPadding(new Insets(CONTAINER_OFFSET, CONTAINER_OFFSET, CONTAINER_OFFSET, CONTAINER_OFFSET));
        CheckBox checkBox = new CheckBox("Show missing bridges");
        checkBox.setSelected(true);
        checkBox.setOnAction(e -> board.setShowMissingBridges(checkBox.isSelected()));
        controls = new HBox(CONTAINER_OFFSET);
        controls.setAlignment(Pos.CENTER);
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
    void createFreeGame() {
        board.stopAutoSolve();
        NewGameFrame newGameFrame = new NewGameFrame(board);
        newGameFrame.initOwner(window);
        newGameFrame.show();
    }

    void loadGame() {
        try {
            FileHandler.loadGame();
        } catch (IOException | IllegalArgumentException e) {
            setAlert("Puzzle could not be loaded.");
            return;
        }
        board.setLoadedGrid();
    }

    void saveGame() {
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
                solved("Puzzle solved in " + infoLabel.getText() + ".");
            } else {
                solved("Solved.");
            }
        }
    }

    private void handlePoints(PointEvent e) {
        infoLabel.setText(e.getPoints());
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
        infoLabel.setText(timer.getTime());
    }

    /**
     * Schlie�t die Applikation.
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