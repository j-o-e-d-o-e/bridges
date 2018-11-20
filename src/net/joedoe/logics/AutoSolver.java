package net.joedoe.logics;

import net.joedoe.utils.Coordinate;
import net.joedoe.views.SolverListener;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AutoSolver {
    private Solver solver;
    private final ExecutorService service = Executors.newCachedThreadPool();
    private boolean running;
    private SolverListener listener;

    public AutoSolver(Solver solver) {
        this.solver = solver;
    }

    public void start() {
        running = true;
        service.submit(this::run);
    }

    private void run() {
        while (running) {
            listener.onChange();
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public Coordinate[] getNextBridge() {
        return solver.getNextBridge();
    }


    public void stop() {
        running = false;
    }

    public void shutdown() {
        running = false;
        service.shutdown();
    }

    public boolean isRunning() {
        return running;
    }

    public void addListener(SolverListener listener) {
        this.listener = listener;
    }
}
