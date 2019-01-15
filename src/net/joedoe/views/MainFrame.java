package net.joedoe.views;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import net.joedoe.utils.FileHandler;
import net.joedoe.views.DifficultyStage.Difficulty;

import java.io.IOException;
import java.util.Optional;

/**
 * Das Hauptfenster, das das Spielfeld und die Steuerung enthält.
 */
public class MainFrame extends BorderPane {
    private Stage window, difficulty, newGame;
    private HighScorePane highScore;
    private MainPane main;
    private Tutorial tutorial;

    /**
     * Erzeugt das Hauptfenster und die darin enthaltene Menüleiste, Spielsteuerung
     * sowie das Spielfeld.
     *
     * @param window schließt die Appplikation und erzeugt neue Dialoge
     */
    public MainFrame(Stage window) {
        this.window = window;
        setTop(new CustomMenuBar(this));
        try {
            FileHandler.loadUser();
        } catch (IOException e) {
            setAlert("User data could not be loaded.");
        }
        main = new MainPane();
        setCenter(main);
    }

    void createLevelGame() {
        main.createLevelGame();
    }

    void chooseDifficulty() {
        if (difficulty == null) {
            difficulty = new DifficultyStage();
            difficulty.initOwner(window);
            ((DifficultyStage) difficulty).setListener(e -> {
                Difficulty difficulty = e.getDifficulty();
                main.createTimeGame(difficulty.getLevel());
            });
        }
        difficulty.show();
    }

    void createFreeGame() {
        main.stopAutoSolve();
        if (newGame == null) {
            newGame = new NewGameStage();
            newGame.initOwner(window);
            ((NewGameStage) newGame).setListener(e -> main.createFreeGame());
        }
        newGame.show();
    }

    void reset() {
        main.reset();
    }

    void loadGame() {
        try {
            FileHandler.loadGame();
        } catch (IOException | IllegalArgumentException e) {
            setAlert("Puzzle could not be loaded.");
            return;
        }
        main.setLoadedGrid();
    }

    void saveGame() {
        main.saveGame();
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


    void showHighScore() {
        if (highScore == null) {
            highScore = new HighScorePane();
            highScore.setListener(e -> setCenter(main));
        }
        setCenter(highScore);
    }

    void showTutorial() {
        if (tutorial == null) tutorial = new Tutorial();
        tutorial.initOwner(window);
        tutorial.show();
    }

    public void close() {
        try {
            FileHandler.saveUser();
        } catch (IOException e) {
            setAlert("User data could not be saved.");
            return;
        }
        main.close();
        window.close();
    }
}