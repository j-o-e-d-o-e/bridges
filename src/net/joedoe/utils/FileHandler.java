package net.joedoe.utils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static net.joedoe.utils.GameInfo.*;

public class FileHandler {
    private static GameData gameData = GameData.getInstance();

    public static void loadGame(String filepath) throws IOException, IllegalArgumentException {
        StringBuilder sb = new StringBuilder();
        BufferedReader in = new BufferedReader(new FileReader(filepath));
        String line = in.readLine();
        while (line != null) {
            if (!line.startsWith("#") && !line.isEmpty()) {
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
        loadData(input);
    }

    private static boolean valid(String line) {
        return line.equals("FIELD") || line.equals("ISLANDS") || line.equals("BRIDGES")
                || line.matches("\\d+ x \\d+ \\| \\d+") || line.matches("\\( \\d+, \\d+ \\| \\d+ \\)")
                || line.matches("\\( \\d+, \\d+ \\| (true|false) \\)");
    }

    private static void loadData(String input) throws IllegalArgumentException {
        Pattern pattern = Pattern.compile("(\\d+)x(\\d+)\\|(\\d+)");
        Matcher matcher = pattern.matcher(input);
        if (matcher.find()) {
            int width = Integer.parseInt(matcher.group(1));
            int height = Integer.parseInt(matcher.group(2));
            int islesCount = Integer.parseInt(matcher.group(3));
            if (width < MIN_WIDTH || width > MAX_WIDTH || height < MIN_HEIGHT || height > MAX_HEIGHT
                    || islesCount < MIN_ISLES_COUNT)
                throw new IllegalArgumentException();
            gameData.setWidth(width);
            gameData.setHeight(height);
            gameData.setIslesCount(islesCount);
        } else throw new IllegalArgumentException();

        List<Object[]> isles = new ArrayList<>();
        pattern = Pattern.compile("\\((\\d+),(\\d+)\\|(\\d+)\\)");
        matcher = pattern.matcher(input);
        while (matcher.find()) {
            int x = Integer.parseInt(matcher.group(1));
            int y = Integer.parseInt(matcher.group(2));
            int islesCount = Integer.parseInt(matcher.group(3));
            if (x < 0 || x > gameData.getWidth() - 1 || y < 0 || y > gameData.getHeight() - 1 || islesCount < 0
                    || islesCount > 8)
                throw new IllegalArgumentException();
            Object[] isle = new Object[2];
            isle[0] = new Coordinate(x, y);
            isle[1] = islesCount;
            isles.add(isle);
        }
        if (isles.size() < gameData.getIslesCount()) throw new IllegalArgumentException();
        gameData.setIsles(isles.toArray(new Object[isles.size()][2]));

        List<Object[]> bridges = new ArrayList<>();
        pattern = Pattern.compile("\\((\\d+),(\\d+)\\|(true|false)\\)");
        matcher = pattern.matcher(input);
        while (matcher.find()) {
            int startIndex = Integer.parseInt(matcher.group(1));
            int endIndex = Integer.parseInt(matcher.group(2));
            boolean doubleBridge = Boolean.parseBoolean(matcher.group(3));
            Object startIsle = isles.get(startIndex)[0];
            Object endIsle = isles.get(endIndex)[0];
            if (startIsle == null || endIsle == null) throw new IllegalArgumentException();
            Object[] bridge = new Object[3];
            bridge[0] = startIsle;
            bridge[1] = endIsle;
            bridge[2] = doubleBridge;
            bridges.add(bridge);
        }
        gameData.setBridges(bridges.toArray(new Object[bridges.size()][3]));
    }

    public static void saveGame(String filepath) throws IOException {
        BufferedWriter out = new BufferedWriter(new FileWriter(filepath));
        out.write(saveData());
        out.close();
    }

    private static String saveData() {
        StringBuilder sb = new StringBuilder();
        sb.append("FIELD").append(System.lineSeparator());
        sb.append(gameData.getWidth()).append(" x ").append(gameData.getHeight()).append(" | ")
                .append(gameData.getIslesCount()).append(System.lineSeparator());
        sb.append(System.lineSeparator());
        sb.append("ISLANDS").append(System.lineSeparator());
        for (Object[] data : gameData.getIsles()) {
            sb.append("( ").append(((Coordinate) data[0]).getX()).append(", ").append(((Coordinate) data[0]).getY())
                    .append(" | ").append(data[1]).append(" )").append(System.lineSeparator());
        }
        sb.append(System.lineSeparator());
        sb.append("BRIDGES");
        if (gameData.getBridges() != null) {
            sb.append(System.lineSeparator());
            for (Object[] data : gameData.getBridges()) {
                sb.append("( ").append(data[0]).append(", ").append(data[1]).append(" | ").append(data[2]).append(" )")
                        .append(System.lineSeparator());
            }
        }
        return sb.toString();
    }
}
