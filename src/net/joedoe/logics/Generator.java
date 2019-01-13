package net.joedoe.logics;

import net.joedoe.entities.Bridge;
import net.joedoe.entities.IBridge;
import net.joedoe.entities.IIsle;
import net.joedoe.entities.Isle;
import net.joedoe.utils.Alignment;
import net.joedoe.utils.Coordinate;
import net.joedoe.utils.Direction;
import net.joedoe.utils.GameData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static net.joedoe.utils.GameInfo.*;

/**
 * Generiert ein neues Spiel.
 */
public class Generator {
    private GameData gameData = GameData.getInstance();
    private BridgeDetector detector;
    private int height, width, islesCount;
    private List<Isle> isles = new ArrayList<>();
    private List<Bridge> bridges = new ArrayList<>();
    private List<Integer> indices = new ArrayList<>();
    private Random random = new Random();

    private final static Logger LOGGER = Logger.getLogger(Generator.class.getName());

    public Generator() {
        detector = new BridgeDetector(bridges);
        LOGGER.setLevel(Level.OFF);
    }

    /**
     * Generiert ein neues Spiel.
     */
    public void generateGame() {
        // erstellt Liste aus allen verfügbaren Koordinaten als Ganzzahlen
        indices = IntStream.range(0, height * width).boxed().collect(Collectors.toList());
        // legt Anfangs-Insel fest
        int index = random.nextInt(indices.size());
        Coordinate start = new Coordinate(index % width, index / width);
        Isle initialIsle = createIsle(start);
        isles.add(initialIsle);
        LOGGER.info("Height: " + height + " Width: " + width + " Isles: " + islesCount + "\nRandom Index: " + index
                + " Initial Isle: " + initialIsle.toString() + "\nIndices Size: " + indices.size() + "\n");
        while (islesCount - isles.size() > 0) {
            Collections.shuffle(isles);
            for (Isle startIsle : isles) {
                // Inseln mit maximaler Brücken-Anzahl nicht berücksichtigen
                if (startIsle.getBridges() == 8) continue;
                // neue Insel ermitteln
                Isle endIsle = getEndIsle(startIsle);
                if (endIsle != null) {
                    // neue Insel und Brücke hinzufügen
                    isles.add(endIsle);
                    bridges.add(createBridge(endIsle, startIsle, random.nextBoolean()));
                    break;
                }
                // startet Spiel-Generierung neu
                if (startIsle == isles.get(isles.size() - 1)) {
                    isles.clear();
                    bridges.clear();
                    generateGame();
                    return;
                }
            }
        }
        gameData.setWidth(width);
        gameData.setHeight(height);
        gameData.setIsles(getIsles());
        gameData.setBridges(getBridges());
    }

    /**
     * Gibt Insel zurück, zu der eine Brücke gebaut werden kann.
     *
     * @param startIsle Insel, von der ausgehend eine Brücke gebaut werden soll
     * @return zweite Insel, die eine neue Brücke mit der ersten Insel 'startIsle'
     * verbindet
     */
    private Isle getEndIsle(Isle startIsle) {
        for (Direction direction : getDirections(startIsle))
            for (int distance : getDistances(startIsle, direction)) {
                // Berechnung der Koordinate der neuen Insel
                int y = 0;
                int x = 0;
                if (direction == Direction.UP) {
                    y = startIsle.getY() - distance;
                    x = startIsle.getX();
                }
                if (direction == Direction.LEFT) {
                    y = startIsle.getY();
                    x = startIsle.getX() - distance;
                }
                if (direction == Direction.DOWN) {
                    y = startIsle.getY() + distance;
                    x = startIsle.getX();
                }
                if (direction == Direction.RIGHT) {
                    y = startIsle.getY();
                    x = startIsle.getX() + distance;
                }
                Coordinate end = new Coordinate(x, y);
                // Kollisionsdetektion für neue Insel und neue Brücke
                if (indices.contains(end.getY() * width + end.getX()) && !collides(startIsle.getPos(), end)) {
                    Isle endIsle = createIsle(end);
                    LOGGER.info("Start Isle: " + startIsle.toString() + " Direction: " + direction.toString()
                            + " Distance: " + distance + "\nEnd Isle: " + endIsle.toString() + "\nMissing Isles: "
                            + islesCount + " Indices Size: " + indices.size() + "\n");
                    return endIsle;
                }
            }
        return null;
    }

