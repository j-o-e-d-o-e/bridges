package net.joedoe.utils;

import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;

public class FileHandler  {
    private Stage window;
    private FileChooser fileChooser;

    public FileHandler(Stage window){
        this.window = window;
        fileChooser = new FileChooser();
    }

    public void loadGame() {
        fileChooser.setTitle("Datei öffnen");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Bridges (*.bgs)", "*.bgs");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showOpenDialog(window);
        if (file != null) {
            BufferedReader reader;
            try {
                reader = new BufferedReader(new FileReader(file.toString()));

                reader.close();
            } catch (IOException e) {
                e.getMessage();
                e.printStackTrace();
            }
        }
    }

    public void saveGame() {
        fileChooser.setTitle("Datei speichern");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Bridges (*.bgs)", "*.bgs");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showSaveDialog(window);
        if (file != null) {
            BufferedWriter writer;
            try {
                writer = new BufferedWriter(new FileWriter(file.toString()));

                writer.close();
            } catch (IOException e) {
                e.getMessage();
                e.printStackTrace();
            }
        }
    }
}
