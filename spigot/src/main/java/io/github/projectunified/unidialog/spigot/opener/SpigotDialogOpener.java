package io.github.projectunified.unidialog.spigot.opener;

import io.github.projectunified.unidialog.bungeecord.opener.BungeeDialogOpener;
import net.md_5.bungee.api.dialog.Dialog;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Spigot implementation of {@link BungeeDialogOpener} that opens dialogs for Bukkit players.
 */
public class SpigotDialogOpener extends BungeeDialogOpener {
    /**
     * Create a new Spigot dialog opener.
     *
     * @param dialog the dialog to open
     */
    public SpigotDialogOpener(Dialog dialog) {
        super(dialog);
    }

    /**
     * Open the dialog for a player.
     *
     * @param player the player to open the dialog for
     */
    public void open(Player player) {
        player.showDialog(getDialog());
    }

    @Override
    public boolean open(UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) return false;
        open(player);
        return true;
    }
}
