package net.joedoe.logics;

import net.joedoe.utils.Coordinate;
import net.joedoe.views.SolverListener;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AutoSolver {
    private final ExecutorService service = Executors.newCachedThreadPool();
    private boolean running = true;
    private GridController controller;
    private SolverListener listener;

    public AutoSolver(GridController controller) {
        this.controller = controller;
        service.submit(() -> {
            while (running) {
                listener.onChange();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public Coordinate[] getNextBridge() {
        return controller.getNextBridge();
    }

    public void stop() {
        running = false;
        service.shutdown();
    }

    public void addListener(SolverListener listener) {
        this.listener = listener;
    }
}
