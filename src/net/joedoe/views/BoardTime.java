package net.joedoe.views;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import net.joedoe.utils.Timer;

import java.io.File;

import static net.joedoe.views.SceneController.Screen.HIGHSCORE;
import static net.joedoe.views.SceneController.Screen.START;

class BoardTime extends Board {
    private Timer timer;

    BoardTime(SceneController controller) {
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
    Image getInfoImage() {
        return new Image("file:assets" + File.separator + "images" + File.separator + "clock.png");
    }

    @Override
    String getInfoText() {
        return timer.getTime();
    }

    @Override
    void setGrid() {
        super.setGrid();
        timer.start();
    }

    @Override
    void handlePoints(PointEvent e){
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

    void restartTimer() {
        timer.start();
    }

    @Override
    void close() {
        super.close();
        if (timer != null) timer.shutdown();
    }
}
