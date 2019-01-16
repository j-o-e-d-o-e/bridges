package test.net.joedoe.logics;

import net.joedoe.logics.BridgeController;
import net.joedoe.utils.FileHandler;
import net.joedoe.utils.GameData;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileHandlerTesting {
    private GameData gameData = GameData.getInstance();
    protected BridgeController controller = new BridgeController();
    private String load = "." + File.separator + "src" + File.separator + "test" + File.separator + "data" + File.separator + "puzzle"
            + File.separator + "in_";
    private String res = "." + File.separator + "src" + File.separator + "test" + File.separator + "data" + File.separator + "temp"
            + File.separator + "out_";
    private String sol = "." + File.separator + "src" + File.separator + "test" + File.separator + "data" + File.separator + "sol"
            + File.separator + "sol_";

    void loadData(String filename) {
        String filepath = load + filename;
        try {
            FileHandler.loadPuzzle(filepath);
        } catch (IOException | IllegalArgumentException e) {
            e.printStackTrace();
        }
        controller.setIsles(gameData.getIsles());
        controller.setBridges(gameData.getBridges());
    }

    void saveData(String filename) {
        String filepath = res + filename;
        try {
            if (Files.exists(Paths.get(filepath))) Files.delete(Paths.get(filepath));
            FileHandler.savePuzzle(filepath);
        } catch (IOException | IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    String convertResultToString(String filename) {
        return convertFileToString(res + filename);
    }

    String convertSolutionToString(String filename) {
        return convertFileToString(sol + filename);
    }

    private String convertFileToString(String filepath) {
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader in = new BufferedReader(new FileReader(filepath));
            String line = in.readLine();
            while (line != null) {
                if (!line.startsWith("#")) {
                    sb.append(line);
                    sb.append("\r\n");
                }
                line = in.readLine();
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String result = sb.toString();
        result = result.replaceAll("\\s+", "");
        return result;
    }
}
