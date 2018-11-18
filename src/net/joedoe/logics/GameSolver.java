package net.joedoe.logics;

import net.joedoe.utils.Coordinate;

public class GameSolver implements Runnable {
    private GridController controller;
    private Coordinate[] next;

    public GameSolver(GridController controller) {
        this.controller = controller;
    }

    @Override
    public void run() {
        if (!controller.gameSolved()) {
            next = controller.showNextBridge();
            System.out.println("Sugar");
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public Coordinate[] getNext() {
        return next;
    }
}
