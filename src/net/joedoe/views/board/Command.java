package net.joedoe.views.board;

import net.joedoe.entities.IBridge;

public class Command {
    private IBridge bridge;
    private boolean add, doubleRemove;

    public Command(IBridge bridge, boolean add) {
        this.bridge = bridge;
        this.add = add;
        if (!add && bridge.isDoubleBridge()) doubleRemove = true;
    }

    public IBridge getBridge() {
        return bridge;
    }

    public boolean isAdd() {
        return add;
    }

    /**
     * If double bridge was removed by one additional click.
     *
     * @return true if bridge was removed by one click
     */
    public boolean isDoubleRemove() {
        return doubleRemove;
    }

    @Override
    public String toString() {
        return "Command{" + bridge.toString() + "}, add=" + add + "}";
    }
}
