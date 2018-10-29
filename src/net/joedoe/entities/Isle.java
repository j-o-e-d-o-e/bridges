package net.joedoe.entities;

import javafx.scene.layout.StackPane;
import net.joedoe.utils.Direction;
import net.joedoe.views.IslePane;

import java.util.ArrayList;
import java.util.List;

import static net.joedoe.utils.GameInfo.*;

public class Isle {
    private IslePane pane;
    private int bridgeCount, missingBridgeCount;
    private int row, column;
    private boolean showMissingBridges, showFinished, showAlert;
    private List<Bridge> bridges = new ArrayList<>();

    public Isle(int row, int column, int bridgeCount) {
        this.row = row;
        this.column = column;
        this.bridgeCount = bridgeCount;
        this.missingBridgeCount = bridgeCount;
        showMissingBridges = true;
        pane = new IslePane(this);
        pane.setText(Integer.toString(missingBridgeCount));
    }

    public void addBridge(Bridge bridge) {
        bridges.add(bridge);
        missingBridgeCount--;
        if (showMissingBridges)
            pane.setText(Integer.toString(missingBridgeCount));
        if (missingBridgeCount == 0) {
            pane.setColor(SOLVED_COLOR);
            showFinished = true;
        }
        if (missingBridgeCount < 0) {
            pane.setColor(ALERT_COLOR);
            showAlert = true;
        }
    }

    public void removeBridge(Bridge bridge) {
        bridges.remove(bridge);
        missingBridgeCount++;
        if (showMissingBridges)
            pane.setText(Integer.toString(missingBridgeCount));
        setCircleColor();
    }

    public Bridge getBridge(Direction direction) {
        for (Bridge bridge : bridges)
            if (bridge.getDirection() == direction)
                return bridge;
        return null;
    }

    public List<Bridge> getBridges() {
        return bridges;
    }

    public void increaseMissingBridges() {
        missingBridgeCount++;
        if (showMissingBridges)
            pane.setText(Integer.toString(missingBridgeCount));
        setCircleColor();
    }

    private void setCircleColor() {
        if (showAlert && missingBridgeCount == 0) {
            showAlert = false;
            pane.setColor(SOLVED_COLOR);
        } else if (showFinished && missingBridgeCount > 0) {
            showFinished = false;
            pane.setColor(STD_COLOR);
        }
    }

    public void decreaseMissingBridges() {
        missingBridgeCount--;
        if (showMissingBridges)
            pane.setText(Integer.toString(missingBridgeCount));
        if (missingBridgeCount == 0) {
            pane.setColor(SOLVED_COLOR);
            showFinished = true;
        }
        if (missingBridgeCount < 0) {
            pane.setColor(ALERT_COLOR);
            showAlert = true;
        }
    }

    public int getMissingBridgeCount() {
        return missingBridgeCount;
    }

    public void setText(boolean showMissingBridges) {
        this.showMissingBridges = showMissingBridges;
        if (showMissingBridges)
            pane.setText(Integer.toString(missingBridgeCount));
        else
            pane.setText(Integer.toString(bridgeCount));
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public StackPane getPane() {
        return pane;
    }

    @Override
    public String toString() {
        return "Isle{" + "row=" + row + ", column=" + column + '}';
    }
}