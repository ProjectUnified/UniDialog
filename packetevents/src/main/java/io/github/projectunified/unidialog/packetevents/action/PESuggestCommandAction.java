package io.github.projectunified.unidialog.packetevents.action;

import com.github.retrooper.packetevents.protocol.chat.clickevent.SuggestCommandClickEvent;
import com.github.retrooper.packetevents.protocol.dialog.action.Action;
import com.github.retrooper.packetevents.protocol.dialog.action.StaticAction;
import io.github.projectunified.unidialog.core.action.SuggestCommandAction;

public class PESuggestCommandAction implements SuggestCommandAction<PESuggestCommandAction>, PEDialogAction {
    private String command;

    @Override
    public PESuggestCommandAction command(String command) {
        this.command = command;
        return this;
    }

    @Override
    public Action getAction() {
        if (command == null) {
            throw new IllegalStateException("Command must be set before getting the action");
        }
        return new StaticAction(new SuggestCommandClickEvent(command));
    }
}
