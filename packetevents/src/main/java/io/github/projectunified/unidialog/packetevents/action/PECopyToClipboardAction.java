package io.github.projectunified.unidialog.packetevents.action;

import com.github.retrooper.packetevents.protocol.chat.clickevent.CopyToClipboardClickEvent;
import com.github.retrooper.packetevents.protocol.dialog.action.Action;
import com.github.retrooper.packetevents.protocol.dialog.action.StaticAction;
import io.github.projectunified.unidialog.core.action.CopyToClipboardAction;

public class PECopyToClipboardAction implements CopyToClipboardAction<PECopyToClipboardAction>, PEDialogAction {
    private String value;

    @Override
    public PECopyToClipboardAction value(String value) {
        this.value = value;
        return this;
    }

    @Override
    public Action getAction() {
        if (value == null) {
            throw new IllegalStateException("Value must be set before getting the action");
        }
        return new StaticAction(new CopyToClipboardClickEvent(value));
    }
}
