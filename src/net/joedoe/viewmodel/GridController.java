package net.joedoe.viewmodel;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import net.joedoe.entities.IBridge;
import net.joedoe.entities.IIsle;
import net.joedoe.utils.Coordinate;
import net.joedoe.views.IsleListener;

/**
 * Verwaltet die aktuellen Brücken-Linien und Inseln im View.
 *
 */
public class GridController {
    private List<IslePane> panes = new ArrayList<>();
    private List<BridgeLine> lines = new ArrayList<>();
    private IsleListener listener;

    /**
     * Wird Listener übergeben, um {@link net.joedoe.views.Grid} über angeklickte
     * Insel zu benachrichtigen.
     * 
     * @param listener
     *            beobachtet Inseln auf Klicks
     */
    public GridController(IsleListener listener) {
        this.listener = listener;
    }

    /**
     * Fügt eine neue Brücken-Linie hinzu, falls möglich. Falls bereits zwei Brücken
     * mit den beiden Koordinaten existieren, wird die Methode beendet. Falls eine
     * Brücke existiert, wird eine zweite hinzugefügt und beide versetzt platziert.
     * Falls keine Brücke existiert, die die beiden Koordinaten miteinander
     * verbindet, wird eine neue, zentrierte Brücke hinzugefügt.
     * 
     * @param bridge
     *            hinzuzufügende Brücke (Modell)
     */
    public BridgeLine addLine(IBridge bridge) {
        updateLines();
        // suche existierende Linien mit diesen Koordinaten
        List<BridgeLine> lines = getLines(bridge.getStart(), bridge.getEnd());
        BridgeLine line;
        // neue Linie
        if (lines.size() == 0) {
            line = new BridgeLine(bridge.getStart(), bridge.getEnd(), false);
            this.lines.add(line);
            return line;
        } // existierende Linie wird versetzt und neue Linie ebenfalls versetzt
        else if (lines.size() == 1) {
            line = lines.get(0);
            line.setOffset(true);
            line = new BridgeLine(line.getEnd(), line.getStart(), true);
            this.lines.add(line);
            return line;
        } else {
            return null;
        }
    }

    /**
     * Entfernt Brücken-Linie, falls möglich. Falls zwei Brücken mit den beiden
     * Koordinaten existieren, wird die erste entfernt und die zweite zentriert
     * dargestellt. Falls eine Brücke existiert, wird diese entfernt. Falls keine
     * Brücke existiert, die die beiden Koordinaten miteinander verbindet, wird die
     * Methode beendet.
     * 
     * @param bridge
     *            zu entferndende Brücke (Modell)
     */
    public BridgeLine removeLine(IBridge bridge) {
        updateLines();
        List<BridgeLine> lines = getLines(bridge.getStart(), bridge.getEnd());
        BridgeLine line;
        // falls keine entsprechende Linie existiert
        if (lines.size() == 0) {
            return null;
        } // falls eine Linie existiert, diese entfernen
        else if (lines.size() == 1) {
            line = lines.get(0);
            this.lines.remove(line);
            return line;
        } // falls zwei Linie existieren
        else {
            line = lines.get(0);
            // erste Linie zentrieren
            line.setOffset(false);
            // zweite Linie entfernen
            line = lines.get(1);
            this.lines.remove(line);
            return line;
        }
    }

    /**
     * Gibt alle Brücken-Linien zurück, die die beiden Koordinaten enthalten.
     * 
     * @param start
     *            erste Koordinate
     * @param end
     *            zweite Koordinate
     * @return Liste mit Brücken-Linien (View), die die beiden Koordinaten
     *         miteinander verbinden
     */
    private List<BridgeLine> getLines(Coordinate start, Coordinate end) {
        return lines.stream().filter(bridge -> bridge.contains(start, end)).collect(Collectors.toList());
    }

    /**
     * Setzt die Farbe der zuletzt hinzugefügten Brücken-Linie auf die
     * Standard-Farbe, bevor eine neue Brücken-Linie hinzugefügt wird.
     */
    public void updateLines() {
        if (lines.size() > 0) lines.get(lines.size() - 1).setStdColor();
    }

    /**
     * Lädt Brücken-Linien.
     * 
     * @param bridges
     *            Liste mit zu ladenen Brücken
     */
    public void setLines(List<IBridge> bridges) {
        for (IBridge bridge : bridges) {
            BridgeLine line = new BridgeLine(bridge.getStart(), bridge.getEnd(), false);
            line.setStdColor();
            lines.add(line);
            if (bridge.isDoubleBridge()) {
                line.setOffset(true);
                line = new BridgeLine(bridge.getEnd(), bridge.getStart(), true);
                line.setStdColor();
                lines.add(line);
            }
        }
    }

    /**
     * Gibt aktuelle Brücken-Linien zurück.
     * 
     * @return Liste mit aktuellen Brücken-Linien.
     */
    public List<BridgeLine> getLines() {
        return lines;
    }

    /**
     * Lädt Inseln im View.
     * 
     * @param isles
     *            Liste mit zu ladenen Inseln
     */
    public void setPanes(List<IIsle> isles) {
        for (IIsle i : isles) {
            IslePane isle = new IslePane(i.getPos(), i.getBridges());
            isle.setOnMouseClicked(e -> listener.handle(e));
            this.panes.add(isle);
        }
    }

    /**
     * Gibt Inseln zurück.
     * 
     * @return Liste mit aktuellen Inseln
     */
    public List<IslePane> getPanes() {
        return panes;
    }

    /**
     * Setzt den Spielstatus zurück. Brücken werden entfernt.
     */
    public void reset() {
        lines.clear();
    }
}
