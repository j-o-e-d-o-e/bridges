package net.joedoe.views;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.layout.StackPane;
import net.joedoe.utils.GameData;
import net.joedoe.utils.Mocks;

import static net.joedoe.utils.GameInfo.CONTAINER_OFFSET;

class Board extends StackPane {
    private Grid grid;
    private int width, height;
    private EventHandler<StatusEvent> statusListener;
    private GameData gameData = GameData.getInstance();

    Board(EventHandler<StatusEvent> statusListener) {
        this.width = Mocks.WIDTH;
        this.height = Mocks.HEIGHT;
        this.statusListener = statusListener;
        setPadding(new Insets(CONTAINER_OFFSET, CONTAINER_OFFSET, 0, CONTAINER_OFFSET));
        grid = new Grid(statusListener, width, height, Mocks.ISLES, null);
//        grid = new Grid(statusListener, width, height, Mocks.ISLES, Mocks.BRIDGES);
        getChildren().add(grid);
    }

    void setGrid(int width, int height, Object[][] islesData, Object[][] bridgesData) {
        this.width = width;
        this.height = height;
        getChildren().remove(grid);
        grid.shutdownAutoSolve();
        grid = new Grid(statusListener, width, height, islesData, bridgesData);
        getChildren().add(grid);
    }
    
    void setLoadedGrid() {
        setGrid(gameData.getWidth(), gameData.getHeight(), gameData.getIsles(), gameData.getBridges());
    }

    void setShowMissingBridges(boolean selected) {
        grid.setShowMissingBridges(selected);
    }

    void reset() {
        grid.reset();
    }

    void getNextBridge() {
        grid.getNextBridge();
    }

    void startAutoSolve() {
        grid.startAutoSolve();
    }

    void stopAutoSolve() {
        grid.stopAutoSolve();
    }

    void shutdownAutoSolve() {
        grid.shutdownAutoSolve();
    }
    
    boolean autoSolverIsRunning() {
        return grid.autoSolverIsRunning();
    }

    void saveGame() {
        gameData.setWidth(width);
        gameData.setHeight(height);
        grid.saveGame();
    }
}
