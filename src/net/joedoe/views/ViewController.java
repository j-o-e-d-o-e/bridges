package net.joedoe.views;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import net.joedoe.logics.Generator;
import net.joedoe.utils.FileHandler;
import net.joedoe.utils.GameManager;
import net.joedoe.views.board.Board;
import net.joedoe.views.board.BoardFree;
import net.joedoe.views.board.BoardLevel;
import net.joedoe.views.board.BoardTime;

import java.io.IOException;
import java.util.Optional;

import static net.joedoe.utils.GameManager.Mode.LEVEL;
import static net.joedoe.utils.GameManager.Mode.TIME;
import static net.joedoe.views.SizeChooser.Type.AUTO;
import static net.joedoe.views.SizeChooser.Type.WIDTH_HEIGHT;
import static net.joedoe.views.ViewController.View.*;

public class ViewController {
    private final Stage stage;
    private final int width = 768, height = 1024;
    private Scene startScene, boardScene, modeScene, difficultyScene, sizeScene, highScoreScene, rulesScene;
    private Start start;
    private Board board;
    private GameManager gameManager = GameManager.getInstance();
    private Generator generator = new Generator();

    public enum View {
        START, NEW, RESUME, LOAD, BEST, RULES, QUIT
    }

    public ViewController(Stage stage) {
        this.stage = stage;
        goTo(START);
//        goTo(NEW);
        stage.show();
    }

    private void goTo(View view) {
        switch (view) {
            case START:
                if (startScene == null) {
                    start = new Start(e -> goTo(e.getView()));
                    startScene = new Scene(start, width, height);
                }
                if (board == null) start.disableResume(true);
                else start.disableResume(false);
                if (!FileHandler.filesExist()) start.disableLoad(true);
                else start.disableLoad(false);
                stage.setScene(startScene);
                return;
            case NEW:
                if (modeScene == null) {
                    ModeChooser mode = new ModeChooser(e -> goTo(START));
                    mode.setListener(e -> {
                        gameManager.setMode(e.getMode());
                        createNewGame();
                    });
                    modeScene = new Scene(mode, width, height);
                }
                stage.setScene(modeScene);
                return;
            case RESUME:
                if (gameManager.getMode() == TIME) ((BoardTime) board).restartTimer();
                board.restartSound();
                stage.setScene(boardScene);
                return;
            case LOAD:
                try {
                    FileHandler.loadUser();
                    FileHandler.loadPuzzle();
                } catch (IOException e) {
                    showAlert(AlertType.ERROR, "Loading level", "Level progress could not be loaded.");
                }
                gameManager.setMode(LEVEL);
                board = new BoardLevel(e -> goTo(START));
                createBoardLevel();
                board.setGridWithBridges();
                stage.setScene(boardScene);
                return;
            case BEST:
                if (highScoreScene == null) highScoreScene = new Scene(new BestScores(e -> goTo(START)), width, height);
                stage.setScene(highScoreScene);
                return;
            case RULES:
                if (rulesScene == null) rulesScene = new Scene(new Rules(e -> goTo(START)), width, height);
                stage.setScene(rulesScene);
                return;
            case QUIT:
                close();
        }
    }

    private void createBoardLevel() {
        board = new BoardLevel(e -> goTo(START));
        board.setListenerNext(e -> {
            if (gameManager.getLevel() == 25) {
                //noinspection StatementWithEmptyBody, ConstantConditions
                if (true) { // TODO: if gameManager.getAllPoints > currentBestScoreForLevel
                    //TODO: edit BestScore
                }
                goTo(BEST);
            } else {
                gameManager.savePoints();
                gameManager.increaseLevel();
                generator.setData(gameManager.getLevel() * 5);
                generator.generateGame();
                ((BoardLevel) board).updateToolbar();
                board.setGrid();
                try {
                    board.savePuzzle();
                    FileHandler.savePuzzle();
                    FileHandler.saveUser();
                } catch (IOException ex) {
                    showAlert(AlertType.ERROR, "Saving Level", "Level progress could not be saved.");
                }
            }
        });
        boardScene = new Scene(board, width, height);
    }

    private void createNewGame() {
        switch (gameManager.getMode()) {
            case LEVEL:
                if (FileHandler.filesExist())
                    if (!showAlert(AlertType.CONFIRMATION, "New game", "Previous level progress will be deleted."))
                        return;
                if(!FileHandler.deleteFiles()){
                    showAlert(AlertType.ERROR, "Deleting progress", "Level progress could not be deleted");
                    return;
                }
                gameManager.setLevel(1);
                gameManager.resetAllPoints();
                generator.setData(5);
                generator.generateGame();
                createBoardLevel();
                board.setGrid();
                stage.setScene(boardScene);
                break;
            case TIME:
                if (difficultyScene == null) {
                    DifficultyChooser difficulty = new DifficultyChooser(e -> goTo(NEW));
                    difficulty.setListener(e -> {
                        generator.setData(e.getDifficulty().getLevel() * 5);
                        generator.generateGame();
                        board = new BoardTime(e1 -> goTo(START));
                        board.setListenerNext(e2 -> {
                            //noinspection ConstantConditions
                            if (true) { //TODO: if game.getTempPoints() > currentBestScoreForTime
                                board.stopSound();
                                board = null;
                                goTo(BEST);
                            }
                        });
                        boardScene = new Scene(board, width, height);
                        board.setGrid();
                        stage.setScene(boardScene);
                    });
                    difficultyScene = new Scene(difficulty, width, height);
                }
                stage.setScene(difficultyScene);
                break;
            case FREE:
                if (sizeScene == null) {
                    SizeChooser size = new SizeChooser(e -> goTo(NEW));
                    size.setListener(e -> {
                        if (e.getType() == AUTO) generator.setData();
                        else if (e.getType() == WIDTH_HEIGHT) generator.setData(e.getWidth(), e.getHeight());
                        else generator.setData(e.getWidth(), e.getHeight(), e.getIsles());
                        generator.generateGame();
                        board = new BoardFree(e1 -> goTo(START));
                        board.setListenerNext(e2 -> {
                            //noinspection ConstantConditions
                            if (1 > 0) { //TODO: if game.getTempPoints() > currentBestScoreForTime
                                board.stopSound();
                                board = null;
                                goTo(BEST);
                            }
                        });
                        boardScene = new Scene(board, width, height);
                        board.setGrid();
                        stage.setScene(boardScene);
                    });
                    sizeScene = new Scene(size, width, height);
                }
                stage.setScene(sizeScene);
        }
    }

    private boolean showAlert(AlertType alertType, String title, String text) {
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
                if (gameManager.getMode() == LEVEL) {
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
