package net.joedoe.logics;

/**
 * Listener für {@link net.joedoe.logics.AutoSolver}, um Daten an View zurückzugeben.
 */
@FunctionalInterface
public interface AutoSolverListener {

    void onChange();
}
