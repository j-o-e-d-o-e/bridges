package net.joedoe.utils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.joedoe.entities.Bridge;
import net.joedoe.entities.IBridge;
import net.joedoe.entities.IIsle;
import net.joedoe.entities.Isle;

import static net.joedoe.utils.GameInfo.*;

/**
 * L�dt bzw. speichert Daten in bzw. aus dem .bgs-Format.
 *
 */
public class FileHandler {
    private static GameData gameData = GameData.getInstance();

    /**
     * L�dt Spiel-Daten aus Datei.
     * 
     * @param filepath
     *            Dateipfad der Lade-Datei
     * @throws IOException
     *             falls Ausnahme beim Auslesen auftritt
     * @throws IllegalArgumentException
     *             falls Datei semantische oder syntaktische Fehler aufweist
     */
    public static void loadGame(String filepath) throws IOException, IllegalArgumentException {
        StringBuilder sb = new StringBuilder();
        BufferedReader in = new BufferedReader(new FileReader(filepath));
        String line = in.readLine();
        while (line != null) {
            if (!line.startsWith("#") && !line.isEmpty()) {
                // syntaktische Fehler�berpr�fung
                if (valid(line)) {
                    sb.append(line);
                } else {
                    in.close();
                    throw new IllegalArgumentException();
                }
            }
            line = in.readLine();
        }
        in.close();
        String input = sb.toString();
        input = input.replaceAll("\\s+", "");
        // Daten laden und semantische Fehler�berpr�fung
        loadData(input);
    }

    /**
     * Syntaktische Fehler�berpr�fung.
     * 
     * @param line
     *            zu �berpr�fender, eingelesene Zeile
     * @return true, falls keine syntaktischen Fehler enthalten sind
     */
    private static boolean valid(String line) {
        return line.equals("FIELD") || line.equals("ISLANDS") || line.equals("BRIDGES")
                || line.matches("\\d+ x \\d+ \\| \\d+") || line.matches("\\( \\d+, \\d+ \\| \\d+ \\)")
                || line.matches("\\( \\d+, \\d+ \\| (true|false) \\)");
    }

    /**
     * Semantische Fehler�berpr�fung und Laden der Spieldaten.
     * 
     * @param input
     *            ausgelesener String
     * @throws IllegalArgumentException
     *             falls semantische Fehler vorhanden sind
     */
    private static void loadData(String input) throws IllegalArgumentException {
        // 'FIELD'-Sektion
        Pattern pattern = Pattern.compile("(\\d+)x(\\d+)\\|(\\d+)");
        Matcher matcher = pattern.matcher(input);
        if (matcher.find()) {
            int width = Integer.parseInt(matcher.group(1));
            int height = Integer.parseInt(matcher.group(2));
            int islesCount = Integer.parseInt(matcher.group(3));
            // Semantische Fehler�berpr�fung
            if (width < MIN_WIDTH || width > MAX_WIDTH || height < MIN_HEIGHT || height > MAX_HEIGHT
                    || islesCount < MIN_ISLES_COUNT)
                throw new IllegalArgumentException();
            gameData.setWidth(width);
            gameData.setHeight(height);
            gameData.setIslesCount(islesCount);
        } else throw new IllegalArgumentException();

        // 'ISLANDS'-Sektion
        List<IIsle> isles = new ArrayList<>();
        pattern = Pattern.compile("\\((\\d+),(\\d+)\\|(\\d+)\\)");
        matcher = pattern.matcher(input);
        while (matcher.find()) {
            int x = Integer.parseInt(matcher.group(1));
            int y = Integer.parseInt(matcher.group(2));
            int islesCount = Integer.parseInt(matcher.group(3));
            // Semantische Fehler�berpr�fung
            if (x < 0 || x > gameData.getWidth() - 1 || y < 0 || y > gameData.getHeight() - 1 || islesCount < 0
                    || islesCount > 8)
                throw new IllegalArgumentException();
            IIsle isle = new Isle(new Coordinate(x, y), islesCount);
            isles.add(isle);
        }
        if (isles.size() < gameData.getIslesCount()) throw new IllegalArgumentException();
        gameData.setIsles(isles);

        // 'BRIDGES'-Sektion
        List<IBridge> bridges = new ArrayList<>();
        pattern = Pattern.compile("\\((\\d+),(\\d+)\\|(true|false)\\)");
        matcher = pattern.matcher(input);
        while (matcher.find()) {
            int startIndex = Integer.parseInt(matcher.group(1));
            int endIndex = Integer.parseInt(matcher.group(2));
            boolean doubleBridge = Boolean.parseBoolean(matcher.group(3));
            IIsle startIsle = isles.get(startIndex);
            IIsle endIsle = isles.get(endIndex);
            // Semantische Fehler�berpr�fung
            if (startIsle == null || endIsle == null) throw new IllegalArgumentException();
            IBridge bridge = new Bridge(startIsle.getPos(), endIsle.getPos(), doubleBridge);
            bridges.add(bridge);
        }
        gameData.setBridges(bridges);
    }

    /**
     * Speichert Spiel-Daten in Datei.
     * 
     * @param filepath
     *            Dateipfad zu der Speicher-Datei.
     * @throws IOException
     *             falls Ausnahme beim Einlesen auftritt
     */
    public static void saveGame(String filepath) throws IOException {
        BufferedWriter out = new BufferedWriter(new FileWriter(filepath));
        out.write(saveData());
        out.close();
    }

    /**
     * Erzeugt Inhalt mit den zu speichernden Daten.
     * 
     * @return Inhalt der zu speichernden Daten
     */
    private static String saveData() {
        StringBuilder sb = new StringBuilder();
        // 'FIELD'-Sektion einlesen
        sb.append("FIELD").append(System.lineSeparator());
        sb.append(gameData.getWidth()).append(" x ").append(gameData.getHeight()).append(" | ")
                .append(gameData.getIslesCount()).append(System.lineSeparator());
        sb.append(System.lineSeparator());
        // 'ISLANDS'-Sektion einlesen
        sb.append("ISLANDS").append(System.lineSeparator());
        for (IIsle isle : gameData.getIsles()) {
            sb.append("( ").append(isle.getX()).append(", ").append(isle.getY()).append(" | ").append(isle.getBridges())
                    .append(" )").append(System.lineSeparator());
        }
        sb.append(System.lineSeparator());
        // 'BRIDGES'-Sektion einlesen
        sb.append("BRIDGES");
        if (gameData.getBridgesData() != null) {
            sb.append(System.lineSeparator());
            for (Object[] data : gameData.getBridgesData()) {
                sb.append("( ").append(data[0]).append(", ").append(data[1]).append(" | ").append(data[2]).append(" )")
                        .append(System.lineSeparator());
            }
        }
        return sb.toString();
    }
}