    /**
     * Gibt mögliche Richtungen ausgehend von einer Insel zurück.
     *
     * @param startIsle Insel, von der ausgehend die möglichen Richtungen gesucht werden
     * @return Liste mit möglichen Richtungen
     */
    public List<Direction> getDirections(Isle startIsle) {
        List<Direction> directions = new ArrayList<>();
        if (startIsle.getY() > 1) directions.add(Direction.UP);
        if (startIsle.getX() > 1) directions.add(Direction.LEFT);
        if (startIsle.getY() < height - 2) directions.add(Direction.DOWN);
        if (startIsle.getX() < width - 2) directions.add(Direction.RIGHT);
        Collections.shuffle(directions);
        return directions;
    }

    /**
     * Gibt mögliche Distanzen ausgehend von einer Insel und einer Richtung zurück.
     *
     * @param startIsle Insel, von der ausgehend die möglichen Distanzen gesucht werden
     * @param direction Richtung, in der die möglichen Distanzen gesucht werden
     * @return Liste mit möglichen Distanzen
     */
    public List<Integer> getDistances(Isle startIsle, Direction direction) {
        int max = 0;
        if (direction == Direction.UP) max = startIsle.getY();
        if (direction == Direction.LEFT) max = startIsle.getX();
        if (direction == Direction.DOWN) max = height - startIsle.getY() - 1;
        if (direction == Direction.RIGHT) max = width - startIsle.getX() - 1;
        List<Integer> distances = IntStream.range(2, max + 1).boxed().collect(Collectors.toList());
        Collections.shuffle(distances);
        return distances;
    }

    /**
     * Checkt, ob eine neue Brücke mit einer bereits existierenden Insel oder Brücke
     * kollidiert.
     *
     * @param start erste Koordinate einer neuen Brücke
     * @param end   zweite Koordinate einer neuen Brücke
     * @return true, falls neue Brücke kollidiert
     */
    private boolean collides(Coordinate start, Coordinate end) {
        // falls 'start' weiter vom Koordinaten-Ursprung entfernt ist als 'end'
        if (start.compareTo(end) > 0) {
            return collidesIsles(end, start) || detector.collides(end, start);
        }
        return collidesIsles(start, end) || detector.collides(start, end);
    }

    /**
     * Checkt, ob neue Brücke mit einer existierenden Insel kollidiert.
     *
     * @param start erste Koordinate einer neuen Brücke
     * @param end   zweite Koordinate einer neuen Brücke
     * @return true, falls neue Brücke kollidiert
     */
    private boolean collidesIsles(Coordinate start, Coordinate end) {
        return isles.stream().anyMatch(i -> i.getY() > start.getY() && i.getY() < end.getY() && i.getX() == start.getX()
                || i.getX() > start.getX() && i.getX() < end.getX() && i.getY() == start.getY());
    }

    /**
     * Erstellt neue Insel.
     *
     * @param pos Koordinate, an der die neue Insel platziert wird
     * @return neue Insel
     */
    private Isle createIsle(Coordinate pos) {
        Isle isle = new Isle(pos, 0);
        // Insel-Koordinate in Ganzzahl umwandeln
        int index = pos.getY() * width + pos.getX();
        // unzulässige Koordinaten laut Nachbarschaftsregel entfernen
        indices.remove(new Integer(index - width));
        indices.remove(new Integer(index - 1));
        indices.remove(new Integer(index));
        indices.remove(new Integer(index + 1));
        indices.remove(new Integer(index + width));
        return isle;
    }

