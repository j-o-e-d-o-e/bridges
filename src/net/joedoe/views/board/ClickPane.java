package net.joedoe.views.board;

import javafx.scene.layout.StackPane;

class ClickPane extends StackPane {
    private IslePane up, left, down, right;

    IslePane getUp() {
        return up;
    }

    void setUp(IslePane up) {
        this.up = up;
    }

    IslePane getLeft() {
        return left;
    }

    void setLeft(IslePane left) {
        this.left = left;
    }

    IslePane getDown() {
        return down;
    }

    void setDown(IslePane down) {
        this.down = down;
    }

    IslePane getRight() {
        return right;
    }

    void setRight(IslePane right) {
        this.right = right;
    }
}
