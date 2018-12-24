package net.joedoe.views;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import net.joedoe.entities.IBridge;
import net.joedoe.entities.IIsle;
import net.joedoe.utils.GameData;
import net.joedoe.utils.Mocks;

import java.io.File;
import java.util.List;

import static net.joedoe.utils.GameInfo.CONTAINER_OFFSET;
import static net.joedoe.utils.GameInfo.ONE_TILE;

/**
 * Spielfeld, das das Raster enth�lt, auf dem Inseln und Br�cken platziert
 * werden, und dessen Erstellung und Gr��e verwaltet.
 */
class Board extends StackPane {
    private Grid grid;
    private int width, height;
    private boolean showMissingBridges = true;
    private EventHandler<StatusEvent> statusListener;
    private GameData gameData = GameData.getInstance();

    /**
     * Board wird Listener �bergeben, um die Statuszeile im
     * {@link net.joedoe.views.MainFrame} �ber �nderungen zu benachrichtigen. Es
     * erzeugt das Raster, auf dem die Inseln und Br�cken platziert werden.
     *
     * @param statusListener Listener, �ber den die Statuszeile �ber �nderungen informiert wird
     */
    Board(EventHandler<StatusEvent> statusListener) {
        this.width = Mocks.WIDTH;
        this.height = Mocks.HEIGHT;
        this.statusListener = statusListener;
        setPadding(new Insets(CONTAINER_OFFSET, CONTAINER_OFFSET, 0, CONTAINER_OFFSET));
        grid = new Grid(statusListener, width, height, Mocks.getIsles(), null);
        // grid = new Grid(statusListener, width, height, Mocks.getIsles(),
        // Mocks.getBridges());
        getChildren().add(setWaves());
        getChildren().add(grid);
        setShowMissingBridges(showMissingBridges);
    }

    private Node setWaves() {
//        TilePane tile = new TilePane();
        FlowPane tile = new FlowPane();
        tile.setPadding(new Insets(5, 0, 5, 0));
        tile.setVgap(4);
        tile.setHgap(4);
//        tile.setPrefColumns(50);
//        tile.setPrefRows(4);
        tile.setId("pane");
        Image waves = new Image("file:assets" + File.separator + "images" + File.separator + "waves.gif");
        Image water = new Image("file:assets" + File.separator + "images" + File.separator + "water.png");
        Image waterBG = new Image("file:assets" + File.separator + "images" + File.separator + "waterBG.gif");
//        ImageView view = new ImageView(waterBG);
//        tile.getChildren().add(view);
        for (int i = 0; i < 1000; i++) {
            ImageView view;
            if (i % 8 == 0) {
                view = new ImageView(waves);
                tile.getChildren().add(view);
                view.setFitHeight(ONE_TILE >> 1);
                view.setPreserveRatio(true);
                view = new ImageView(waves);
                tile.getChildren().add(view);
            } else {
                view = new ImageView(water);
                tile.getChildren().add(view);
            }
            view.setFitHeight(ONE_TILE >> 1);
            view.setPreserveRatio(true);
        }
        return tile;
    }

    /**
     * Erzeugt neues Raster mit generierten Spiel-Daten.
     *
     * @param width   Breite des Spielfelds
     * @param height  H�he des Spielfelds
     * @param isles   Inseln, die auf dem Spielfeld platziert werden
     * @param bridges Br�cken, die auf dem Spielfeld platziert werden
     */
    void setGrid(int width, int height, List<IIsle> isles, List<IBridge> bridges) {
        this.width = width;
        this.height = height;
        getChildren().remove(grid);
        grid.shutdownAutoSolve();
        grid = new Grid(statusListener, width, height, isles, bridges);
        getChildren().add(grid);
        setShowMissingBridges(showMissingBridges);
    }

    /**
     * Erzeugt neues Raster mit geladenen Spiel-Daten.
     */
    void setLoadedGrid() {
        setGrid(gameData.getWidth(), gameData.getHeight(), gameData.getIsles(), gameData.getBridges());
    }

    /**
     * Leitet Nutzer-Eingabe an {@link net.joedoe.views.Grid} weiter.
     *
     * @param selected true, falls fehlende Br�cken angezeigt werden sollen
     */
    void setShowMissingBridges(boolean selected) {
        showMissingBridges = selected;
        grid.setShowMissingBridges(showMissingBridges);
    }

    /**
     * Leitet Nutzer-Eingabe "N�chste Br�cke" an {@link net.joedoe.views.Grid}
     * weiter.
     */
    void getNextBridge() {
        grid.getNextBridge();
    }

    /**
     * Leitet Nutzer-Eingabe "Automatisch l�sen" an {@link net.joedoe.views.Grid}
     * weiter.
     */
    void startAutoSolve() {
        grid.startAutoSolve();
    }

    /**
     * Leitet Nutzer-Eingabe, dass automatisches L�sen gestoppt werden soll, an
     * {@link net.joedoe.views.Grid} weiter.
     */
    void stopAutoSolve() {
        grid.stopAutoSolve();
    }

    /**
     * Leitet Aufruf an {@link net.joedoe.views.Grid} weiter, dass
     * {@link net.joedoe.logics.AutoSolver} terminiert werden soll, wenn Applikation
     * geschlossen wird.
     */
    void shutdownAutoSolve() {
        grid.shutdownAutoSolve();
    }

    /**
     * Gibt zur�ck, ob {@link net.joedoe.logics.AutoSolver} aktuell l�uft.
     *
     * @return true, falls {@link net.joedoe.logics.AutoSolver} aktuell l�uft
     */
    boolean autoSolverIsRunning() {
        return grid.autoSolverIsRunning();
    }

    /**
     * Leitet Nutzer-Eingabe, dass Spielfortschritt zur�ckgesetzt werden soll, an
     * {@link net.joedoe.views.Grid} weiter.
     */
    void reset() {
        grid.reset();
    }

    /**
     * Speichert Breite und H�he des Spielfelds als Spieldaten und leitet
     * Nutzer-Anfrage zur Speicherung der aktuellen Inseln und Br�cken an
     * {@link net.joedoe.views.Grid} weiter.
     */
    void saveGame() {
        gameData.setWidth(width);
        gameData.setHeight(height);
        grid.saveGame();
    }
}
