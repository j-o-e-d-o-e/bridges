package net.joedoe.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
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

import java.io.File;
import java.util.List;

import static net.joedoe.utils.GameInfo.CONTAINER_OFFSET;

public abstract class Board extends BorderPane {
    protected SceneController controller;
    private GameData gameData = GameData.getInstance();
    private int width, height;
    private StackPane board;
    private Grid grid;
    private CheckBox checkBox;
    private MediaPlayer player;
    ToolBar toolBar;
    Label info;
    HBox controls;
    Label status = new Label();

    public Board(SceneController controller) {
        getStylesheets().add("file:assets/css/dracula.css");
        this.controller = controller;
        setSound();
    }

    void setLayout(){
        toolBar = createToolBar();
        setTop(toolBar);
        setCenter(createBoard());
    }

    private void setSound() {
        String soundUrl = "assets" + File.separator + "sounds" + File.separator + "waves.wav";
        Media sound = new Media(new File(soundUrl).toURI().toString());
        player = new MediaPlayer(sound);
        player.setOnEndOfMedia(() -> player.seek(Duration.ZERO));
        player.play();
    }

    abstract ToolBar createToolBar();

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

        HBox infoBox = new HBox(CONTAINER_OFFSET);
        infoBox.setAlignment(Pos.CENTER);
        infoBox.setMinWidth(200);
        ImageView imageView = new ImageView();
        imageView.setPreserveRatio(true);
        imageView.setFitHeight(15);
        imageView.setImage(getInfoImage());
        info = new Label(getInfoText());
        info.setFont(new Font(14));
        infoBox.getChildren().addAll(imageView, info);

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
        box.getChildren().addAll(resetBox, regionLeft, infoBox, regionRight, controlsBox);
        return box;
    }

    abstract Image getInfoImage();

    abstract String getInfoText();

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

    void setGrid() {
        setGrid(gameData.getWidth(), gameData.getHeight(), gameData.getIsles(), null);
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

    abstract void handleStatus(StatusEvent e);

    void close() {
        if (grid != null) grid.shutdownAutoSolve();
    }

    void savePuzzle() {
        grid.savePuzzle();
    }
}