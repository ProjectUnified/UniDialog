package io.github.projectunified.unidialog.bungeecord.opener;

import io.github.projectunified.unidialog.core.opener.DialogOpener;
import net.md_5.bungee.api.dialog.Dialog;

/**
 * The Bungee implementation of the dialog opener
 */
public abstract class BungeeDialogOpener implements DialogOpener {
    private final Dialog dialog;

    /**
     * Create a new dialog opener
     *
     * @param dialog the dialog to open
     */
    protected BungeeDialogOpener(Dialog dialog) {
        this.dialog = dialog;
    }

    /**
     * Get the dialog to open
     *
     * @return the dialog to open
     */
    public Dialog getDialog() {
        return dialog;
    }
}
