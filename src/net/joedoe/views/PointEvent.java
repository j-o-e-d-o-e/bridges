package net.joedoe.views;

import javafx.event.Event;

/**
 * Enthält aktuellen Punktestatus, über den die Punkte-Zeile im
 * {@link MainFrame} informiert wird.
 */
class PointEvent extends Event {
    private String points;
    private boolean solved;

    /**
     * Werden aktuelle Punkte übergeben.
     *
     * @param points Punkte des Spielers
     */
    PointEvent(int points, boolean solved) {
        super(null);
        this.points = String.valueOf(points);
        this.solved = solved;
    }

    /**
     * Gibt die aktuellen Punkte des Spielers zurück.
     *
     * @return Punkte des Spielers
     */
    String getPoints() {
        return points;
    }

    boolean isSolved() {
        return solved;
    }
}
