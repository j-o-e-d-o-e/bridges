package net.joedoe.views;

import javafx.event.Event;

/**
 * Enthält aktuellen Spielstatus, über den die Status-Zeile im
 * {@link net.joedoe.views.MainFrame} informiert wird.
 */
class StatusEvent extends Event {
    private static final long serialVersionUID = 225178481951279734L;
    private String status;

    /**
     * Wird Text übergeben, der den aktuellen Spielstatus beschreibt: Entweder
     * "Enthält Fehler.", "Nicht mehr lösbar.", "Gelöst!" oder "Noch nicht gelöst."
     *
     * @param status Text, der den aktuellen Spielstatus beschreibt
     */
    StatusEvent(String status) {
        super(null);
        this.status = status;
    }

    /**
     * Gibt den aktuellen Spiel-Status zurück.
     *
     * @return Text zum aktuellen Spielstatus
     */
    String getStatus() {
        return status;
    }
}
