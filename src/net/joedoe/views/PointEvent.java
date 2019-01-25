package net.joedoe.views;

import javafx.event.Event;

/**
 * Enthält aktuellen Punktestatus, über den die Punkte-Zeile im
 * {@link Board} informiert wird.
 */
class PointEvent extends Event {
    private int points;

    /**
     * Werden aktuelle Punkte übergeben.
     *
     * @param points Punkte des Spielers
     */
    PointEvent(int points) {
        super(null);
        this.points = points;
    }

    /**
     * Gibt die aktuellen Punkte des Spielers zurück.
     *
     * @return Punkte des Spielers
     */
    int getPoints() {
        return points;
    }
}
