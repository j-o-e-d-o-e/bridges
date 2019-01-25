package net.joedoe.views.board;

import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import net.joedoe.utils.GameManager;
import net.joedoe.views.ViewController;
import net.joedoe.views.ToolBar;
import net.joedoe.views.board.StatusEvent.Status;

import java.io.File;

import static net.joedoe.views.ViewController.View.HIGHSCORE;
import static net.joedoe.views.ViewController.View.START;

public class BoardFree extends Board {
    private GameManager gameManager = GameManager.getInstance();
    private Image coin = new Image("file:assets" + File.separator + "images" + File.separator + "coin.png");

    public BoardFree(ViewController controller) {
        super(controller);
        setLayout();
    }

    @Override
    ToolBar createToolBar() {
        ToolBar toolBar = new ToolBar("Free mode");
        toolBar.setListener(e -> {
            player.pause();
            controller.goTo(START);
        });
        return toolBar;
    }

    @Override
    Image getInfoImage() {
        return coin;
    }

    @Override
    String getInfoText() {
        return "0";
    }

    @Override
    public void setGrid() {
        super.setGrid();
        gameManager.resetTempPoints();
    }

    @Override
    void handlePoints(PointEvent e) {
        gameManager.addPoints(e.getPoints());
        info.setText(Integer.toString(gameManager.getTempPoints()));
    }

    @Override
    void handleStatus(StatusEvent e) {
        Status status = e.getStatus();
        this.status.setText(status.getText());
        if (status == Status.SOLVED) {
            controller.showAlert(Alert.AlertType.INFORMATION, "Solved!", "Solved.");
            player.stop();
            controller.goTo(HIGHSCORE);
        }
    }
}
