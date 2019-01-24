package net.joedoe.views;

import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import net.joedoe.utils.GameManager;

import java.io.File;

class BoardLevel extends Board {
    private GameManager gameManager = GameManager.getInstance();

    BoardLevel(SceneController controller) {
        super(controller);
        setLayout();
    }

    @Override
    ToolBar createToolBar() {
        System.out.println(gameManager);
        String title = "Level " + gameManager.getLevel() + "/25";
        return new ToolBar(controller, "Start", title);
    }

    @Override
    Image getInfoImage() {
        return new Image("file:assets" + File.separator + "images" + File.separator + "coin.png");
    }

    @Override
    String getInfoText() {
        return Integer.toString(gameManager.getPoints());
    }

    @Override
    void setGrid() {
        super.setGrid();
        toolBar.updateTitle("Level " + gameManager.getLevel() + "/25");
        info.setText(Integer.toString(gameManager.getPoints()));
    }

    @Override
    void handleStatus(StatusEvent e) {
        StatusEvent.Status status = e.getStatus();
        this.status.setText(status.getText());
        if (status == StatusEvent.Status.SOLVED) {
            controller.showAlert(Alert.AlertType.INFORMATION, "Solved!", "Level " + gameManager.getLevel() + " solved.");
            gameManager.savePoints();
            gameManager.increaseLevel();
            setGrid();
        }
    }
}
