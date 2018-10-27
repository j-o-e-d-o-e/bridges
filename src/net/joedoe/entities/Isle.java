package net.joedoe.entities;

import javafx.scene.layout.StackPane;
import net.joedoe.utils.Direction;
import net.joedoe.views.IslePane;

import java.util.ArrayList;
import java.util.List;

public class Isle implements GridEntity {
    private IslePane pane;
    private int bridgeCount, missingBridgeCount, row, column;
    private boolean showMissingBridges;
    private List<Bridge> bridges = new ArrayList<>();

    Isle(int row, int column, int bridgeCount) {
        this.row = row;
        this.column = column;
        this.bridgeCount = bridgeCount;
        this.missingBridgeCount = bridgeCount - ((int) (Math.random() * bridgeCount) + 1);
        pane = new IslePane(this);
        pane.setText(Integer.toString(row) + "/" + Integer.toString(column));
    }

    public void setText(boolean showMissingBridges) {
        this.showMissingBridges = showMissingBridges;
        if (showMissingBridges)
            pane.setText(Integer.toString(missingBridgeCount));
        else
            pane.setText(Integer.toString(bridgeCount));
    }

    public int getBridgeCount() {
        if (showMissingBridges)
            return missingBridgeCount;
        return bridgeCount;
    }

    public void addBridge(Bridge bridge) {
        bridges.add(bridge);
    }

    public void removeBridge(Bridge bridge) {
        bridges.remove(bridge);
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

    public Bridge getBridge(Direction direction) {
        for (Bridge bridge : bridges)
            if (bridge.getDirection() == direction)
                return bridge;
        return null;
    }
}