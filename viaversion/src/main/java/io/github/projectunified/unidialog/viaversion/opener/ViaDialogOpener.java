package io.github.projectunified.unidialog.viaversion.opener;

import com.viaversion.nbt.tag.CompoundTag;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.minecraft.Holder;
import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.api.protocol.ProtocolPathEntry;
import com.viaversion.viaversion.api.protocol.packet.ClientboundPacketType;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.protocol.packet.State;
import com.viaversion.viaversion.api.protocol.packet.provider.PacketTypeMap;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import com.viaversion.viaversion.api.type.Types;
import com.viaversion.viaversion.protocols.v1_21_5to1_21_6.Protocol1_21_5To1_21_6;
import io.github.projectunified.unidialog.core.opener.DialogOpener;
import io.github.projectunified.unidialog.viaversion.dialog.ViaDialog;

import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

/**
 * A ViaVersion-based implementation of {@link DialogOpener} for opening dialogs.
 */
public record ViaDialogOpener(ViaDialog<?> dialog) implements DialogOpener {
    /**
     * Constructor for ViaDialogOpener
     *
     * @param dialog the dialog to open
     */
    public ViaDialogOpener {
    }

    /**
     * Clear the dialog for a user connection
     *
     * @param connection    the user connection
     * @param configuration if true, clears the configuration-phase dialog; if false, the play-phase dialog
     * @return true if the clear packet was sent, false otherwise
     */
    public static boolean clearDialog(UserConnection connection, boolean configuration) {
        if (!Via.isLoaded() || connection == null) {
            return false;
        }
        ProtocolVersion clientVersion = connection.getProtocolInfo().protocolVersion();
        if (clientVersion == null || clientVersion.olderThan(ProtocolVersion.v1_21_6)) {
            return false;
        }
        State state = configuration ? State.CONFIGURATION : State.PLAY;
        ClientboundPacketType packetType = clientboundType(clientVersion, state, "CLEAR_DIALOG");
        if (packetType == null) {
            return false;
        }
        try {
            PacketWrapper wrapper = PacketWrapper.create(packetType, connection);
            wrapper.scheduleSendRaw();
            return true;
        } catch (Exception e) {
            Via.getPlatform().getLogger().log(Level.FINE, "Failed to clear dialog for " + connection.getProtocolInfo().getUsername(), e);
            return false;
        }
    }

    /**
     * Clear the dialog for a user connection, automatically determining the phase from the connection state
     *
     * @param connection the user connection
     * @return true if the clear packet was sent, false otherwise
     */
    public static boolean clearDialog(UserConnection connection) {
        return clearDialog(connection, connection != null && connection.getProtocolInfo().getClientState() == State.CONFIGURATION);
    }

    private static ClientboundPacketType clientboundType(ProtocolVersion clientVersion, State state, String name) {
        List<ProtocolPathEntry> path = Via.getManager().getProtocolManager().getProtocolPath(clientVersion, ProtocolVersion.v1_21_6);
        if (path != null && !path.isEmpty()) {
            ClientboundPacketType type = clientboundType(path.getFirst().protocol(), state, name);
            if (type != null) {
                return type;
            }
        }
        // The client version equals the dialog base version
        Protocol<?, ?, ?, ?> base = Via.getManager().getProtocolManager().getProtocol(Protocol1_21_5To1_21_6.class);
        return base != null ? clientboundType(base, state, name) : null;
    }

    private static ClientboundPacketType clientboundType(Protocol<?, ?, ?, ?> protocol, State state, String name) {
        PacketTypeMap<? extends ClientboundPacketType> types = protocol.getPacketTypesProvider().mappedClientboundPacketTypes().get(state);
        return types == null ? null : types.typeByName(name);
    }

    /**
     * Get the dialog held by this opener
     *
     * @return the dialog
     */
    @Override
    public ViaDialog<?> dialog() {
        return dialog;
    }

    /**
     * Open the dialog for a user connection
     *
     * @param connection    the user connection
     * @param configuration if true, sends the configuration-phase dialog; if false, the play-phase dialog
     * @return true if the dialog was sent, false otherwise
     */
    public boolean open(UserConnection connection, boolean configuration) {
        if (!Via.isLoaded() || connection == null) {
            return false;
        }
        ProtocolVersion clientVersion = connection.getProtocolInfo().protocolVersion();
        if (clientVersion == null || clientVersion.olderThan(ProtocolVersion.v1_21_6)) {
            Via.getPlatform().getLogger().log(Level.FINE, "Cannot open dialog for client " + clientVersion
                    + ": dialogs require client version 1.21.6+");
            return false;
        }
        State state = configuration ? State.CONFIGURATION : State.PLAY;
        ClientboundPacketType packetType = clientboundType(clientVersion, state, "SHOW_DIALOG");
        if (packetType == null) {
            return false;
        }
        try {
            // Build the payload with the components mapped to the player's target version
            CompoundTag dialogTag = dialog.getDialogTag(clientVersion);
            PacketWrapper wrapper = PacketWrapper.create(packetType, connection);
            if (configuration) {
                wrapper.write(Types.TRUSTED_COMPOUND_TAG, dialogTag);
            } else {
                wrapper.write(Types.TRUSTED_COMPOUND_TAG_HOLDER, Holder.of(dialogTag));
            }
            wrapper.scheduleSendRaw();
            return true;
        } catch (Exception e) {
            Via.getPlatform().getLogger().log(Level.FINE, "Failed to open dialog for " + connection.getProtocolInfo().getUsername(), e);
            return false;
        }
    }

    /**
     * Open the dialog for a user connection, automatically determining the phase from the connection state
     *
     * @param connection the user connection
     * @return true if the dialog was sent, false otherwise
     */
    public boolean open(UserConnection connection) {
        return open(connection, connection != null && connection.getProtocolInfo().getClientState() == State.CONFIGURATION);
    }

    @Override
    public boolean open(UUID uuid) {
        UserConnection connection = Via.getManager().getConnectionManager().getServerConnection(uuid);
        return connection != null && open(connection);
    }
}
