package io.github.projectunified.unidialog.viaversion;

import com.google.common.collect.Range;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.connection.ProtocolInfo;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.protocol.packet.Direction;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import com.viaversion.viaversion.libs.mcstructs.text.TextComponent;
import io.github.projectunified.unidialog.core.DialogManager;
import io.github.projectunified.unidialog.core.payload.DialogPayload;
import io.github.projectunified.unidialog.viaversion.action.ViaDialogActionBuilder;
import io.github.projectunified.unidialog.viaversion.body.ViaDialogBodyBuilder;
import io.github.projectunified.unidialog.viaversion.dialog.*;
import io.github.projectunified.unidialog.viaversion.input.ViaDialogInputBuilder;
import io.github.projectunified.unidialog.viaversion.opener.ViaDialogOpener;
import io.github.projectunified.unidialog.viaversion.payload.ViaItem;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.logging.Level;

/**
 * A ViaVersion-based implementation of {@link DialogManager} that manages dialogs for players.
 * It provides dialog creation, custom action registration, and dialog clearing.
 */
@SuppressWarnings("unchecked")
public class ViaVersionDialogManager implements DialogManager<ViaItem, ViaDialogBodyBuilder, ViaDialogInputBuilder, ViaDialog<?>, ViaDialogActionBuilder> {
    private final String defaultNamespace;
    private final Function<String, TextComponent> componentDeserializer;
    private final Map<String, Consumer<DialogPayload>> actions = new ConcurrentHashMap<>();
    private ViaDialogProtocol protocol;

    /**
     * Constructor for ViaVersionDialogManager, using {@link ViaDialogTagBuilder#deserializeLegacy}
     * as the component deserializer
     *
     * @param defaultNamespace the default namespace
     */
    public ViaVersionDialogManager(String defaultNamespace) {
        this(defaultNamespace, ViaDialogTagBuilder::deserializeLegacy);
    }

    /**
     * Constructor for ViaVersionDialogManager
     *
     * @param defaultNamespace      the default namespace
     * @param componentDeserializer a function to deserialize components from strings
     */
    public ViaVersionDialogManager(String defaultNamespace, Function<String, TextComponent> componentDeserializer) {
        this.defaultNamespace = defaultNamespace;
        this.componentDeserializer = componentDeserializer;
    }

    /**
     * Check if a user connection supports dialogs
     *
     * @param connection the user connection, may be null
     * @return true if the connection supports dialogs
     */
    public static boolean supportsDialog(@Nullable UserConnection connection) {
        if (!Via.isLoaded() || connection == null) {
            return false;
        }
        ProtocolInfo protocolInfo = connection.getProtocolInfo();
        ProtocolVersion clientVersion = protocolInfo != null ? protocolInfo.protocolVersion() : null;
        return clientVersion != null && !clientVersion.olderThan(ProtocolVersion.v1_21_6);
    }

    /**
     * Check if a player supports dialogs
     *
     * @param uuid the player's UUID, may be null
     * @return true if the player supports dialogs
     */
    public static boolean supportsDialog(@Nullable UUID uuid) {
        if (!Via.isLoaded() || uuid == null) {
            return false;
        }
        return supportsDialog(Via.getManager().getConnectionManager().getServerConnection(uuid));
    }

    @Override
    public ViaConfirmationDialog createConfirmationDialog() {
        return new ViaConfirmationDialog(defaultNamespace, componentDeserializer);
    }

    @Override
    public ViaMultiActionDialog createMultiActionDialog() {
        return new ViaMultiActionDialog(defaultNamespace, componentDeserializer);
    }

    @Override
    public ViaServerLinksDialog createServerLinksDialog() {
        return new ViaServerLinksDialog(defaultNamespace, componentDeserializer);
    }

    @Override
    public ViaNoticeDialog createNoticeDialog() {
        return new ViaNoticeDialog(defaultNamespace, componentDeserializer);
    }

    @Override
    public ViaDialogListDialog createDialogListDialog() {
        return new ViaDialogListDialog(defaultNamespace, componentDeserializer);
    }

    @Override
    public void register() {
        if (protocol != null) {
            return; // Already registered
        }
        if (!Via.isLoaded()) {
            Via.getPlatform().getLogger().log(Level.WARNING, "ViaVersion is not loaded; custom dialog actions will not be intercepted. "
                    + "Make sure ViaVersion is installed and loaded before registering the dialog manager.");
            return;
        }
        protocol = new ViaDialogProtocol(actions);
        Via.getManager().getProtocolManager().registerBaseProtocol(
                Direction.SERVERBOUND,
                protocol,
                Range.atLeast(ProtocolVersion.v1_21_6)
        );
    }

    @Override
    public void unregister() {
        // The base protocol cannot be unregistered; make its handler a no-op instead
        actions.clear();
    }

    @Override
    public void registerCustomAction(String id, Consumer<DialogPayload> action) {
        registerCustomAction(defaultNamespace, id, action);
    }

    @Override
    public void registerCustomAction(String namespace, String id, Consumer<DialogPayload> action) {
        actions.put(namespace + ":" + id, action);
    }

    @Override
    public void unregisterCustomAction(String id) {
        unregisterCustomAction(defaultNamespace, id);
    }

    @Override
    public void unregisterCustomAction(String namespace, String id) {
        actions.remove(namespace + ":" + id);
    }

    @Override
    public void unregisterAllCustomActions() {
        actions.clear();
    }

    /**
     * Clear the dialog for a user connection, optionally specifying if it is a configuration dialog.
     *
     * @param connection    the user connection to clear the dialog for
     * @param configuration if true, clears as a configuration dialog; if false, clears as a play dialog
     * @return true if the clear packet was sent, false otherwise
     */
    public boolean clearDialog(UserConnection connection, boolean configuration) {
        return ViaDialogOpener.clearDialog(connection, configuration);
    }

    /**
     * Clear the dialog for a user connection, automatically determining if it is a configuration dialog.
     *
     * @param connection the user connection to clear the dialog for
     * @return true if the clear packet was sent, false otherwise
     */
    public boolean clearDialog(UserConnection connection) {
        return ViaDialogOpener.clearDialog(connection);
    }

    @Override
    public boolean clearDialog(UUID uuid) {
        UserConnection connection = Via.getManager().getConnectionManager().getServerConnection(uuid);
        return connection != null && clearDialog(connection);
    }
}
