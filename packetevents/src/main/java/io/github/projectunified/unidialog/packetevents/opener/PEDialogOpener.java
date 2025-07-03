package io.github.projectunified.unidialog.packetevents.opener;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.dialog.Dialog;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerShowDialog;
import io.github.projectunified.unidialog.core.opener.DialogOpener;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
import java.util.function.Function;

public class PEDialogOpener implements DialogOpener {
    private final Dialog dialog;
    private final Function<UUID, @Nullable Object> playerFunction;

    public PEDialogOpener(Dialog dialog, Function<UUID, @Nullable Object> playerFunction) {
        this.dialog = dialog;
        this.playerFunction = playerFunction;
    }

    @Override
    public boolean open(UUID uuid) {
        Object player = playerFunction.apply(uuid);
        if (player == null) return false;

        WrapperPlayServerShowDialog wrapper = new WrapperPlayServerShowDialog(dialog);
        PacketEvents.getAPI().getPlayerManager().sendPacket(player, wrapper);
        return true;
    }
}
