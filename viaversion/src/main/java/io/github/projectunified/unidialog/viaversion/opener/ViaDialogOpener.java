package io.github.projectunified.unidialog.viaversion.opener;

import com.viaversion.nbt.tag.CompoundTag;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.connection.ProtocolInfo;
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
import com.viaversion.viaversion.exception.InformativeException;
import com.viaversion.viaversion.protocols.v1_21_5to1_21_6.Protocol1_21_5To1_21_6;
import io.github.projectunified.unidialog.core.opener.DialogOpener;
import io.github.projectunified.unidialog.viaversion.dialog.ViaDialog;

import java.util.List;
import java.util.Objects;
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
        Objects.requireNonNull(connection, "connection");
        if (!Via.isLoaded()) {
            return false;
        }
        ProtocolVersion clientVersion = connection.getProtocolInfo().protocolVersion();
        if (clientVersion == null) {
            throw new IllegalStateException("Client protocol version of the connection is unknown");
        }
        if (clientVersion.olderThan(ProtocolVersion.v1_21_6)) {
            return false;
        }
        if (!supportedByRuntime(clientVersion)) {
            Via.getPlatform().getLogger().log(Level.WARNING, "Cannot clear dialog for client {0}: the version is not supported by the running ViaVersion {1}",
                    new Object[]{clientVersion, Via.getManager().getPlatform().getPluginVersion()});
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
            Via.getPlatform().getLogger().log(Level.FINE, "Failed to clear dialog for {0}",
                    new Object[]{connection.getProtocolInfo().getUsername(), e});
            return false;
        }
    }

    private static boolean supportedByRuntime(ProtocolVersion version) {
        // The client's version has a protocol path to the dialog base version, so its packet ids are known
        return version.equals(ProtocolVersion.v1_21_6)
                || Via.getManager().getProtocolManager().getProtocolPath(version, ProtocolVersion.v1_21_6) != null;
    }

    private static String newestPipelineProtocol(UserConnection connection) {
        List<Protocol> pipes = connection.getProtocolInfo().getPipeline().pipes();
        return pipes.isEmpty() ? "none" : pipes.get(pipes.size() - 1).getClass().getSimpleName();
    }

    /**
     * Clear the dialog for a user connection, automatically determining the phase from the connection state
     *
     * @param connection the user connection
     * @return true if the clear packet was sent, false otherwise
     */
    public static boolean clearDialog(UserConnection connection) {
        return clearDialog(Objects.requireNonNull(connection, "connection"),
                connection.getProtocolInfo().getClientState() == State.CONFIGURATION);
    }

    private static ClientboundPacketType clientboundType(ProtocolVersion targetVersion, State state, String name) {
        // Resolve the packet type from the target version's own protocol path, so the packet id
        // matches what the client of that version expects
        List<ProtocolPathEntry> path = Via.getManager().getProtocolManager().getProtocolPath(targetVersion, ProtocolVersion.v1_21_6);
        if (path != null && !path.isEmpty()) {
            ClientboundPacketType type = clientboundType(path.getFirst().protocol(), state, name);
            if (type != null) {
                return type;
            }
        }
        // The target version equals the dialog base version
        if (targetVersion.equals(ProtocolVersion.v1_21_6)) {
            Protocol<?, ?, ?, ?> base = Via.getManager().getProtocolManager().getProtocol(Protocol1_21_5To1_21_6.class);
            return base != null ? clientboundType(base, state, name) : null;
        }
        return null;
    }

    private static ClientboundPacketType clientboundType(Protocol<?, ?, ?, ?> protocol, State state, String name) {
        PacketTypeMap<? extends ClientboundPacketType> types = protocol.getPacketTypesProvider().mappedClientboundPacketTypes().get(state);
        return types == null ? null : types.typeByName(name);
    }

    private static void writeDialogTag(PacketWrapper wrapper, boolean configuration, CompoundTag dialogTag) throws InformativeException {
        if (configuration) {
            wrapper.write(Types.TRUSTED_COMPOUND_TAG, dialogTag);
        } else {
            wrapper.write(Types.TRUSTED_COMPOUND_TAG_HOLDER, Holder.of(dialogTag));
        }
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
     * Open the dialog for a user connection, using the given base and target versions
     *
     * @param connection    the user connection
     * @param configuration if true, sends the configuration-phase dialog; if false, the play-phase dialog
     * @param baseVersion   the protocol version of the server the dialog is created for
     * @param targetVersion the protocol version of the player the dialog is sent to
     * @return true if the dialog was sent, false otherwise
     */
    public boolean open(UserConnection connection, boolean configuration, ProtocolVersion baseVersion, ProtocolVersion targetVersion) {
        Objects.requireNonNull(connection, "connection");
        Objects.requireNonNull(baseVersion, "baseVersion");
        Objects.requireNonNull(targetVersion, "targetVersion");
        if (!Via.isLoaded()) {
            return false;
        }
        if (targetVersion.olderThan(ProtocolVersion.v1_21_6)) {
            Via.getPlatform().getLogger().log(Level.FINE, "Cannot open dialog for client {0}: dialogs require client version 1.21.6+",
                    new Object[]{targetVersion});
            return false;
        }
        if (!supportedByRuntime(targetVersion)) {
            Via.getPlatform().getLogger().log(Level.WARNING, "Cannot open dialog for client {0}: the version is not supported by the running ViaVersion {1} "
                            + "(newest protocol in the pipeline: {2}); the dialog packet id would be wrong. Update ViaVersion to a build supporting {0}",
                    new Object[]{targetVersion, Via.getManager().getPlatform().getPluginVersion(), newestPipelineProtocol(connection)});
            return false;
        }
        State state = configuration ? State.CONFIGURATION : State.PLAY;
        ClientboundPacketType packetType = clientboundType(targetVersion, state, "SHOW_DIALOG");
        if (packetType == null) {
            Via.getPlatform().getLogger().log(Level.WARNING, "No SHOW_DIALOG packet type found for {0} in the protocol path of {1}",
                    new Object[]{targetVersion, connection.getProtocolInfo().getUsername()});
            return false;
        }
        try {
            // Build the payload with the components mapped to the player's target version
            CompoundTag dialogTag = dialog.getDialogTag(baseVersion, targetVersion);
            PacketWrapper wrapper = PacketWrapper.create(packetType, connection);
            writeDialogTag(wrapper, configuration, dialogTag);
            wrapper.scheduleSendRaw();
            return true;
        } catch (Exception e) {
            Via.getPlatform().getLogger().log(Level.WARNING, "Failed to open dialog for {0} (base {1}, target {2})",
                    new Object[]{connection.getProtocolInfo().getUsername(), baseVersion, targetVersion, e});
            return false;
        }
    }

    /**
     * Open the dialog for a user connection, using the given base and target versions and
     * automatically determining the phase from the connection state
     *
     * @param connection    the user connection
     * @param baseVersion   the protocol version of the server the dialog is created for
     * @param targetVersion the protocol version of the player the dialog is sent to
     * @return true if the dialog was sent, false otherwise
     */
    public boolean open(UserConnection connection, ProtocolVersion baseVersion, ProtocolVersion targetVersion) {
        return open(Objects.requireNonNull(connection, "connection"),
                connection.getProtocolInfo().getClientState() == State.CONFIGURATION, baseVersion, targetVersion);
    }

    /**
     * Open the dialog for the given player, using the given base and target versions
     *
     * @param uuid          the UUID of the player to open the dialog for
     * @param baseVersion   the protocol version of the server the dialog is created for
     * @param targetVersion the protocol version of the player the dialog is sent to
     * @return true if the dialog was sent, false otherwise
     */
    public boolean open(UUID uuid, ProtocolVersion baseVersion, ProtocolVersion targetVersion) {
        UserConnection connection = Via.getManager().getConnectionManager().getServerConnection(uuid);
        return connection != null && open(connection, baseVersion, targetVersion);
    }

    /**
     * Open the dialog for a user connection, automatically determining the phase from the connection state
     *
     * @param connection the user connection
     * @return true if the dialog was sent, false otherwise
     */
    public boolean open(UserConnection connection) {
        ProtocolInfo protocolInfo = Objects.requireNonNull(connection, "connection").getProtocolInfo();
        ProtocolVersion baseVersion = protocolInfo.serverProtocolVersion();
        if (!baseVersion.isKnown()) {
            throw new IllegalStateException("Server protocol version of the connection is unknown");
        }
        ProtocolVersion targetVersion = protocolInfo.protocolVersion();
        if (targetVersion == null) {
            throw new IllegalStateException("Client protocol version of the connection is unknown");
        }
        return open(connection, baseVersion, targetVersion);
    }

    @Override
    public boolean open(UUID uuid) {
        UserConnection connection = Via.getManager().getConnectionManager().getServerConnection(uuid);
        return connection != null && open(connection);
    }
}
