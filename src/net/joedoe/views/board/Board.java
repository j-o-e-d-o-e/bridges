package net.joedoe.views.board;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Font;
import javafx.util.Duration;
import net.joedoe.entities.IBridge;
import net.joedoe.entities.IIsle;
import net.joedoe.utils.GameData;
import net.joedoe.utils.GameManager;
import net.joedoe.utils.Zoom;

import java.io.File;
import java.util.List;
import java.util.Optional;

import static javafx.scene.control.Alert.AlertType.INFORMATION;
import static net.joedoe.utils.GameInfo.CONTAINER_OFFSET;

public abstract class Board extends BorderPane {
    private GameData gameData = GameData.getInstance();
    private int width, height;
    private CheckBox checkBox;
    private boolean soundOn;
    private StackPane innerPane;
    EventHandler<Event> next;
    GameManager gameManager = GameManager.getInstance();
    Grid grid;
    MediaPlayer player;
    ImageView view;
    Label info;
    HBox controls;
    Label status = new Label();

    public Board() {
        getStylesheets().add("file:assets/css/board.css");
        setSound();
        player.stop();
    }

    private void setSound() {
        String soundUrl = "assets" + File.separator + "sounds" + File.separator + "waves.wav";
        Media sound = new Media(new File(soundUrl).toURI().toString());
        player = new MediaPlayer(sound);
        player.setOnEndOfMedia(() -> player.seek(Duration.ZERO));
        soundOn = true;
        player.play();
    }

    void setLayout() {
        BorderPane outerPane = new BorderPane();
        outerPane.setTop(createTop());
        innerPane = new StackPane();
        innerPane.setPadding(new Insets(CONTAINER_OFFSET, CONTAINER_OFFSET, 0, CONTAINER_OFFSET));
        outerPane.setCenter(innerPane);
        outerPane.setBottom(createBottom());
        setCenter(outerPane);
    }

    private HBox createTop() {
        HBox resetBox = new HBox(CONTAINER_OFFSET);
        resetBox.setMinWidth(200);
        Button reset = new Button("\u21BA");
        reset.setAlignment(Pos.CENTER_LEFT);
        reset.setOnAction(e -> reset());
        resetBox.getChildren().add(reset);

        HBox infoBox = new HBox(CONTAINER_OFFSET);
        infoBox.setAlignment(Pos.CENTER);
        infoBox.setMinWidth(200);
        view = new ImageView();
        view.setPreserveRatio(true);
        view.setFitHeight(15);
        view.setImage(new Image("file:assets" + File.separator + "images" + File.separator + "coin.png"));
        info = new Label(Integer.toString(gameManager.getTempPoints()));
        info.setFont(new Font(14));
        infoBox.getChildren().addAll(view, info);

        HBox controlsBox = new HBox(CONTAINER_OFFSET);
        controlsBox.setAlignment(Pos.CENTER_RIGHT);
        controlsBox.setMinWidth(200);
        Button zoomIn = new Button("\uD83D\uDD0D+");
        zoomIn.setOnAction(e -> {
            Zoom.in();
            grid.zoomInOut(width, height, checkBox.isSelected());
        });
        Button zoomOut = new Button("\uD83D\uDD0D-");
        zoomOut.setOnAction(e -> {
            Zoom.out();
            grid.zoomInOut(width, height, checkBox.isSelected());
        });
        Button sound = new Button("\uD83D\uDD0A");
        sound.setMinWidth(30);
        sound.setOnAction(e -> {
            soundOn = !soundOn;
            if (soundOn) {
                sound.setText("\uD83D\uDD0A");
                player.play();
            } else {
                sound.setText("\uD83D\uDD07");
                player.pause();
            }
        });
        controlsBox.getChildren().addAll(zoomIn, zoomOut, sound);

        Region regionLeft = new Region();
        HBox.setHgrow(regionLeft, Priority.ALWAYS);
        Region regionRight = new Region();
        HBox.setHgrow(regionRight, Priority.ALWAYS);

        HBox box = new HBox();
        box.setPadding(new Insets(CONTAINER_OFFSET, CONTAINER_OFFSET, 0, CONTAINER_OFFSET));
        box.getChildren().addAll(resetBox, regionLeft, infoBox, regionRight, controlsBox);
        return box;
    }

    private VBox createBottom() {
        VBox vBox = new VBox(CONTAINER_OFFSET);
        vBox.setPadding(new Insets(CONTAINER_OFFSET, CONTAINER_OFFSET, CONTAINER_OFFSET, CONTAINER_OFFSET));
        checkBox = new CheckBox("Show missing bridges");
        checkBox.setSelected(true);
        checkBox.setOnAction(e -> grid.setShowMissingBridges(checkBox.isSelected()));
        controls = new HBox(CONTAINER_OFFSET);
        controls.setAlignment(Pos.CENTER);
        controls.setPrefWidth(100);
        Button solveBtn = new Button("_Solve auto");
        solveBtn.setMnemonicParsing(true);
        solveBtn.setMinWidth(controls.getPrefWidth());
        solveBtn.setOnAction(e -> {
            if (grid.autoSolverIsRunning()) grid.stopAutoSolve();
            else grid.startAutoSolve();
        });
        Button nextBtn = new Button("_Next bridge");
        nextBtn.setMnemonicParsing(true);
        nextBtn.setMinWidth(controls.getPrefWidth());
        nextBtn.setOnAction(e -> grid.getNextBridge());
        controls.getChildren().addAll(solveBtn, nextBtn);
        vBox.getChildren().addAll(checkBox, controls, status);
        return vBox;
    }

    private void setGrid(int width, int height, List<IIsle> isles, List<IBridge> bridges) {
        this.width = width;
        this.height = height;
        innerPane.getChildren().remove(grid);
        Zoom.zoom(isles.size());
        if (grid != null) grid.shutdownAutoSolve();
        grid = new Grid(this::handleStatus, width, height, isles, bridges);
        grid.setPointListener(this::handlePoints);
        ScrollPane scroll = new ScrollPane();
        scroll.setContent(grid);
        scroll.setFitToHeight(true);
        scroll.setFitToWidth(true);
        innerPane.getChildren().add(scroll);
        grid.setShowMissingBridges(checkBox.isSelected());
    }

    public void setGrid() {
        setGrid(gameData.getWidth(), gameData.getHeight(), gameData.getIsles(), null);
    }

    public void setGridWithBridges() {
        setGrid(gameData.getWidth(), gameData.getHeight(), gameData.getIsles(), gameData.getBridges());
    }

    void handlePoints(PointEvent e) {
        gameManager.addPoints(e.getPoints());
        info.setText(Integer.toString(gameManager.getAllPoints()));
    }

    abstract void setLayout(EventHandler<Event> listener);

    abstract void handleStatus(StatusEvent e);

    abstract void reset();

    public void savePuzzle() {
        grid.savePuzzle();
    }

    public void restartSound() {
        if (soundOn) player.play();
    }

    void showAlert(String text) {
        Alert alert = new Alert(INFORMATION);
        alert.setTitle("Solved!");
        alert.setHeaderText(text);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) alert.close();
    }

    public void setNext(EventHandler<Event> next) {
        this.next = next;
    }

    public void close() {
        if (grid != null) grid.shutdownAutoSolve();
    }
}