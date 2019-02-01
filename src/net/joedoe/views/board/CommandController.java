package net.joedoe.views.board;

import java.util.LinkedList;

public class CommandController {
    private LinkedList<Command> commands = new LinkedList<>();

    public void addCommand(Command command) {
        if (commands.size() == 10) commands.removeFirst();
        commands.addLast(command);
    }

    public Command getCommand() {
        if (commands.isEmpty()) return null;
        return commands.removeLast();
    }
}