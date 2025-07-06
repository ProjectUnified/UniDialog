package io.github.projectunified.unidialog.spigot.input;

import net.md_5.bungee.api.dialog.input.DialogInput;

public abstract class SpigotDialogInput {
    protected final String key;

    protected SpigotDialogInput(String key) {
        this.key = key;
    }

    public abstract DialogInput getDialogInput();
}
