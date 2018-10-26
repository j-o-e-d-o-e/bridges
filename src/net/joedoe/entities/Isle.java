package net.joedoe.entities;

import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import net.joedoe.views.IsleListener;

import java.util.ArrayList;
import java.util.List;

import static net.joedoe.utils.GameInfo.CIRCLE_COLOR;
import static net.joedoe.utils.GameInfo.CIRCLE_RADIUS;

public class Isle extends StackPane implements GridEntity {
    @SuppressWarnings("FieldCanBeLocal")
    private Circle circle;
    private Label text;
    private int bridgeCount, missingBridgeCount, row, column;
    private boolean showMissingBridges;
    private  IsleListener listener;
    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    private List<Bridge> bridges = new ArrayList<>();

    @SuppressWarnings("WeakerAccess")
    public Isle(int row, int column, int bridgeCount) {
//        setBorder(new Border(new BorderStroke(Color.YELLOW, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        this.row = row;
        this.column = column;
        this.bridgeCount = bridgeCount;
        this.missingBridgeCount = bridgeCount - ((int) (Math.random() * bridgeCount) + 1);
        circle = new Circle(CIRCLE_RADIUS, CIRCLE_COLOR);
        text = new Label(Integer.toString(row) + "/" + Integer.toString(column));
        getChildren().addAll(circle, text);
        setOnMouseClicked(e -> listener.handle(e));
    }

    public void setText(boolean showMissingBridges) {
        this.showMissingBridges = showMissingBridges;
        if (showMissingBridges)
            text.setText(Integer.toString(missingBridgeCount));
        else
            text.setText(Integer.toString(bridgeCount));
    }

//    public int getBridgeCount() {
//        if (showMissingBridges)
//            return missingBridgeCount;
//        return bridgeCount;
//    }
//
//    public void addBridge(Bridge bridge) {
//        bridges.add(bridge);
//    }
//
//    public void removeBridge(int index) {
//        bridges.remove(index);
//    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public void setListener(IsleListener listener) {
        this.listener = listener;
    }

    @Override
    public String toString() {
        return "Isle{" + "row=" + row + ", column=" + column + '}';
    }
}