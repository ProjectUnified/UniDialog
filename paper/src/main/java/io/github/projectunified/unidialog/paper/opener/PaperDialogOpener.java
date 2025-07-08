package io.github.projectunified.unidialog.paper.opener;

import io.github.projectunified.unidialog.core.opener.DialogOpener;
import io.papermc.paper.dialog.Dialog;
import net.kyori.adventure.audience.Audience;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public record PaperDialogOpener(Dialog dialog) implements DialogOpener {
    /**
     * Open a dialog for a specific audience
     *
     * @param audience the audience to open the dialog for
     */
    public void open(Audience audience) {
        audience.showDialog(dialog);
    }

    @Override
    public boolean open(UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) return false;
        open(player);
        return true;
    }
}
