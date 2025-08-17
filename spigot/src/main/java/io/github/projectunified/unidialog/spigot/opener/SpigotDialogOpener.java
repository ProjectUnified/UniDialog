package io.github.projectunified.unidialog.spigot.opener;

import io.github.projectunified.unidialog.bungeecord.opener.BungeeDialogOpener;
import net.md_5.bungee.api.dialog.Dialog;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class SpigotDialogOpener extends BungeeDialogOpener {
    public SpigotDialogOpener(Dialog dialog) {
        super(dialog);
    }

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
