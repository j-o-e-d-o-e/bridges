package net.joedoe.views.board;

import net.joedoe.entities.IBridge;

public class Command {
    private IBridge bridge;
    private boolean add;

    public Command(IBridge bridge, boolean add) {
        this.bridge = bridge;
        this.add = add;
    }

    public IBridge getBridge() {
        return bridge;
    }

    public boolean isAdd() {
        return add;
    }

    @Override
    public String toString() {
        return "Command{" + bridge.toString() + "}, add=" + add + "}";
    }
}
