package net.joedoe.logics;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import net.joedoe.entities.IBridge;
import net.joedoe.views.AutoSolverListener;

/**
 * Klasse für das automatische Lösen.
 *
 */
public class AutoSolver {
    private Solver solver;
    private final ExecutorService service = Executors.newCachedThreadPool();
    private AutoSolverListener listener;
    private boolean running;

    /**
     * Klasse wird {@link net.joedoe.logics.Solver} übergeben, um neue Brücke zu
     * ermitteln.
     * 
     * @param solver
     *            enthält die Logik zur Ermittlung einer neuen Brücke
     */
    public AutoSolver(Solver solver) {
        this.solver = solver;
    }

    /**
     * Startet den Thread.
     */
    public void start() {
        running = true;
        service.submit(this::run);
    }

    private void run() {
        while (running) {
            listener.onChange();
            try {
                Thread.sleep(50); // 2000
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Gibt die nächste Brücke zurück.
     * 
     * @return neue Brücke
     */
    public IBridge getNextBridge() {
        return solver.getNextBridge();
    }

    /**
     * Stoppt Thread.
     */
    public void stop() {
        running = false;
    }

    /**
     * Schließt Thread.
     */
    public void shutdown() {
        running = false;
        service.shutdown();
    }

    /**
     * Checkt, ob Thread aktuell läuft.
     * 
     * @return true, falls Thread aktuell läuft.
     */
    public boolean isRunning() {
        return running;
    }

    /**
     * Setzt den Listener, um {@link net.joedoe.views.Grid} zu benachrichtigen.
     * 
     * @param listener
     *            enthält auszuführende Methode
     */
    public void setListener(AutoSolverListener listener) {
        this.listener = listener;
    }
}
