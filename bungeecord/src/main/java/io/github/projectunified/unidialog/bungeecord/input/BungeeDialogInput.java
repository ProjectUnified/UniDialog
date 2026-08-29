package io.github.projectunified.unidialog.bungeecord.input;

import net.md_5.bungee.api.dialog.input.DialogInput;

/**
 * The Bungee implementation of the dialog input
 */
public abstract class BungeeDialogInput {
    /**
     * The key of the input
     */
    protected final String key;

    /**
     * Create a new dialog input
     *
     * @param key the key of the input
     */
    protected BungeeDialogInput(String key) {
        this.key = key;
    }

    /**
     * Get the Bungee dialog input
     *
     * @return the Bungee dialog input
     */
    public abstract DialogInput getDialogInput();
}
