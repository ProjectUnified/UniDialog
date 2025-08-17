package io.github.projectunified.unidialog.bungeecord.opener;

import io.github.projectunified.unidialog.core.opener.DialogOpener;
import net.md_5.bungee.api.dialog.Dialog;

public abstract class BungeeDialogOpener implements DialogOpener {
    private final Dialog dialog;

    protected BungeeDialogOpener(Dialog dialog) {
        this.dialog = dialog;
    }

    public Dialog getDialog() {
        return dialog;
    }
}
