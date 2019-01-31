package net.joedoe.views;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import net.joedoe.logics.Generator;
import net.joedoe.utils.*;
import net.joedoe.views.board.Board;
import net.joedoe.views.board.BoardFree;
import net.joedoe.views.board.BoardLevel;
import net.joedoe.views.board.BoardTime;

import java.io.IOException;
import java.time.LocalTime;
import java.util.Optional;

import static net.joedoe.views.Difficulty.VERY_EASY;
import static net.joedoe.views.Mode.LEVEL;
import static net.joedoe.views.Mode.TIME;
import static net.joedoe.views.Size.AUTO;
import static net.joedoe.views.Size.WIDTH_HEIGHT;
import static net.joedoe.views.View.*;

public class ViewController {
    private final Stage stage;
    private GameManager gameManager = GameManager.getInstance();
    private GameData gameData = GameData.getInstance();
    private Generator generator = new Generator();
    private Scene startScene, boardScene, modeScene, difficultyScene, sizeScene, howToScene;
    private Start start;
    private Board board;

    public ViewController(Stage stage) {
        this.stage = stage;
        try {
            FileHandler.loadScores();
        } catch (IOException e) {
            showAlert(AlertType.ERROR, "Loading scores", "Scores could not be loaded.");
        }
        goTo(START);
        stage.show();
    }

    private void goTo(View view) {
        switch (view) {
            case START:
                if (startScene == null) {
                    start = new Start(e -> goTo(e.getView()));
                    startScene = new Scene(start, stage.getWidth(), stage.getHeight());
                }
                start.disableResume(board == null);
                start.disableLoad(!FileHandler.filesExist());
                stage.setScene(startScene);
                return;
            case NEW:
                if (modeScene == null) {
                    ModeChooser mode = new ModeChooser(e -> goTo(START));
                    mode.setNext(e -> {
                        gameManager.setMode(e.getMode());
                        createNewGame();
                    });
                    modeScene = new Scene(mode, stage.getWidth(), stage.getHeight());
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
                createBoardLevel();
                board.setGridWithBridges();
                stage.setScene(boardScene);
                return;
            case BEST:
                Scene bestScene = new Scene(new BestScores(e -> goTo(START)), stage.getWidth(), stage.getHeight());
                stage.setScene(bestScene);
                return;
            case HOWTO:
                if (howToScene == null)
                    howToScene = new Scene(new HowTo(e -> goTo(START)), stage.getWidth(), stage.getHeight());
                stage.setScene(howToScene);
                return;
            case QUIT:
                close();
        }
    }

    private void createNewGame() {
        switch (gameManager.getMode()) {
            case LEVEL:
                if (FileHandler.filesExist()) {
                    if (!showAlert(AlertType.CONFIRMATION, "New game", "Previous level progress will be deleted."))
                        return;
                    if (!FileHandler.deleteFiles()) {
                        showAlert(AlertType.ERROR, "Deleting progress", "Level progress could not be deleted");
                        return;
                    }
                }
                gameManager.setLevel(1);
                gameManager.resetAllPoints();
                generator.setData(VERY_EASY.getIsles());
                generator.generateGame();
                createBoardLevel();
                board.setGrid();
                stage.setScene(boardScene);
                break;
            case TIME:
                if (difficultyScene == null) {
                    DifficultyChooser difficulty = new DifficultyChooser(e -> goTo(NEW));
                    difficulty.setNext(e1 -> {
                        Difficulty diff = e1.getDifficulty();
                        generator.setData(diff.getIsles());
                        generator.generateGame();
                        board = new BoardTime(e2 -> goTo(START), diff.getName());
                        board.setNext(e3 -> {
                            LocalTime time = ((BoardTime) board).getTime();
                            board.stopSound();
                            board = null;
                            TimeEntry bestTime = gameData.getBestTime(diff);
                            if (time.compareTo(bestTime.getTime()) < 0) {
                                String timeTxt = String.format("%02d:%02d", time.getMinute(), time.getSecond());
                                NewScore newScore = new NewScore("New time score (" + diff.getName() + ")", timeTxt);
                                newScore.setNext(e4 -> {
                                    gameData.setBestTime(diff, new TimeEntry(time, e4.getName()));
                                    goTo(BEST);
                                });
                                stage.setScene(new Scene(newScore, stage.getWidth(), stage.getHeight()));
                            } else goTo(BEST);
                        });
                        boardScene = new Scene(board, stage.getWidth(), stage.getHeight());
                        board.setGrid();
                        stage.setScene(boardScene);
                    });
                    difficultyScene = new Scene(difficulty, stage.getWidth(), stage.getHeight());
                }
                stage.setScene(difficultyScene);
                break;
            case FREE:
                if (sizeScene == null) {
                    SizeChooser size = new SizeChooser(e -> goTo(NEW));
                    size.setNext(e -> {
                        if (e.getSize() == AUTO) generator.setData();
                        else if (e.getSize() == WIDTH_HEIGHT) generator.setData(e.getWidth(), e.getHeight());
                        else generator.setData(e.getWidth(), e.getHeight(), e.getIsles());
                        generator.generateGame();
                        board = new BoardFree(e1 -> goTo(START));
                        board.setNext(e2 -> {
                            board = null;
                            goTo(START);
                        });
                        boardScene = new Scene(board, stage.getWidth(), stage.getHeight());
                        board.setGrid();
                        stage.setScene(boardScene);
                    });
                    sizeScene = new Scene(size, stage.getWidth(), stage.getHeight());
                }
                stage.setScene(sizeScene);
        }
    }

    private void createBoardLevel() {
        board = new BoardLevel(e -> goTo(START));
        board.setNext(e1 -> {
            if (gameManager.getLevel() == 10) {
                board = null;
                FileHandler.deleteFiles();
                int points = gameManager.getAllPoints();
                if (points > gameData.getBestLevel().getPoints()) {
                    NewScore newScore = new NewScore("New Level score:", Integer.toString(points));
                    newScore.setNext(e2 -> {
                        gameData.setBestLevel(new PointEntry(points, e2.getName()));
                        goTo(BEST);
                    });
                    stage.setScene(new Scene(newScore, stage.getWidth(), stage.getHeight()));
                } else goTo(BEST);
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
        ((BoardLevel) board).updateToolbar();
        ((BoardLevel) board).setPoints();
        boardScene = new Scene(board, stage.getWidth(), stage.getHeight());
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
        try {
            FileHandler.saveScores();
        } catch (IOException e) {
            showAlert(AlertType.ERROR, "Saving scores", "Best scores could not be saved.");
        }
        if (board != null) {
            try {
                gameManager.savePoints();
                FileHandler.saveUser();
                if (gameManager.getMode() == LEVEL) {
                    board.savePuzzle();
                    FileHandler.savePuzzle();
                }
            } catch (IOException e) {
                showAlert(AlertType.ERROR, "Saving Level", "Level progress could not be saved.");
            }
            board.close();
        }
        stage.close();
    }
}
