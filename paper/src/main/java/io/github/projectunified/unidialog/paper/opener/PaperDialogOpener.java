package io.github.projectunified.unidialog.paper.opener;

import io.github.projectunified.unidialog.core.opener.DialogOpener;
import io.papermc.paper.dialog.Dialog;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public record PaperDialogOpener(Dialog dialog) implements DialogOpener {
    @Override
    public boolean open(UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) return false;
        player.showDialog(dialog);
        return true;
    }
}
