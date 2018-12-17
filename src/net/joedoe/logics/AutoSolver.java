package net.joedoe.logics;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import net.joedoe.entities.IBridge;
import net.joedoe.views.AutoSolverListener;

/**
 * Klasse f�r das automatische L�sen.
 *
 */
public class AutoSolver {
    private Solver solver;
    private final ExecutorService service = Executors.newCachedThreadPool();
    private AutoSolverListener listener;
    private boolean running;

    /**
     * Klasse wird {@link net.joedoe.logics.Solver} �bergeben, um neue Br�cke zu
     * ermitteln.
     * 
     * @param solver
     *            enth�lt die Logik zur Ermittlung einer neuen Br�cke
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
     * Gibt die n�chste Br�cke zur�ck.
     * 
     * @return neue Br�cke
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
     * Schlie�t Thread.
     */
    public void shutdown() {
        running = false;
        service.shutdown();
    }

    /**
     * Checkt, ob Thread aktuell l�uft.
     * 
     * @return true, falls Thread aktuell l�uft.
     */
    public boolean isRunning() {
        return running;
    }

    /**
     * Setzt den Listener, um {@link net.joedoe.views.Grid} zu benachrichtigen.
     * 
     * @param listener
     *            enth�lt auszuf�hrende Methode
     */
    public void setListener(AutoSolverListener listener) {
        this.listener = listener;
    }
}
