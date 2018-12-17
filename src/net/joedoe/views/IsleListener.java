package net.joedoe.views;

import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import net.joedoe.utils.Direction;
import net.joedoe.viewmodel.IslePane;

import java.util.logging.Level;
import java.util.logging.Logger;

import static net.joedoe.utils.GameInfo.ONE_TILE;

/**
 * Implementierung der Klick-Sektoren.
 */
public class IsleListener implements EventHandler<MouseEvent> {
    private Grid grid;

    private final static Logger LOGGER = Logger.getLogger(IsleListener.class.getName());

    /**
     * Wird {@link net.joedoe.views.Grid} übergeben, die aufgerufen wird,
     * nachdem die Insel und ihr angeglickter Klick-Sektor ermittelt wurde.
     *
     * @param grid wird nach Ermittlung des Klick-Sektors aufgerufen
     */
    IsleListener(Grid grid) {
        this.grid = grid;
        LOGGER.setLevel(Level.OFF);
    }

    /**
     * Ermittelt die Insel, den Klick-Sektor und ob mit der linken oder rechten
     * Maustaste geklickt wurde.
     */
    @Override
    public void handle(MouseEvent e) {
        IslePane pane = ((IslePane) e.getSource());
        // Klick-Sektor oben
        if (0 < e.getX() && e.getX() < ONE_TILE && 0 < e.getY() && e.getY() < ONE_TILE / 2
                && Math.abs(e.getY() - (ONE_TILE >> 1)) > Math.abs(e.getX() - (ONE_TILE >> 1))) {
            LOGGER.info(pane.toString() + ": Click Up");
            if (e.getButton() == MouseButton.PRIMARY) grid.addBridge(pane, Direction.UP);
            if (e.getButton() == MouseButton.SECONDARY) grid.removeBridge(pane, Direction.UP);
        }
        // Klick-Sektor links
        if (0 < e.getX() && e.getX() < ONE_TILE / 2 && 0 < e.getY() && e.getY() < ONE_TILE
                && Math.abs(e.getY() - (ONE_TILE >> 1)) < Math.abs(e.getX() - (ONE_TILE >> 1))) {
            LOGGER.info(pane.toString() + ": Click Left");
            if (e.getButton() == MouseButton.PRIMARY) grid.addBridge(pane, Direction.LEFT);
            if (e.getButton() == MouseButton.SECONDARY) grid.removeBridge(pane, Direction.LEFT);
        }
        // Klick-Sektor unten
        if (0 < e.getX() && e.getX() < ONE_TILE && ONE_TILE / 2 < e.getY() && e.getY() < ONE_TILE
                && Math.abs(e.getY() - (ONE_TILE >> 1)) > Math.abs(e.getX() - (ONE_TILE >> 1))) {
            LOGGER.info(pane.toString() + ": Click Down");
            if (e.getButton() == MouseButton.PRIMARY) grid.addBridge(pane, Direction.DOWN);
            if (e.getButton() == MouseButton.SECONDARY) grid.removeBridge(pane, Direction.DOWN);
        }
        // KLick-Sektor rechts
        if (ONE_TILE / 2 < e.getX() && e.getX() < ONE_TILE && 0 < e.getY() && e.getY() < ONE_TILE
                && Math.abs(e.getY() - (ONE_TILE >> 1)) < Math.abs(e.getX() - (ONE_TILE >> 1))) {
            LOGGER.info(pane.toString() + ": Click Right");
            if (e.getButton() == MouseButton.PRIMARY) grid.addBridge(pane, Direction.RIGHT);
            if (e.getButton() == MouseButton.SECONDARY) grid.removeBridge(pane, Direction.RIGHT);
        }
    }
}