    /**
     * Erstellt neue Brücke.
     *
     * @param startIsle    erste Insel der neuen Brücke
     * @param endIsle      zweite Insel der neuen BRücke
     * @param doubleBridge true, falls doppelte Brücke - ansonsten false
     * @return neue Brücke
     */
    private Bridge createBridge(Isle startIsle, Isle endIsle, boolean doubleBridge) {
        Bridge bridge = new Bridge(startIsle.getPos(), endIsle.getPos(), doubleBridge);
        startIsle.increaseBridges(doubleBridge);
        endIsle.increaseBridges(doubleBridge);
        // Koordinaten in Ganzzahlen umwandeln
        int startIndex = bridge.getStartY() * width + bridge.getStartX();
        int endIndex = bridge.getEndY() * width + bridge.getEndX();
        // unzulässige Koordinaten aus Indizes-Liste entfernen
        if (bridge.getAlignment() == Alignment.HORIZONTAL)
            IntStream.range(startIndex, endIndex).boxed().forEach(i -> indices.remove(i));
        else IntStream.range(startIndex, endIndex).filter(i -> i % width == bridge.getStartX()).boxed()
                .forEach(i -> indices.remove(i));
        return bridge;
    }

    /**
     * Legt die Breite, Höhe und Insel-Anzahl zufällig fest.
     */
    public void setData() {
        width = random.nextInt((MAX_WIDTH - MIN_WIDTH) + 1) + MIN_WIDTH;
        height = random.nextInt((MAX_HEIGHT - MIN_HEIGHT) + 1) + MIN_HEIGHT;
        int maxIsles = (int) (0.2 * width * height);
        islesCount = random.nextInt((maxIsles - MIN_WIDTH) + 1) + MIN_WIDTH;
    }

    /**
     * Legt die Breite und Höhe laut Nutzer-Eingabe fest und Insel-Anzahl zufällig.
     */
    public void setData(int width, int height) {
        if (width < MIN_WIDTH || width > MAX_WIDTH) throw new IllegalArgumentException();
        this.width = width;
        if (height < MIN_HEIGHT || height > MAX_HEIGHT) throw new IllegalArgumentException();
        this.height = height;
        if (width == 4 && height == 4) islesCount = 4;
        else {
            int minIsles = Math.min(width, height);
            int maxIsles = (int) (width * height * 0.2);
            islesCount = random.nextInt((maxIsles - minIsles) + 1) + minIsles;
        }
    }

    /**
     * Legt die Breite, Höhe und Insel-Anzahl laut Nutzer-Eingabe fest.
     */
    public void setData(int width, int height, int islesCount) {
        if (width < MIN_WIDTH || width > MAX_WIDTH) {
            throw new IllegalArgumentException();
        }
        this.width = width;
        if (height < MIN_HEIGHT || height > MAX_HEIGHT) {
            throw new IllegalArgumentException();
        }
        this.height = height;
        if (islesCount < MIN_ISLES_COUNT || islesCount > width * height * 0.2) {
            throw new IllegalArgumentException();
        }
        this.islesCount = islesCount;
    }

    public void setData(int islesCount) {
        this.islesCount = islesCount;
        int min = (int) Math.sqrt(5 * islesCount);
        width = min;
        height = min;
    }

    /**
     * Gibt die Breite des Spielfelds zurück.
     *
     * @return Breite des Spielfelds
     */
    public int getWidth() {
        return width;
    }

    /**
     * Gibt die Höhe des Spielfelds zurück.
     *
     * @return Höhe des Spielfelds
     */
    public int getHeight() {
        return height;
    }

    /**
     * Gibt die Anzahl der zu platzierenden Inseln zurück.
     *
     * @return Insel-Anzahl
     */
    public int getIsleCount() {
        return islesCount;
    }

    /**
     * Gibt generierte Inseln zurück.
     *
     * @return Liste von Inseln
     */
    public List<IIsle> getIsles() {
        Collections.sort(isles);
        return new ArrayList<>(isles);
    }

    /**
     * Gibt generierte Brücken zurück (als Musterlösung).
     *
     * @return Liste von Brücken
     */
    public List<IBridge> getBridges() {
        return new ArrayList<>(bridges);
    }

    /**
     * Legt die Liste mit (noch) verfügbaren Koordinaten als Ganzzahlen fest, wo
     * neue Inseln und Brücken platziert werden können. Zum Testen einzelner
     * Methoden.
     *
     * @param indices Liste mit Ganzzahlen, die die Koordinaten repräsentieren
     */
    public void setIndices(List<Integer> indices) {
        this.indices = indices;
    }
}
