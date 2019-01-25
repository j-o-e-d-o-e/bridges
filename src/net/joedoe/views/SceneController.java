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

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import static net.joedoe.views.SceneController.Screen.START;

public class SceneController {
    private final Stage stage;
    private GameManager gameManager = GameManager.getInstance();
    private final int width = 768, height = 1024;
    private Board board;
    private Start start;
    private Scene startScene, boardScene, modeScene, difficultyChooserScene, sizeChooserScene, highScoreScene, rulesScene;
    private Generator generator = new Generator();

    public SceneController(Stage stage) {
        this.stage = stage;
        start = new Start(this);
        startScene = new Scene(start, width, height);
        goTo(START);
        stage.show();
    }

    public enum Screen {
        START, NEW, RESUME, LOAD, HIGHSCORE, RULES, QUIT
    }

    void goTo(Screen screen) {
        switch (screen) {
            case START:
                if (board == null) start.disableResume(true);
                else start.disableResume(false);
                if(FileHandler.fileExists()) start.disableLoad(true);
                else start.disableLoad(false);
                stage.setScene(startScene);
                return;
            case NEW:
                if (modeScene == null) modeScene = new Scene(new ModeChooser(this), width, height);
                stage.setScene(modeScene);
                return;
            case RESUME:
                if (board == null) return;
                else if (gameManager.getMode() == Mode.TIME) ((BoardTime) board).restartTimer();
                board.restartSound();
                stage.setScene(boardScene);
                return;
            case LOAD:
                try {
                    FileHandler.loadUser();
                    FileHandler.loadPuzzle();
                } catch (IOException e) {
                    showAlert(AlertType.ERROR, "Loading Data", "Data could not be loaded.");
                }
                board.setGridWithBridges();
                stage.setScene(boardScene);
                return;
            case HIGHSCORE:
                if (highScoreScene == null) highScoreScene = new Scene(new HighScore(this), width, height);
                stage.setScene(highScoreScene);
                return;
            case RULES:
                if (rulesScene == null) {
                    rulesScene = new Scene(new Rules(this), width, height);
                }
                stage.setScene(rulesScene);
                return;
            case QUIT:
                close();
        }
    }

    void createNewGame(Mode mode) {
        gameManager.setMode(mode);
        switch (mode) {
            case LEVEL:
                createBoard();
                break;
            case TIME:
                if (difficultyChooserScene == null)
                    difficultyChooserScene = new Scene(new DifficultyChooser(this), width, height);
                stage.setScene(difficultyChooserScene);
                break;
            case FREE:
                if (sizeChooserScene == null) sizeChooserScene = new Scene(new SizeChooser(this), width, height);
                stage.setScene(sizeChooserScene);
        }
    }

    void createBoard() {
        switch (gameManager.getMode()) {
            case LEVEL:
                if (!showAlert(AlertType.CONFIRMATION, "New game", "Previous progress will be deleted. Continue?"))
                    return;
                gameManager.setLevel(1);
                gameManager.resetAllPoints();
                gameManager.resetTempPoints();
                generator.setData(5);
                generator.generateGame();
                board = new BoardLevel(this);
                boardScene = new Scene(board, width, height);
                board.setGrid();
                stage.setScene(boardScene);
                break;
            case TIME:
                board = new BoardTime(this);
                board.setGrid();
                boardScene = new Scene(board, width, height);
                break;
            case FREE:
                board = new BoardFree(this);
                board.setGrid();
                boardScene = new Scene(board, width, height);
        }
        stage.setScene(boardScene);
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
