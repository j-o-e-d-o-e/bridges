package net.joedoe.views.board;

import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import net.joedoe.logics.Generator;
import net.joedoe.utils.GameManager;
import net.joedoe.views.ToolBar;
import net.joedoe.views.ViewController;

import java.io.File;

import static net.joedoe.views.ViewController.View.START;

public class BoardLevel extends Board {
    private Generator generator;

    public BoardLevel(ViewController controller) {
        super(controller);
        generator = new Generator();
        setLayout();
    }

    @Override
    ToolBar createToolBar() {
        ToolBar toolbar = new ToolBar("Level " + gameManager.getLevel() + "/25");
        toolbar.setListener(e -> {
            player.pause();
            controller.goTo(START);
        });
        return toolbar;
    }

    @Override
    void reset() {
        gameManager.resetTempPoints();
        info.setText(Integer.toString(gameManager.getAllPoints()));
        grid.reset();
    }

    @Override
    public void setGrid() {
        super.setGrid();
        toolBar.updateTitle("Level " + gameManager.getLevel() + "/25");
        info.setText(Integer.toString(gameManager.getAllPoints()));
    }

    @Override
    void handleStatus(StatusEvent e) {
        StatusEvent.Status status = e.getStatus();
        this.status.setText(status.getText());
        if (status == StatusEvent.Status.SOLVED) {
            controller.showAlert(Alert.AlertType.INFORMATION, "Solved!", "Level " + gameManager.getLevel() + " solved.");
            gameManager.savePoints();
            info.setText(Integer.toString(gameManager.getAllPoints()));
            gameManager.increaseLevel();
            generator.setData(gameManager.getLevel() * 5);
            generator.generateGame();
            setGrid();
        }
    }
}
