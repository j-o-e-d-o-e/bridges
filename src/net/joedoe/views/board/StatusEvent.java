package net.joedoe.views.board;

import javafx.event.Event;

/**
 * Enthält aktuellen Spielstatus, über den die Status-Zeile im
 * {@link Board} informiert wird.
 */
class StatusEvent extends Event {
    private static final long serialVersionUID = 225178481951279734L;
    private Status status;

    /**
     * Wird Text übergeben, der den aktuellen Spielstatus beschreibt: Entweder
     * "Enthält Fehler.", "Nicht mehr lösbar.", "Gelöst!" oder "Noch nicht gelöst."
     *
     * @param status Text, der den aktuellen Spielstatus beschreibt
     */
    StatusEvent(Status status) {
        super(null);
        this.status = status;
    }

    Status getStatus() {
        return status;
    }

    public enum Status {
        ERROR("Error . . ."), SOLVED("Solved!"), UNSOLVABLE("Unsolvable . . ."), NOT_SOLVED("Not solved, yet.");

        private String text;

        Status(String text) {
            this.text = text;
        }

        public String getText() {
            return text;
        }
    }
}
