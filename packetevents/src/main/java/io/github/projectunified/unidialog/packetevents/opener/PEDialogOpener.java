package io.github.projectunified.unidialog.packetevents.opener;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.ConnectionState;
import com.github.retrooper.packetevents.protocol.dialog.Dialog;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.wrapper.common.server.WrapperCommonServerShowDialog;
import com.github.retrooper.packetevents.wrapper.configuration.server.WrapperConfigServerShowDialog;
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

    /**
     * Get the dialog associated with this opener
     *
     * @return the dialog
     */
    public Dialog dialog() {
        return dialog;
    }

    /**
     * Open the dialog for a user, optionally specifying if it is a configuration dialog.
     *
     * @param user          the user to open the dialog for
     * @param configuration if true, opens as a configuration dialog; if false, opens as a play dialog
     */
    public void open(User user, boolean configuration) {
        WrapperCommonServerShowDialog<?> wrapper = configuration
                ? new WrapperConfigServerShowDialog(dialog)
                : new WrapperPlayServerShowDialog(dialog);
        user.sendPacket(wrapper);
    }

    /**
     * Open the dialog for a user, automatically determining if it is a configuration dialog based on the user's connection state.
     *
     * @param user the user to open the dialog for
     */
    public void open(User user) {
        open(user, user.getConnectionState() == ConnectionState.CONFIGURATION);
    }

    @Override
    public boolean open(UUID uuid) {
        Object player = playerFunction.apply(uuid);
        if (player == null) return false;

        User user = PacketEvents.getAPI().getPlayerManager().getUser(player);
        open(user);
        return true;
    }
}
