package net.joedoe.logics;

/**
 * Listener f�r {@link net.joedoe.logics.AutoSolver}, um Daten an View zur�ckzugeben.
 */
@FunctionalInterface
public interface AutoSolverListener {

    void onChange();
}
