package net.joedoe.views.board;

import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import net.joedoe.utils.Direction;

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
     * Wird {@link Grid} übergeben, die aufgerufen wird,
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
    @SuppressWarnings("Duplicates")
    @Override
    public void handle(MouseEvent e) {
        IslePane isle = ((IslePane) e.getSource());
        // Klick-Sektor oben
        if (0 < e.getX() && e.getX() < ONE_TILE && 0 < e.getY() && e.getY() < ONE_TILE / 2
                && Math.abs(e.getY() - (ONE_TILE >> 1)) > Math.abs(e.getX() - (ONE_TILE >> 1))) {
            LOGGER.info(isle.toString() + ": Click Top -> " + e.getX() + "/" + e.getY());
            if (e.getButton() == MouseButton.PRIMARY) grid.addBridge(isle, Direction.UP);
            if (e.getButton() == MouseButton.SECONDARY) grid.removeBridge(isle, Direction.UP);
        }
        // Klick-Sektor links
        if (0 < e.getX() && e.getX() < ONE_TILE / 2 && 0 < e.getY() && e.getY() < ONE_TILE
                && Math.abs(e.getY() - (ONE_TILE >> 1)) < Math.abs(e.getX() - (ONE_TILE >> 1))) {
            LOGGER.info(isle.toString() + ": Click Left -> " + e.getX() + "/" + e.getY());
            if (e.getButton() == MouseButton.PRIMARY) grid.addBridge(isle, Direction.LEFT);
            if (e.getButton() == MouseButton.SECONDARY) grid.removeBridge(isle, Direction.LEFT);
        }
        // Klick-Sektor unten
        if (0 < e.getX() && e.getX() < ONE_TILE && ONE_TILE / 2 < e.getY() && e.getY() < ONE_TILE
                && Math.abs(e.getY() - (ONE_TILE >> 1)) > Math.abs(e.getX() - (ONE_TILE >> 1))) {
            LOGGER.info(isle.toString() + ": Click Bottom -> " + e.getX() + "/" + e.getY());
            if (e.getButton() == MouseButton.PRIMARY) grid.addBridge(isle, Direction.DOWN);
            if (e.getButton() == MouseButton.SECONDARY) grid.removeBridge(isle, Direction.DOWN);
        }
        // KLick-Sektor rechts
        if (ONE_TILE / 2 < e.getX() && e.getX() < ONE_TILE && 0 < e.getY() && e.getY() < ONE_TILE
                && Math.abs(e.getY() - (ONE_TILE >> 1)) < Math.abs(e.getX() - (ONE_TILE >> 1))) {
            LOGGER.info(isle.toString() + ": Click Right -> " + e.getX() + "/" + e.getY());
            if (e.getButton() == MouseButton.PRIMARY) grid.addBridge(isle, Direction.RIGHT);
            if (e.getButton() == MouseButton.SECONDARY) grid.removeBridge(isle, Direction.RIGHT);
        }
    }
}