package io.github.projectunified.unidialog.spigot.opener;

import io.github.projectunified.unidialog.core.opener.DialogOpener;
import net.md_5.bungee.api.dialog.Dialog;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class SpigotDialogOpener implements DialogOpener {
    private final Dialog dialog;

    public SpigotDialogOpener(Dialog dialog) {
        this.dialog = dialog;
    }

    @Override
    public boolean open(UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) return false;
        player.showDialog(dialog);
        return true;
    }
}
