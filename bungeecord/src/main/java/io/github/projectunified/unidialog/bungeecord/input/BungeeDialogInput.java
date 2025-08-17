package io.github.projectunified.unidialog.bungeecord.input;

import net.md_5.bungee.api.dialog.input.DialogInput;

public abstract class BungeeDialogInput {
    protected final String key;

    protected BungeeDialogInput(String key) {
        this.key = key;
    }

    public abstract DialogInput getDialogInput();
}
