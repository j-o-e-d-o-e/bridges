package net.joedoe.controllers;

public class GameSolver implements Runnable {
    private GridController controller;

    public GameSolver(GridController controller) {
        this.controller = controller;
    }

    @Override
    public void run() {
        controller.getBridges();

    }
}
