package net.joedoe.views;

import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import net.joedoe.utils.Direction;

import static net.joedoe.utils.GameInfo.ONE_TILE;

public class IsleListener implements EventHandler<MouseEvent> {
    private Grid grid;

    IsleListener(Grid grid) {
        this.grid = grid;
    }

    @Override
    public void handle(MouseEvent e) {
        IslePane pane = ((IslePane) e.getSource());
        if (0 < e.getX() && e.getX() < ONE_TILE && 0 < e.getY() && e.getY() < ONE_TILE / 2
                && Math.abs(e.getY() - (ONE_TILE >> 1)) > Math.abs(e.getX() - (ONE_TILE >> 1))) {
            if (e.getButton() == MouseButton.PRIMARY)
                grid.addBridge(pane, Direction.UP);
            if (e.getButton() == MouseButton.SECONDARY)
                grid.removeBridge(pane, Direction.UP);
        }
        if (0 < e.getX() && e.getX() < ONE_TILE / 2 && 0 < e.getY() && e.getY() < ONE_TILE
                && Math.abs(e.getY() - (ONE_TILE >> 1)) < Math.abs(e.getX() - (ONE_TILE >> 1))) {
            if (e.getButton() == MouseButton.PRIMARY)
                grid.addBridge(pane, Direction.LEFT);
            if (e.getButton() == MouseButton.SECONDARY)
                grid.removeBridge(pane, Direction.LEFT);
        }
        if (0 < e.getX() && e.getX() < ONE_TILE && ONE_TILE / 2 < e.getY() && e.getY() < ONE_TILE
                && Math.abs(e.getY() - (ONE_TILE >> 1)) > Math.abs(e.getX() - (ONE_TILE >> 1))) {
            if (e.getButton() == MouseButton.PRIMARY)
                grid.addBridge(pane, Direction.DOWN);
            if (e.getButton() == MouseButton.SECONDARY)
                grid.removeBridge(pane, Direction.DOWN);
        }
        if (ONE_TILE / 2 < e.getX() && e.getX() < ONE_TILE && 0 < e.getY() && e.getY() < ONE_TILE
                && Math.abs(e.getY() - (ONE_TILE >> 1)) < Math.abs(e.getX() - (ONE_TILE >> 1))) {
            if (e.getButton() == MouseButton.PRIMARY)
                grid.addBridge(pane, Direction.RIGHT);
            if (e.getButton() == MouseButton.SECONDARY)
                grid.removeBridge(pane, Direction.RIGHT);
        }
    }
}