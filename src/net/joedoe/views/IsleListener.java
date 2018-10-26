package net.joedoe.views;

import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import net.joedoe.entities.Isle;
import net.joedoe.utils.Direction;

import static net.joedoe.utils.GameInfo.ONE_TILE;

public class IsleListener implements EventHandler<MouseEvent> {
    private Grid grid;

    @SuppressWarnings("WeakerAccess")
    public IsleListener(Grid grid) {
        this.grid = grid;
    }

    @Override
    public void handle(MouseEvent e) {
        if (ONE_TILE / 2 < e.getX() && e.getX() < ONE_TILE && 0 < e.getY() && e.getY() < ONE_TILE
                && Math.abs(e.getY() - (ONE_TILE >> 1)) < Math.abs(e.getX() - (ONE_TILE >> 1))) {
            if (e.getButton() == MouseButton.PRIMARY) {
//                ((Isle)e.getSource()).addBridge();
                grid.addBridge((Isle) e.getSource(), Direction.RIGHT);
            }
            if (e.getButton() == MouseButton.SECONDARY) {
                grid.removeBridge((Isle) e.getSource(), Direction.RIGHT);
//                ((Isle)e.getSource()).removeBridge();
            }
        }
        if (0 < e.getX() && e.getX() < ONE_TILE / 2 && 0 < e.getY() && e.getY() < ONE_TILE
                && Math.abs(e.getY() - (ONE_TILE >> 1)) < Math.abs(e.getX() - (ONE_TILE >> 1))) {
            if (e.getButton() == MouseButton.PRIMARY) {
//                ((Isle)e.getSource()).addBridge();
                grid.addBridge((Isle) e.getSource(), Direction.LEFT);
            }
            if (e.getButton() == MouseButton.SECONDARY) {
                grid.removeBridge((Isle) e.getSource(), Direction.LEFT);
//                ((Isle)e.getSource()).removeBridge();
            }
        }
        if (0 < e.getX() && e.getX() < ONE_TILE && ONE_TILE / 2 < e.getY() && e.getY() < ONE_TILE
                && Math.abs(e.getY() - (ONE_TILE >> 1)) > Math.abs(e.getX() - (ONE_TILE >> 1))) {
            if (e.getButton() == MouseButton.PRIMARY) {
//                ((Isle)e.getSource()).addBridge();
                grid.addBridge((Isle) e.getSource(), Direction.DOWN);
            }
            if (e.getButton() == MouseButton.SECONDARY) {
                grid.removeBridge((Isle) e.getSource(), Direction.DOWN);
//                ((Isle)e.getSource()).removeBridge();
            }
        }
        if (0 < e.getX() && e.getX() < ONE_TILE && 0 < e.getY() && e.getY() < ONE_TILE / 2
                && Math.abs(e.getY() - (ONE_TILE >> 1)) > Math.abs(e.getX() - (ONE_TILE >> 1))) {
            if (e.getButton() == MouseButton.PRIMARY) {
//                ((Isle)e.getSource()).addBridge();
                grid.addBridge((Isle) e.getSource(), Direction.UP);
            }
            if (e.getButton() == MouseButton.SECONDARY) {
                grid.removeBridge((Isle) e.getSource(), Direction.UP);
//                ((Isle)e.getSource()).removeBridge();
            }
        }
    }
}