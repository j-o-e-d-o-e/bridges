package net.joedoe.views;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import net.joedoe.entities.IBridge;
import net.joedoe.entities.IIsle;
import net.joedoe.utils.GameData;
import net.joedoe.utils.Mocks;

import java.io.File;
import java.util.List;

import static net.joedoe.utils.GameInfo.CONTAINER_OFFSET;

/**
 * Spielfeld, das das Raster enthält, auf dem Inseln und Brücken platziert
 * werden, und dessen Erstellung und Größe verwaltet.
 */
class Board extends StackPane {
    private Grid grid;
    private int width, height;
    private boolean showMissingBridges = true;
    private EventHandler<StatusEvent> statusListener;
    private GameData gameData = GameData.getInstance();

    /**
     * Board wird Listener übergeben, um die Statuszeile im
     * {@link net.joedoe.views.MainFrame} über Änderungen zu benachrichtigen. Es
     * erzeugt das Raster, auf dem die Inseln und Brücken platziert werden.
     *
     * @param statusListener Listener, über den die Statuszeile über Änderungen informiert wird
     */
    Board(EventHandler<StatusEvent> statusListener) {
        this.width = Mocks.WIDTH;
        this.height = Mocks.HEIGHT;
        this.statusListener = statusListener;
        setPadding(new Insets(CONTAINER_OFFSET, CONTAINER_OFFSET, 0, CONTAINER_OFFSET));
        grid = new Grid(statusListener, width, height, Mocks.getIsles(), null);
        // grid = new Grid(statusListener, width, height, Mocks.getIsles(),
        // Mocks.getBridges());
        getChildren().add(grid);
        setShowMissingBridges(showMissingBridges);

//        ImageView imageView = new ImageView();
//        Image image = new Image("file:assets" + File.separator + "images" + File.separator + "water.gif");
//        imageView.setImage(image);
//        getChildren().add(imageView);


        String file = "assets" + File.separator + "sounds" + File.separator + "waves.wav";
        Media sound = new Media(new File(file).toURI().toString());
        MediaPlayer player =new MediaPlayer(sound);
        player.setOnEndOfMedia(() -> player.seek(Duration.ZERO));
        player.play();
    }

    /**
     * Erzeugt neues Raster mit generierten Spiel-Daten.
     *
     * @param width   Breite des Spielfelds
     * @param height  Höhe des Spielfelds
     * @param isles   Inseln, die auf dem Spielfeld platziert werden
     * @param bridges Brücken, die auf dem Spielfeld platziert werden
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
     * @param selected true, falls fehlende Brücken angezeigt werden sollen
     */
    void setShowMissingBridges(boolean selected) {
        showMissingBridges = selected;
        grid.setShowMissingBridges(showMissingBridges);
    }

    /**
     * Leitet Nutzer-Eingabe "Nächste Brücke" an {@link net.joedoe.views.Grid}
     * weiter.
     */
    void getNextBridge() {
        grid.getNextBridge();
    }

    /**
     * Leitet Nutzer-Eingabe "Automatisch lösen" an {@link net.joedoe.views.Grid}
     * weiter.
     */
    void startAutoSolve() {
        grid.startAutoSolve();
    }

    /**
     * Leitet Nutzer-Eingabe, dass automatisches Lösen gestoppt werden soll, an
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
     * Gibt zurück, ob {@link net.joedoe.logics.AutoSolver} aktuell läuft.
     *
     * @return true, falls {@link net.joedoe.logics.AutoSolver} aktuell läuft
     */
    boolean autoSolverIsRunning() {
        return grid.autoSolverIsRunning();
    }

    /**
     * Leitet Nutzer-Eingabe, dass Spielfortschritt zurückgesetzt werden soll, an
     * {@link net.joedoe.views.Grid} weiter.
     */
    void reset() {
        grid.reset();
    }

    /**
     * Speichert Breite und Höhe des Spielfelds als Spieldaten und leitet
     * Nutzer-Anfrage zur Speicherung der aktuellen Inseln und Brücken an
     * {@link net.joedoe.views.Grid} weiter.
     */
    void saveGame() {
        gameData.setWidth(width);
        gameData.setHeight(height);
        grid.saveGame();
    }
}
