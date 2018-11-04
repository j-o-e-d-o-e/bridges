package net.joedoe.views;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.layout.StackPane;
import net.joedoe.entities.Isle;

import java.util.List;

import static net.joedoe.utils.GameInfo.CONTAINER_OFFSET;

class Board extends StackPane {
    private Grid grid;
    private EventHandler<StatusEvent> statusListener;

    Board(EventHandler<StatusEvent> statusListener) {
        this.statusListener = statusListener;
        setPadding(new Insets(CONTAINER_OFFSET, CONTAINER_OFFSET, 0, CONTAINER_OFFSET));
        grid = new Grid();
        grid.setStatusListener(statusListener);
        getChildren().add(grid);
    }

    void setGrid(int height, int width, List<Isle> isles) {
        getChildren().remove(grid);
        grid = new Grid(height, width, isles);
        grid.setStatusListener(statusListener);
        getChildren().add(grid);
    }

    void setShowMissingBridges(boolean selected) {
        grid.setShowMissingBridges(selected);
    }
}
