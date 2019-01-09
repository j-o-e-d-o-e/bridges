package net.joedoe.views;

import javafx.event.Event;

/**
 * Enthält aktuellen Punktestatus, über den die Punkte-Zeile im
 * {@link MainFrame} informiert wird.
 */
class PointEvent extends Event {
    private String points;

    /**
     * Werden aktuelle Punkte übergeben.
     *
     * @param points Punkte des Spielers
     */
    PointEvent(int points) {
        super(null);
        this.points = String.valueOf(points);
    }

    /**
     * Gibt die aktuellen Punkte des Spielers zurück.
     *
     * @return Punkte des Spielers
     */
    String getPoints() {
        return points;
    }
}
