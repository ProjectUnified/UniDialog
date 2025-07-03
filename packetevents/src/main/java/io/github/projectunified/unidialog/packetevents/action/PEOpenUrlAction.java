package io.github.projectunified.unidialog.packetevents.action;

import com.github.retrooper.packetevents.protocol.chat.clickevent.OpenUrlClickEvent;
import com.github.retrooper.packetevents.protocol.dialog.action.Action;
import com.github.retrooper.packetevents.protocol.dialog.action.StaticAction;
import io.github.projectunified.unidialog.core.action.OpenUrlAction;

public class PEOpenUrlAction implements OpenUrlAction<PEOpenUrlAction>, PEDialogAction {
    private String url;

    @Override
    public PEOpenUrlAction url(String url) {
        this.url = url;
        return this;
    }

    @Override
    public Action getAction() {
        if (url == null) {
            throw new IllegalStateException("URL must be set before getting the action");
        }
        return new StaticAction(new OpenUrlClickEvent(url));
    }
}
