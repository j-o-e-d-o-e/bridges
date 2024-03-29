package net.joedoe.views.board;

import net.joedoe.entities.IBridge;
import net.joedoe.entities.IIsle;
import net.joedoe.utils.Coordinate;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Verwaltet die aktuellen Br�cken-Linien und Inseln im View.
 */
public class GridController {
    private List<IslePane> panes = new ArrayList<>();
    private List<BridgeLine> lines = new ArrayList<>();

    /**
     * F�gt eine neue Br�cken-Linie hinzu, falls m�glich. Falls bereits zwei Br�cken
     * mit den beiden Koordinaten existieren, wird die Methode beendet. Falls eine
     * Br�cke existiert, wird eine zweite hinzugef�gt und beide versetzt platziert.
     * Falls keine Br�cke existiert, die die beiden Koordinaten miteinander
     * verbindet, wird eine neue, zentrierte Br�cke hinzugef�gt.
     *
     * @param bridge hinzuzuf�gende Br�cke (Modell)
     */
    BridgeLine addLine(IBridge bridge) {
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

    List<BridgeLine> addLines(IBridge bridge) {
        List<BridgeLine> lines = new ArrayList<>();
        BridgeLine line = new BridgeLine(bridge.getStart(), bridge.getEnd(), true);
        lines.add(line);
        line = new BridgeLine(bridge.getEnd(), bridge.getStart(), true);
        lines.add(line);
        this.lines.addAll(lines);
        return lines;
    }

    /**
     * Entfernt Br�cken-Linie, falls m�glich. Falls zwei Br�cken mit den beiden
     * Koordinaten existieren, wird die erste entfernt und die zweite zentriert
     * dargestellt. Falls eine Br�cke existiert, wird diese entfernt. Falls keine
     * Br�cke existiert, die die beiden Koordinaten miteinander verbindet, wird die
     * Methode beendet.
     *
     * @param bridge zu entferndende Br�cke (Modell)
     */
    BridgeLine removeLine(IBridge bridge) {
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

    List<BridgeLine> removeLines(IBridge bridge) {
        updateLines();
        List<BridgeLine> lines = getLines(bridge.getStart(), bridge.getEnd());
        this.lines.removeAll(lines);
        return lines;
    }

    /**
     * Gibt alle Br�cken-Linien zur�ck, die die beiden Koordinaten enthalten.
     *
     * @param start erste Koordinate
     * @param end   zweite Koordinate
     * @return Liste mit Br�cken-Linien (View), die die beiden Koordinaten
     * miteinander verbinden
     */
    private List<BridgeLine> getLines(Coordinate start, Coordinate end) {
        return lines.stream().filter(bridge -> bridge.contains(start, end)).collect(Collectors.toList());
    }

    /**
     * Setzt die Farbe der zuletzt hinzugef�gten Br�cken-Linie auf die
     * Standard-Farbe, bevor eine neue Br�cken-Linie hinzugef�gt wird.
     */
    void updateLines() {
        if (lines.size() > 0) lines.get(lines.size() - 1).setStdColor();
    }

    /**
     * L�dt Br�cken-Linien.
     *
     * @param bridges Liste mit zu ladenen Br�cken
     */
    void setLines(List<IBridge> bridges) {
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
        BridgeLine line;
        try {
            line = lines.get(lines.size() - 1);
        } catch (ArrayIndexOutOfBoundsException e) {
            return;
        }
        line.getLine().getStyleClass().clear();
//        line.getLine().getStyleClass().add("bridge-first");
    }

    /**
     * Gibt aktuelle Br�cken-Linien zur�ck.
     *
     * @return Liste mit aktuellen Br�cken-Linien.
     */
    List<BridgeLine> getLines() {
        return lines;
    }

    /**
     * L�dt Inseln im View.
     *
     * @param isles Liste mit zu ladenen Inseln
     */
    void setPanes(List<IIsle> isles) {
        for (IIsle i : isles) {
            IslePane isle = new IslePane(i.getPos(), i.getBridges());
            panes.add(isle);
        }
    }

    /**
     * Gibt Inseln zur�ck.
     *
     * @return Liste mit aktuellen Inseln
     */
    List<IslePane> getPanes() {
        return panes;
    }

    void reset() {
        lines.clear();
    }

    void clear() {
        lines.clear();
        panes.clear();
    }
}
