package net.joedoe.views;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
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
import net.joedoe.utils.GameInfo;
import net.joedoe.utils.GameManager;
import net.joedoe.utils.Timer;

import java.io.File;
import java.util.List;

import static net.joedoe.utils.GameInfo.CONTAINER_OFFSET;

public class Board extends BorderPane {
    private final SceneController controller;
    private GameManager gameManager = GameManager.getInstance();
    private GameData gameData = GameData.getInstance();
    private int width, height;
    private StackPane board;
    private ToolBar toolBar;
    private Grid grid;
    private Timer timer;
    private Label info, status = new Label();
    private Image coin = new Image("file:assets" + File.separator + "images" + File.separator + "coin.png");
    private Image clock = new Image("file:assets" + File.separator + "images" + File.separator + "clock.png");
    private ImageView imageView;
    private CheckBox checkBox;
    private HBox controls;
    private MediaPlayer player;

    public Board(SceneController controller) {
        getStylesheets().add("file:assets/css/dracula.css");
        this.controller = controller;
        toolBar = createToolBar();
        setTop(toolBar);
        setCenter(createBoard());
        setSound();
    }

    private void setSound() {
        String soundUrl = "assets" + File.separator + "sounds" + File.separator + "waves.wav";
        Media sound = new Media(new File(soundUrl).toURI().toString());
        player = new MediaPlayer(sound);
        player.setOnEndOfMedia(() -> player.seek(Duration.ZERO));
        player.play();
    }

    private ToolBar createToolBar() {
        String title;
        switch (gameManager.getMode()) {
            case LEVEL:
                title = "Level " + gameManager.getLevel() + "/25";
                break;
            case TIME:
                title = "Time mode";
                break;
            case FREE:
                title = "Free mode";
                break;
            default:
                title = "Title";
        }
        return new ToolBar(controller, "Start", title);
    }

    private BorderPane createBoard() {
        BorderPane borderPane = new BorderPane();
        borderPane.setTop(createBoardTop());
        board = new StackPane();
        board.setPadding(new Insets(CONTAINER_OFFSET, CONTAINER_OFFSET, 0, CONTAINER_OFFSET));
        borderPane.setCenter(board);
        borderPane.setBottom(createBoardBottom());
        return borderPane;
    }

    private HBox createBoardTop() {
        HBox resetBox = new HBox(CONTAINER_OFFSET);
        resetBox.setMinWidth(200);
        Button reset = new Button("\u21BA");
        reset.setAlignment(Pos.CENTER_LEFT);
        reset.setOnAction(e -> grid.reset());
        resetBox.getChildren().add(reset);

        HBox controlsBox = new HBox(CONTAINER_OFFSET);
        controlsBox.setAlignment(Pos.CENTER_RIGHT);
        controlsBox.setMinWidth(200);
        Button zoomIn = new Button("\uD83D\uDD0D+");
        zoomIn.setOnAction(e -> {
            GameInfo.zoomIn();
            grid.zoomInOut(width, height, checkBox.isSelected());
        });
        Button zoomOut = new Button("\uD83D\uDD0D-");
        zoomOut.setOnAction(e -> {
            GameInfo.zoomOut();
            grid.zoomInOut(width, height, checkBox.isSelected());
        });
        Button sound = new Button("\uD83D\uDD0A");
        sound.setMinWidth(30);
        sound.setOnAction(e -> {
            String txt = sound.getText();
            if (txt.equals("\uD83D\uDD07")) sound.setText("\uD83D\uDD0A");
            else sound.setText("\uD83D\uDD07");
            if (player.getStatus() == MediaPlayer.Status.PLAYING) player.pause();
            else player.play();
        });
        controlsBox.getChildren().addAll(zoomIn, zoomOut, sound);

        Region regionLeft = new Region();
        HBox.setHgrow(regionLeft, Priority.ALWAYS);
        Region regionRight = new Region();
        HBox.setHgrow(regionRight, Priority.ALWAYS);

        HBox box = new HBox();
        box.setPadding(new Insets(CONTAINER_OFFSET, CONTAINER_OFFSET, 0, CONTAINER_OFFSET));
        box.getChildren().addAll(resetBox, regionLeft, createInfoBox(), regionRight, controlsBox);
        return box;
    }

