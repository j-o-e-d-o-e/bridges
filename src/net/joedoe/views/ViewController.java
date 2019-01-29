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

import static net.joedoe.utils.GameManager.Mode.LEVEL;
import static net.joedoe.utils.GameManager.Mode.TIME;
import static net.joedoe.views.SizeChooser.Type.AUTO;
import static net.joedoe.views.SizeChooser.Type.WIDTH_HEIGHT;
import static net.joedoe.views.ViewController.View.*;

public class ViewController {
    private final Stage stage;
    private GameManager gameManager = GameManager.getInstance();
    private GameData gameData = GameData.getInstance();
    private Generator generator = new Generator();
    private Scene startScene, boardScene, modeScene, difficultyScene, sizeScene, bestScene, howToScene;
    private Start start;
    private Board board;
    private BestScores best;

    public enum View {
        START, NEW, RESUME, LOAD, BEST, HOWTO, QUIT
    }

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
                    mode.setListener(e -> {
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
                createBestScores();
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

    private void createBoardLevel() {
        board = new BoardLevel(e -> goTo(START));
        board.setListenerNext(e -> {
            if (gameManager.getLevel() == 10) {
                int points = gameManager.getAllPoints();
                if (points > gameData.getBestLevel().getPoints()) {
                    gameData.setBestLevel(new PointEntry(points, ""));
                    createBestScores();
                    best.setLevelPoints(points);
                    best.setEditableLevel();
                }
                board = null;
                FileHandler.deleteFiles();
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
        ((BoardLevel) board).updateToolbar();
        boardScene = new Scene(board, stage.getWidth(), stage.getHeight());
    }


    private void createBestScores() {
        if (bestScene == null) {
            best = new BestScores(e -> goTo(START));
            best.setListener(e2 -> {
                try {
                    FileHandler.saveScores();
                    System.out.println("save");
                } catch (IOException e1) {
                    showAlert(AlertType.ERROR, "Saving scores", "Scores could not be saved.");
                }
            });
            bestScene = new Scene(best, stage.getWidth(), stage.getHeight());
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
                        board = new BoardTime(e1 -> goTo(START), e.getDifficulty().getName());
                        board.setListenerNext(e2 -> {
                            createBestScores();
                            LocalTime time = ((BoardTime) board).getTime();
                            switch (e.getDifficulty()) {
                                case VERY_EASY:
                                    if (time.compareTo(gameData.getBestVeryEasy().getTime()) < 0) {
                                        gameData.setBestVeryEasy(new TimeEntry(time, ""));
                                        best.setVeryEasyTime(time);
                                        best.setEditableVeryEasy();
                                    }
                                    break;
                                case EASY:
                                    if (time.compareTo(gameData.getBestEasy().getTime()) < 0) {
                                        gameData.setBestEasy(new TimeEntry(time, ""));
                                        best.setEasyTime(time);
                                        best.setEditableEasy();
                                    }
                                    break;
                                case MEDIUM:
                                    if (time.compareTo(gameData.getBestMedium().getTime()) < 0) {
                                        gameData.setBestMedium(new TimeEntry(time, ""));
                                        best.setMediumTime(time);
                                        best.setEditableMedium();
                                    }
                                    break;
                                case HARD:
                                    if (time.compareTo(gameData.getBestHard().getTime()) < 0) {
                                        gameData.setBestHard(new TimeEntry(time, ""));
                                        best.setHardTime(time);
                                        best.setEditableHard();
                                    }
                                    break;
                                case CHALLENGING:
                                    if (time.compareTo(gameData.getBestChallenging().getTime()) < 0) {
                                        gameData.setBestChallenging(new TimeEntry(time, ""));
                                        best.setChallengingTime(time);
                                        best.setEditableChallenging();
                                    }
                                    break;
                            }
                            board.stopSound();
                            board = null;
                            goTo(BEST);
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
                    size.setListener(e -> {
                        if (e.getType() == AUTO) generator.setData();
                        else if (e.getType() == WIDTH_HEIGHT) generator.setData(e.getWidth(), e.getHeight());
                        else generator.setData(e.getWidth(), e.getHeight(), e.getIsles());
                        generator.generateGame();
                        board = new BoardFree(e1 -> goTo(START));
                        board.setListenerNext(e2 -> {
                            board.stopSound();
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
                showAlert(AlertType.ERROR, "Saving Level", "Level progress could not be saved.");
            }
            board.close();
        }
        stage.close();
    }
}
