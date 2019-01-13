package net.joedoe.views;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;

public class CustomMenuBar extends MenuBar {
    private MainFrame main;

    CustomMenuBar(MainFrame main) {
        this.main = main;
        createMenuBar();
    }

    private void createMenuBar() {
        Menu menu = new Menu("\u2630");
        Menu newGame = new Menu("New game");
        MenuItem levelMode = new MenuItem("Level mode");
        levelMode.setOnAction(e -> main.createNewGame());
        MenuItem timeMode = new MenuItem("Time mode");
        timeMode.setOnAction(e -> main.chooseDifficulty());
        MenuItem freeMode = new MenuItem("Free mode");
        freeMode.setOnAction(e -> main.createFreeGame());
        newGame.getItems().addAll(levelMode, timeMode, freeMode);
        MenuItem reset = new MenuItem("Restart");
        reset.setOnAction(e -> main.reset());
        MenuItem loadGame = new MenuItem("Load puzzle");
        loadGame.setOnAction(e -> main.loadGame());
        MenuItem saveGame = new MenuItem("Save puzzle");
        saveGame.setOnAction(e -> main.saveGame());
        MenuItem highScore = new MenuItem("HighScore");
        highScore.setOnAction(e -> main.showHighScore());
        MenuItem tutorial = new MenuItem("Tutorial");
        tutorial.setOnAction(e -> main.showTutorial());
        MenuItem exit = new MenuItem("Quit");
        exit.setOnAction(e -> main.close());
        menu.getItems().addAll(newGame, reset, loadGame, saveGame, highScore, tutorial, exit);
        getMenus().add(menu);
    }
}
