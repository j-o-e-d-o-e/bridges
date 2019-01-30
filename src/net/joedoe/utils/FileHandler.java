package net.joedoe.utils;

import net.joedoe.entities.Bridge;
import net.joedoe.entities.IBridge;
import net.joedoe.entities.IIsle;
import net.joedoe.entities.Isle;
import net.joedoe.views.Difficulty;

import java.io.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static net.joedoe.utils.GameInfo.*;

public class FileHandler {
    private static GameData gameData = GameData.getInstance();
    private static GameManager gameManager = GameManager.getInstance();
    private static String filepathPuzzle = new File("").getAbsolutePath() + File.separator + "data" + File.separator + "puzzle.bgs";
    private static String filepathUser = new File("").getAbsolutePath() + File.separator + "data" + File.separator + "player.usr";
    private static String filepathScores = new File("").getAbsolutePath() + File.separator + "data" + File.separator + "best.scr";


    public static void loadPuzzle() throws IOException {
        loadPuzzle(filepathPuzzle);
    }

    public static void loadPuzzle(String filepathPuzzle) throws IOException, IllegalArgumentException {
        StringBuilder sb = new StringBuilder();
        BufferedReader in = new BufferedReader(new FileReader(filepathPuzzle));
        String line = in.readLine();
        while (line != null) {
            if (!line.startsWith("#") && !line.isEmpty()) {
                // syntaktische Fehlerüberprüfung
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
        // Daten laden und semantische Fehlerüberprüfung
        // 'FIELD'-Sektion
        Pattern pattern = Pattern.compile("(\\d+)x(\\d+)\\|(\\d+)");
        Matcher matcher = pattern.matcher(input);
        if (matcher.find()) {
            int width = Integer.parseInt(matcher.group(1));
            int height = Integer.parseInt(matcher.group(2));
            int islesCount = Integer.parseInt(matcher.group(3));
            // Semantische Fehlerüberprüfung
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
            // Semantische Fehlerüberprüfung
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
            // Semantische Fehlerüberprüfung
            if (startIsle == null || endIsle == null) throw new IllegalArgumentException();
            IBridge bridge = new Bridge(startIsle.getPos(), endIsle.getPos(), doubleBridge);
            bridges.add(bridge);
        }
        gameData.setBridges(bridges);
    }

    private static boolean valid(String line) {
        return line.equals("FIELD") || line.equals("ISLANDS") || line.equals("BRIDGES")
                || line.matches("\\d+ x \\d+ \\| \\d+") || line.matches("\\( \\d+, \\d+ \\| \\d+ \\)")
                || line.matches("\\( \\d+, \\d+ \\| (true|false) \\)");
    }

    public static void savePuzzle() throws IOException {
        savePuzzle(filepathPuzzle);
    }

    public static void savePuzzle(String filepathPuzzle) throws IOException {
        BufferedWriter out = new BufferedWriter(new FileWriter(filepathPuzzle));
        StringBuilder sb = new StringBuilder();
        sb.append("#PUZZLE").append(System.lineSeparator());
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
        out.write(sb.toString());
        out.close();
    }

    public static void loadUser() throws IOException {
        BufferedReader in = new BufferedReader(new FileReader(filepathUser));
        String line = in.readLine();
        StringBuilder sb = new StringBuilder();
        while (line != null) {
            if (!line.startsWith("#") && !line.isEmpty()) sb.append(line);
            line = in.readLine();
        }
        Pattern pattern = Pattern.compile("Points: (\\d+)");
        Matcher matcher = pattern.matcher(sb);
        if (matcher.find()) gameManager.setPoints(Integer.parseInt(matcher.group(1)));
        pattern = Pattern.compile("Level: (\\d+)");
        matcher = pattern.matcher(sb);
        if (matcher.find()) gameManager.setLevel(Integer.parseInt(matcher.group(1)));
        in.close();
    }

    public static void saveUser() throws IOException {
        BufferedWriter out = new BufferedWriter(new FileWriter(filepathUser));
        String sb = "#USER" + System.lineSeparator() + "Points: " + gameManager.getAllPoints() +
                System.lineSeparator() + "Level: " + gameManager.getLevel();
        out.write(sb);
        out.close();
    }

    public static void loadScores() throws IOException {
        BufferedReader in = new BufferedReader(new FileReader(filepathScores));
        String line = in.readLine();
        while (line != null) {
            if (line.startsWith("#") || line.isEmpty()) {
                line = in.readLine();
                continue;
            }
            Pattern pattern = Pattern.compile("Level: (\\d+) (\\S+)");
            Matcher matcher = pattern.matcher(line);
            if (matcher.find()) {
                int points = Integer.parseInt(matcher.group(1));
                String name = matcher.group(2);
                PointEntry bestLevel = new PointEntry(points, name);
                gameData.setBestLevel(bestLevel);
            }
            for (Difficulty difficulty : Difficulty.values()) {
                pattern = Pattern.compile(difficulty.getName() + ": (\\d+):(\\d+) (\\S+)");
                matcher = pattern.matcher(line);
                if (matcher.find()) {
                    int mins = Integer.parseInt(matcher.group(1));
                    int secs = Integer.parseInt(matcher.group(2));
                    LocalTime time = LocalTime.of(0, mins, secs);
                    String name = matcher.group(3);
                    TimeEntry bestVeryEasy = new TimeEntry(time, name);
                    gameData.setBestTime(difficulty, bestVeryEasy);
                }
            }
            line = in.readLine();
        }
        in.close();
    }

    public static void saveScores() throws IOException {
        BufferedWriter out = new BufferedWriter(new FileWriter(filepathScores));
        StringBuilder sb = new StringBuilder("#SCORES").append(System.lineSeparator()).append("#Level").append(System.lineSeparator());

        PointEntry bestLevel = gameData.getBestLevel();
        sb.append("Level: ").append(bestLevel.getPoints()).append(" ").append(bestLevel.getName()).append(System.lineSeparator());
        sb.append("#Time").append(System.lineSeparator());
        for (Difficulty difficulty : Difficulty.values()) {
            TimeEntry entry = gameData.getBestTime(difficulty);
            LocalTime time = entry.getTime();
            sb.append(difficulty.getName()).append(": ").append(time.getMinute()).append(":").append(time.getSecond()).append(" ").append(entry.getName()).append(System.lineSeparator());
        }
        out.write(sb.toString());
        out.close();
    }

    public static boolean filesExist() {
        File user = new File(filepathUser);
        File puzzle = new File(filepathPuzzle);
        return user.exists() && puzzle.exists();
    }

    public static boolean deleteFiles() {
        File user = new File(filepathUser);
        File puzzle = new File(filepathPuzzle);
        return user.delete() && puzzle.delete();
    }
}