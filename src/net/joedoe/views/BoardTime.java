package net.joedoe.views;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import net.joedoe.utils.Timer;

import java.io.File;

public class BoardTime extends Board {
    private Timer timer;

    public BoardTime(SceneController controller) {
        super(controller);
        setLayout();
        controls.setVisible(false);
    }

    @Override
    ToolBar createToolBar() {
        return new ToolBar(controller, "Start", "Time mode");
    }

    @Override
    Image getInfoImage() {
        return new Image("file:assets" + File.separator + "images" + File.separator + "clock.png");
    }

    @Override
    String getInfoText() {
        return timer.getStartTime();
    }

    @Override
    void setGrid() {
        super.setGrid();
        if (timer != null) timer.stop();
        if (timer == null) {
            timer = new Timer();
            timer.setListener(() -> Platform.runLater(() -> info.setText(timer.getTime())));
            timer.start();
        } else {
            timer.restart();
        }
        info.setText(timer.getStartTime());
    }

    @Override
    void handleStatus(StatusEvent e) {
        StatusEvent.Status status = e.getStatus();
        this.status.setText(status.getText());
        if (status == StatusEvent.Status.SOLVED) {
            timer.stop();
            controller.showAlert(Alert.AlertType.INFORMATION, "Solved!", "Puzzle solved in " + info.getText() + ".");
        }
    }

    @Override
    void close() {
        super.close();
        if (timer != null) timer.shutdown();
    }
}
