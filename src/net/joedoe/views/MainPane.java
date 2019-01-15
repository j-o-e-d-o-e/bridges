package net.joedoe.views;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import net.joedoe.utils.GameManager;
import net.joedoe.utils.GameManager.Mode;
import net.joedoe.utils.Timer;

import java.io.File;
import java.util.Optional;

import static net.joedoe.utils.GameInfo.CONTAINER_OFFSET;

public class MainPane extends BorderPane {
    private GameManager gameManager = GameManager.getInstance();
    private Board board;
    private Timer timer;
    private Label mode, infoLbl, status;
    private ImageView infoImg;
    private HBox controls;


    MainPane() {
        status = new Label();
        board = new Board(this::handleStatus, gameManager.getLevel());
        board.setPointListener(this::handlePoints);
        setTop(createTopBar());
        setCenter(board);
        setBottom(createControls());
    }

    private Node createTopBar() {
        HBox modeBox = new HBox(CONTAINER_OFFSET);
        modeBox.setAlignment(Pos.CENTER_LEFT);
        modeBox.setMinWidth(200);
        mode = new Label("Level " + gameManager.getLevel() + "/25");
        mode.setFont(new Font(14));
        modeBox.getChildren().add(mode);

        HBox infoBox = new HBox(CONTAINER_OFFSET);
        infoBox.setAlignment(Pos.CENTER);
        infoBox.setMinWidth(200);
        infoImg = new ImageView();
        Image image = new Image("file:assets" + File.separator + "images" + File.separator + "coin.png");
        infoImg.setImage(image);
        infoImg.setPreserveRatio(true);
        infoImg.setFitHeight(15);
        infoLbl = new Label(Integer.toString(gameManager.getPoints()));
        infoLbl.setFont(new Font(14));
        infoBox.getChildren().addAll(infoImg, infoLbl);

        HBox controlsBox = new HBox(CONTAINER_OFFSET);
        controlsBox.setAlignment(Pos.CENTER_RIGHT);
        controlsBox.setMinWidth(200);
        Button zoomIn = new Button("\uD83D\uDD0D+");
        zoomIn.setOnAction(e -> board.zoomIn());
        Button zoomOut = new Button("\uD83D\uDD0D-");
        zoomOut.setOnAction(e -> board.zoomOut());
        Button sound = new Button("\uD83D\uDD0A");
        sound.setMinWidth(30);
        sound.setOnAction(e -> {
            String txt = sound.getText();
            if (txt.equals("\uD83D\uDD07")) sound.setText("\uD83D\uDD0A");
            else sound.setText("\uD83D\uDD07");
            board.setSound();
        });
        controlsBox.getChildren().addAll(zoomIn, zoomOut, sound);

        Region regionLeft = new Region();
        HBox.setHgrow(regionLeft, Priority.ALWAYS);
        Region regionRight = new Region();
        HBox.setHgrow(regionRight, Priority.ALWAYS);

        HBox box = new HBox();
        box.setPadding(new Insets(CONTAINER_OFFSET, CONTAINER_OFFSET, 0, CONTAINER_OFFSET));
        box.getChildren().addAll(modeBox, regionLeft, infoBox, regionRight, controlsBox);
        return box;
    }


    private Node createControls() {
        VBox vBox = new VBox(CONTAINER_OFFSET);
        vBox.setPadding(new Insets(CONTAINER_OFFSET, CONTAINER_OFFSET, CONTAINER_OFFSET, CONTAINER_OFFSET));
        CheckBox checkBox = new CheckBox("Show missing bridges");
        checkBox.setSelected(true);
        checkBox.setOnAction(e -> board.setShowMissingBridges(checkBox.isSelected()));
        controls = new HBox(CONTAINER_OFFSET);
        controls.setAlignment(Pos.CENTER);
        controls.setPrefWidth(100);
        Button solveBtn = new Button("_Solve auto");
        solveBtn.setMnemonicParsing(true);
        solveBtn.setMinWidth(controls.getPrefWidth());
        solveBtn.setOnAction(e -> {
            if (board.autoSolverIsRunning()) board.stopAutoSolve();
            else board.startAutoSolve();
        });
        Button nextBtn = new Button("_Next bridge");
        nextBtn.setMnemonicParsing(true);
        nextBtn.setMinWidth(controls.getPrefWidth());
        nextBtn.setOnAction(e -> board.getNextBridge());
        controls.getChildren().addAll(solveBtn, nextBtn);
        vBox.getChildren().addAll(checkBox, controls, status);
        return vBox;
    }

    private void handlePoints(PointEvent e) {
        infoLbl.setText(e.getPoints());
    }

    private void handleStatus(StatusEvent e) {
        StatusEvent.Status status = e.getStatus();
        this.status.setText(status.getText());
        if (status == StatusEvent.Status.SOLVED) {
            if (gameManager.getMode() == Mode.LEVEL) {
                gameManager.savePoints();
                solved("Level " + gameManager.getLevel() + " solved.");
                gameManager.increaseLevel();
                mode.setText("Level " + gameManager.getLevel() + "/25");
                board.createNewGame(gameManager.getLevel());
            } else if (gameManager.getMode() == Mode.TIME) {
                timer.stop();
                solved("Puzzle solved in " + infoLbl.getText() + ".");
            } else {
                solved("Solved.");
            }
        }
    }

    private void solved(String text) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Solved!");
        alert.setHeaderText(text);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) alert.close();

    }

    public void reset() {
        gameManager.resetPoints();
        infoLbl.setText(String.valueOf(gameManager.getPoints()));
        board.reset();
    }

    void createLevelGame() {
        if (board.autoSolverIsRunning()) board.stopAutoSolve();
        if (gameManager.getMode() == Mode.TIME) {
            timer.stop();
            controls.setVisible(true);
        }
        gameManager.setMode(Mode.LEVEL);
        board.createNewGame(gameManager.getLevel());
    }

    void createTimeGame(int level) {
        if (board.autoSolverIsRunning()) board.stopAutoSolve();
        if (timer == null) {
            timer = new Timer();
            timer.setListener(() -> Platform.runLater(() -> infoLbl.setText(timer.getTime())));
        }
        gameManager.setMode(Mode.TIME);
        mode.setText("Time mode");
        infoImg.setImage(new Image("file:assets" + File.separator + "images" + File.separator + "timer.png"));
        controls.setVisible(false);
        board.createNewGame(level);
        infoLbl.setText(timer.getStartTime());
        if (!timer.isRunning()) timer.start();
    }

    void createFreeGame() {
        if (board.autoSolverIsRunning()) board.stopAutoSolve();
        if (!controls.isVisible()) controls.setVisible(true);
        gameManager.setMode(Mode.FREE);
        board.setGrid();
    }

    void stopAutoSolve() {
        board.stopAutoSolve();
    }

    void setLoadedGrid() {
        board.setGridWithBridges();
    }

    public void saveGame() {
        board.saveGame();
    }

    public void close() {
        board.shutdownAutoSolve();
        if (timer != null && timer.isRunning()) timer.shutdown();
    }
}
