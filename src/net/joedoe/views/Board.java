package net.joedoe.views;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import net.joedoe.entities.IBridge;
import net.joedoe.entities.IIsle;
import net.joedoe.logics.Generator;
import net.joedoe.utils.GameData;
import net.joedoe.utils.GameInfo;

import java.io.File;
import java.util.List;

import static net.joedoe.utils.GameInfo.CONTAINER_OFFSET;

/**
 * Spielfeld, das das Raster enth�lt, auf dem Inseln und Br�cken platziert
 * werden, und dessen Erstellung und Gr��e verwaltet.
 */
class Board extends StackPane {
    private Grid grid;
    private Generator generator;
    private int width, height;
    private boolean showMissingBridges = true;
    private EventHandler<StatusEvent> statusListener;
    private GameData gameData = GameData.getInstance();
    private MediaPlayer player;
    private EventHandler<PointEvent> pointListener;

    /**
     * Board wird Listener �bergeben, um die Statuszeile im
     * {@link net.joedoe.views.MainFrame} �ber �nderungen zu benachrichtigen. Es
     * erzeugt das Raster, auf dem die Inseln und Br�cken platziert werden.
     *
     * @param statusListener Listener, �ber den die Statuszeile �ber �nderungen informiert wird
     */
    Board(EventHandler<StatusEvent> statusListener, int level) {
        this.statusListener = statusListener;
        setPadding(new Insets(CONTAINER_OFFSET, CONTAINER_OFFSET, 0, CONTAINER_OFFSET));
        String file = "assets" + File.separator + "sounds" + File.separator + "waves.wav";
        Media sound = new Media(new File(file).toURI().toString());
        player = new MediaPlayer(sound);
        player.setOnEndOfMedia(() -> player.seek(Duration.ZERO));
        player.play();
        generator = new Generator();
        createNewGame(level);
    }

    void setGrid() {
        setGrid(gameData.getWidth(), gameData.getHeight(), gameData.getIsles(), null);
    }

    void setGridWithBridges() {
        setGrid(gameData.getWidth(), gameData.getHeight(), gameData.getIsles(), gameData.getBridges());
    }

    private void setGrid(int width, int height, List<IIsle> isles, List<IBridge> bridges) {
        this.width = width;
        this.height = height;
        getChildren().remove(grid);
        if (grid != null) grid.shutdownAutoSolve();
        grid = new Grid(statusListener, width, height, isles, bridges);
        grid.setPointListener(pointListener);
        ScrollPane scroll = new ScrollPane();
        scroll.setContent(grid);
        scroll.setFitToHeight(true);
        scroll.setFitToWidth(true);
        getChildren().add(scroll);
        setShowMissingBridges(showMissingBridges);
    }

    /**
     * Inseln vergr��ern.
     */
    void zoomIn() {
        GameInfo.zoomIn();
        grid.zoomInOut(width, height, showMissingBridges);
    }

    /**
     * Inseln verkleinern.
     */
    void zoomOut() {
        GameInfo.zoomOut();
        grid.zoomInOut(width, height, showMissingBridges);
    }

    void setSound() {
        if (player.getStatus() == MediaPlayer.Status.PLAYING) player.pause();
        else player.play();
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

    void setPointListener(EventHandler<PointEvent> listener) {
        pointListener = listener;
        grid.setPointListener(listener);
    }

    void createNewGame(int level) {
        generator.setData(5 * level);
        generator.generateGame();
        setGrid(generator.getWidth(), generator.getHeight(), generator.getIsles(), null);
    }
}
