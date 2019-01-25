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
import net.joedoe.views.board.Board;
import net.joedoe.views.board.BoardFree;
import net.joedoe.views.board.BoardLevel;
import net.joedoe.views.board.BoardTime;

import java.io.IOException;
import java.util.Optional;

import static net.joedoe.views.ViewController.View.START;

public class ViewController {
    private final Stage stage;
    private final int width = 768, height = 1024;
    private Scene startScene, boardScene, modeScene, difficultyScene, sizeScene, highScoreScene, rulesScene;
    private Start start;
    private Board board;
    private GameManager gameManager = GameManager.getInstance();
    private Generator generator = new Generator();

    public ViewController(Stage stage) {
        this.stage = stage;
        start = new Start(this);
        startScene = new Scene(start, width, height);
        goTo(START);
        stage.show();
    }

    public enum View {
        START, NEW, RESUME, LOAD, HIGHSCORE, RULES, QUIT
    }

    public void goTo(View view) {
        switch (view) {
            case START:
                if (board == null) start.disableResume(true);
                else start.disableResume(false);
                if (FileHandler.fileExists()) start.disableLoad(true);
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
                if (highScoreScene == null) highScoreScene = new Scene(new Highscore(this), width, height);
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
                if (difficultyScene == null) {
                    DifficultyChooser difficulty = new DifficultyChooser(this);
                    difficulty.setListener(e -> {
                        board = new BoardTime(this);
                        board.setGrid();
                        boardScene = new Scene(board, width, height);
                        stage.setScene(boardScene);
                    });
                    difficultyScene = new Scene(difficulty, width, height);
                }
                stage.setScene(difficultyScene);
                break;
            case FREE:
                if (sizeScene == null) {
                    SizeChooser size = new SizeChooser(this);
                    size.setListener(e -> {
                        board = new BoardFree(this);
                        board.setGrid();
                        boardScene = new Scene(board, width, height);
                        stage.setScene(boardScene);
                    });
                    sizeScene = new Scene(size, width, height);
                }
                stage.setScene(sizeScene);
        }
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

    public boolean showAlert(AlertType alertType, String title, String text) {
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
