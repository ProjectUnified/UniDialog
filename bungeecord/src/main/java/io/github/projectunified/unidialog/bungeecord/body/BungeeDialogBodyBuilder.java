package io.github.projectunified.unidialog.bungeecord.body;

import io.github.projectunified.unidialog.core.body.DialogBodyBuilder;
import net.md_5.bungee.api.dialog.body.DialogBody;

@SuppressWarnings("unchecked")
public class BungeeDialogBodyBuilder implements DialogBodyBuilder<Object>, BungeeDialogBody {
    private BungeeDialogBody current;

    @Override
    public BungeeItemBody item() {
        current = new BungeeItemBody();
        return (BungeeItemBody) current;
    }

    @Override
    public BungeeTextBody text() {
        current = new BungeeTextBody();
        return (BungeeTextBody) current;
    }

    @Override
    public DialogBody getDialogBody() {
        if (current == null) {
            throw new IllegalStateException("No dialog body has been created yet.");
        }
        return current.getDialogBody();
    }
}
