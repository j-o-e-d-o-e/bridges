package net.joedoe.views.board;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import net.joedoe.utils.Timer;
import net.joedoe.views.ToolBar;
import net.joedoe.views.ViewController;

import java.io.File;

import static net.joedoe.views.ViewController.View.HIGHSCORE;
import static net.joedoe.views.ViewController.View.START;

public class BoardTime extends Board {
    private Timer timer;

    public BoardTime(ViewController controller) {
        super(controller);
        timer = new Timer();
        timer.setListener(() -> Platform.runLater(() -> info.setText(timer.getTime())));
        setLayout();
        controls.setVisible(false);
    }

    @Override
    ToolBar createToolBar() {
        ToolBar toolBar = new ToolBar("Time mode");
        toolBar.setListener(e -> {
            timer.stop();
            player.pause();
            controller.goTo(START);
        });
        return toolBar;
    }

    @Override
    void reset() {
        timer.restart();
        info.setText(getInfoText());
        grid.reset();
    }

    @Override
    Image getInfoImage() {
        return new Image("file:assets" + File.separator + "images" + File.separator + "clock.png");
    }

    @Override
    String getInfoText() {
        return timer.getTime();
    }

    @Override
    public void setGrid() {
        super.setGrid();
        timer.start();
    }

    @Override
    void handlePoints(PointEvent e) {
    }

    @Override
    void handleStatus(StatusEvent e) {
        StatusEvent.Status status = e.getStatus();
        this.status.setText(status.getText());
        if (status == StatusEvent.Status.SOLVED) {
            timer.stop();
            controller.showAlert(Alert.AlertType.INFORMATION, "Solved!", "Puzzle solved in " + info.getText() + ".");
            player.stop();
            controller.goTo(HIGHSCORE);
        }
    }

    public void restartTimer() {
        timer.start();
    }

    @Override
    public void close() {
        super.close();
        if (timer != null) timer.shutdown();
    }
}
