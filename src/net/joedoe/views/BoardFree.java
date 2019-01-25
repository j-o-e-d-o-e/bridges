package net.joedoe.views;

import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import net.joedoe.utils.GameManager;
import net.joedoe.views.StatusEvent.Status;

import java.io.File;

class BoardFree extends Board {
    private GameManager gameManager = GameManager.getInstance();
    private Image coin = new Image("file:assets" + File.separator + "images" + File.separator + "coin.png");

    BoardFree(SceneController controller) {
        super(controller);
        setLayout();
    }

    @Override
    ToolBar createToolBar() {
        return new ToolBar(controller, "Start", "Free mode");
    }

    @Override
    Image getInfoImage() {
        return coin;
    }

    @Override
    String getInfoText() {
        return Integer.toString(gameManager.getPoints());
    }

    @Override
    void setGrid() {
        super.setGrid();
        info.setText(Integer.toString(gameManager.getPoints()));
    }

    @Override
    void handleStatus(StatusEvent e) {
        Status status = e.getStatus();
        this.status.setText(status.getText());
        if (status == Status.SOLVED) {
            controller.showAlert(Alert.AlertType.INFORMATION, "Solved!", "Solved.");
        }
    }
}
