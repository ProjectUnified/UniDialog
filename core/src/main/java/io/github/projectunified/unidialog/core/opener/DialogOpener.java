package io.github.projectunified.unidialog.core.opener;

import java.util.UUID;

/**
 * Interface for opening dialogs
 */
public interface DialogOpener {
    /**
     * Open the dialog for the given player UUID
     *
     * @param uuid the UUID of the player to open the dialog for
     * @return true if the dialog was opened successfully, false otherwise
     */
    boolean open(UUID uuid);
}
