package net.joedoe.logics;

import net.joedoe.utils.Coordinate;
import net.joedoe.views.AutoSolverListener;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AutoSolver {
    private GridController controller;
    private final ExecutorService service = Executors.newFixedThreadPool(1);
    private boolean running;
    private AutoSolverListener listener;

    public AutoSolver(GridController controller) {
        this.controller = controller;
    }

    public void start() {
        running = true;
        service.submit(this::run);
    }

    private void run() {
        while (running) {
            listener.onChange();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public Coordinate[] getNextBridge() {
        return controller.getNextBridge();
    }


    public void stop() {
        running = false;
        service.shutdown();
    }

    public boolean isRunning() {
        return running;
    }

    public void addListener(AutoSolverListener listener) {
        this.listener = listener;
    }
}
