package io.github.projectunified.unidialog.packetevents.action;

import com.github.retrooper.packetevents.protocol.chat.clickevent.RunCommandClickEvent;
import com.github.retrooper.packetevents.protocol.dialog.action.Action;
import com.github.retrooper.packetevents.protocol.dialog.action.StaticAction;
import io.github.projectunified.unidialog.core.action.RunCommandAction;

public class PERunCommandAction implements RunCommandAction<PERunCommandAction>, PEDialogAction {
    private String command;

    @Override
    public PERunCommandAction command(String command) {
        this.command = command;
        return this;
    }

    @Override
    public Action getAction() {
        if (command == null) {
            throw new IllegalStateException("Command must be set before getting the action");
        }
        return new StaticAction(new RunCommandClickEvent(command));
    }
}
