package net.joedoe.views;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import net.joedoe.logics.Generator;
import net.joedoe.utils.FileHandler;
import net.joedoe.utils.GameManager;
import net.joedoe.utils.GameManager.Mode;

import java.io.IOException;
import java.util.Optional;

public class SceneController {
    private final Stage stage;
    private Board board;
    private GameManager gameManager = GameManager.getInstance();
    private final int width = 768, height = 1024;
    private Scene boardScene, timeScene, freeScene, highScoreScene, rulesScene, modeScene, startScene;
    private Generator generator = new Generator();

    public SceneController(Stage stage) {
        this.stage = stage;
        Start start = new Start(this);
        startScene = new Scene(start, width, height);
        board = new Board(this);
        boardScene = new Scene(board, width, height);
        stage.setScene(startScene);
        stage.show();
    }

    void goTo(String btn) {
        switch (btn) {
            case "Start":
                stage.setScene(startScene);
                return;
            case "New Game":
                if (modeScene == null) {
                    ModeChooser modeChooser = new ModeChooser(this);
                    modeScene = new Scene(modeChooser, width, height);
                }
                stage.setScene(modeScene);
                return;
            case "Resume":
                switchToBoard();
                return;
            case "Load Game":
                try {
                    FileHandler.loadUser();
                    FileHandler.loadPuzzle();
                } catch (IOException e) {
                    showAlert(AlertType.ERROR, "Loading Data", "Data could not be loaded.");
                }
                board.setGridWithBridges();
                stage.setScene(boardScene);
                return;
            case "Highscore":
                if (highScoreScene == null) {
                    HighScore highScore = new HighScore(this);
                    highScoreScene = new Scene(highScore, width, height);
                    highScoreScene.getStylesheets().add("file:assets/css/highscores.css");
                }
                stage.setScene(highScoreScene);
                return;
            case "Rules":
                if (rulesScene == null) {
                    Rules rules = new Rules(this);
                    rulesScene = new Scene(rules, width, height);
                }
                stage.setScene(rulesScene);
                return;
            case "Quit":
                close();
        }
    }

    void createNewGame(Mode mode) {
        gameManager.setMode(mode);
        switch (mode) {
            case LEVEL:
                if (showAlert(AlertType.CONFIRMATION, "New game", "Previous progress will be deleted. Continue?")) {
                    gameManager.setLevel(1);
                    gameManager.resetPoints();
                    gameManager.resetTempPoints();
                    generator.setData(5);
                    generator.generateGame();
                    if (boardScene == null) {
                        board = new Board(this);
                        boardScene = new Scene(board, width, height);
                    }
                    board.setPuzzle(gameManager.getMode());
                    stage.setScene(boardScene);
                }
                break;
            case TIME:
                if (timeScene == null) {
                    DifficultyChooser difficultyChooser = new DifficultyChooser(this);
                    timeScene = new Scene(difficultyChooser, width, height);
                }
                stage.setScene(timeScene);
                break;
            case FREE:
                if (freeScene == null) {
                    SizeChooser sizeChooser = new SizeChooser(this);
                    freeScene = new Scene(sizeChooser, width, height);
                }
                stage.setScene(freeScene);
        }
    }

    void switchToBoard() {
        stage.setScene(boardScene);
    }

    void setPuzzle() {
        board.setPuzzle(gameManager.getMode());
    }

    void loadPuzzle() {
        try {
            FileHandler.loadPuzzle();
        } catch (IOException e) {
            showAlert(AlertType.ERROR, "Loading Puzzle", "Puzzle could not be loaded.");
        }
        board.setGridWithBridges();
    }

    void savePuzzle() {
        try {
            board.savePuzzle();
            FileHandler.savePuzzle();
        } catch (IOException e) {
            showAlert(AlertType.ERROR, "Saving Puzzle", "Puzzle could not be saved.");
        }
    }

    boolean showAlert(AlertType alertType, String title, String text) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(text);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            alert.close();
            return true;
        }
        return false;
    }

    public void close() {
        if (board != null) {
            try {
                gameManager.savePoints();
                FileHandler.saveUser();
                if (gameManager.getMode() == Mode.LEVEL) {
                    board.savePuzzle();
                    FileHandler.savePuzzle();
                }
            } catch (IOException e) {
                showAlert(AlertType.ERROR, "Saving Data", "Data could not be saved.");
            }
            board.close();
        }
        stage.close();
    }
}