    private HBox createInfoBox() {
        HBox infoBox = new HBox(CONTAINER_OFFSET);
        infoBox.setAlignment(Pos.CENTER);
        infoBox.setMinWidth(200);
        imageView = new ImageView();
        imageView.setPreserveRatio(true);
        imageView.setFitHeight(15);
        switch (gameManager.getMode()) {
            case LEVEL:
            case FREE:
                imageView.setImage(coin);
                info = new Label(Integer.toString(gameManager.getPoints()));
                break;
            case TIME:
                imageView.setImage(clock);
                info = new Label(timer.getStartTime());
        }
        info.setFont(new Font(14));
        infoBox.getChildren().addAll(imageView, info);
        return infoBox;
    }

    private VBox createBoardBottom() {
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

    void setPuzzle() {
        setGrid(gameData.getWidth(), gameData.getHeight(), gameData.getIsles(), null);
        if (timer != null) timer.stop();
        switch (gameManager.getMode()) {
            case LEVEL:
                toolBar.updateTitle("Level " + gameManager.getLevel() + "/25");
                imageView.setImage(coin);
                info.setText(Integer.toString(gameManager.getPoints()));
                controls.setVisible(true);
                break;
            case TIME:
                toolBar.updateTitle("Time mode");
                imageView.setImage(clock);
                if (timer == null) {
                    timer = new Timer();
                    timer.setListener(() -> Platform.runLater(() -> info.setText(timer.getTime())));
                    timer.start();
                } else {
                    timer.restart();
                }
                info.setText(timer.getStartTime());
                controls.setVisible(false);
                break;
            case FREE:
                toolBar.updateTitle("Free mode");
                imageView.setImage(coin);
                info.setText(Integer.toString(gameManager.getTempPoints()));
                controls.setVisible(true);
        }
    }

    private void setGrid(int width, int height, List<IIsle> isles, List<IBridge> bridges) {
        this.width = width;
        this.height = height;
        board.getChildren().remove(grid);
        if (grid != null) grid.shutdownAutoSolve();
        grid = new Grid(this::handleStatus, width, height, isles, bridges);
        grid.setPointListener(this::handlePoints);
        ScrollPane scroll = new ScrollPane();
        scroll.setContent(grid);
        scroll.setFitToHeight(true);
        scroll.setFitToWidth(true);
        board.getChildren().add(scroll);
        grid.setShowMissingBridges(checkBox.isSelected());
    }

    void setGridWithBridges() {
        setGrid(gameData.getWidth(), gameData.getHeight(), gameData.getIsles(), gameData.getBridges());
    }

    private void handlePoints(PointEvent e) {
        info.setText(e.getPoints());
    }

    private void handleStatus(StatusEvent e) {
        StatusEvent.Status status = e.getStatus();
        this.status.setText(status.getText());
        if (status == StatusEvent.Status.SOLVED) {
            switch (gameManager.getMode()) {
                case LEVEL:
                    controller.showAlert(AlertType.INFORMATION, "Solved!", "Level " + gameManager.getLevel() + " solved.");
                    gameManager.savePoints();
                    gameManager.increaseLevel();
                    toolBar.updateTitle("Level " + gameManager.getLevel() + "/25");
                    break;
                case TIME:
                    timer.stop();
                    controller.showAlert(AlertType.INFORMATION, "Solved!", "Puzzle solved in " + info.getText() + ".");
                    break;
                case FREE:
                    controller.showAlert(AlertType.INFORMATION, "Solved!", "Solved.");
            }
        }
    }

    void close() {
        if (grid != null) grid.shutdownAutoSolve();
        if (timer != null) timer.shutdown();
    }

    void savePuzzle() {
        grid.savePuzzle();
    }
}