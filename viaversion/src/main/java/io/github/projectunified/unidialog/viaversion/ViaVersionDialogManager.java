package io.github.projectunified.unidialog.viaversion;

import com.google.common.collect.Range;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.minecraft.item.Item;
import com.viaversion.viaversion.api.protocol.packet.Direction;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import com.viaversion.viaversion.libs.mcstructs.text.TextComponent;
import com.viaversion.viaversion.util.SerializerVersion;
import io.github.projectunified.unidialog.core.DialogManager;
import io.github.projectunified.unidialog.core.payload.DialogPayload;
import io.github.projectunified.unidialog.viaversion.action.ViaDialogActionBuilder;
import io.github.projectunified.unidialog.viaversion.body.ViaDialogBodyBuilder;
import io.github.projectunified.unidialog.viaversion.dialog.*;
import io.github.projectunified.unidialog.viaversion.input.ViaDialogInputBuilder;
import io.github.projectunified.unidialog.viaversion.opener.ViaDialogOpener;

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
public class ViaVersionDialogManager implements DialogManager<Item, ViaDialogBodyBuilder, ViaDialogInputBuilder, ViaDialog<?>, ViaDialogActionBuilder> {
    private final String defaultNamespace;
    private final ProtocolVersion serverVersion;
    private final Function<String, TextComponent> componentDeserializer;
    private final SerializerVersion baseSerializer;
    private final Map<String, Consumer<DialogPayload>> actions = new ConcurrentHashMap<>();
    private ViaDialogProtocol protocol;

    /**
     * Constructor for ViaVersionDialogManager, using 1.21.6 as the base version and
     * {@link ViaDialogTagBuilder#deserializeLegacy} as the component deserializer
     *
     * @param defaultNamespace the default namespace
     */
    public ViaVersionDialogManager(String defaultNamespace) {
        this(defaultNamespace, ProtocolVersion.v1_21_6);
    }

    /**
     * Constructor for ViaVersionDialogManager, using {@link ViaDialogTagBuilder#deserializeLegacy}
     * as the component deserializer
     *
     * @param defaultNamespace the default namespace
     * @param serverVersion    the server protocol version, used as the base version the dialog components are created for
     */
    public ViaVersionDialogManager(String defaultNamespace, ProtocolVersion serverVersion) {
        this(defaultNamespace, serverVersion, ViaDialogTagBuilder::deserializeLegacy);
    }

    /**
     * Constructor for ViaVersionDialogManager
     *
     * @param defaultNamespace      the default namespace
     * @param serverVersion         the server protocol version, used as the base version the dialog components are created for
     * @param componentDeserializer a function to deserialize components from strings
     */
    public ViaVersionDialogManager(String defaultNamespace, ProtocolVersion serverVersion, Function<String, TextComponent> componentDeserializer) {
        this.defaultNamespace = defaultNamespace;
        this.serverVersion = serverVersion;
        this.componentDeserializer = componentDeserializer;
        this.baseSerializer = ViaDialogTagBuilder.serializerFor(serverVersion);
    }

    /**
     * Get the server protocol version used as the base version
     *
     * @return the server protocol version
     */
    public ProtocolVersion getServerVersion() {
        return serverVersion;
    }

    @Override
    public ViaConfirmationDialog createConfirmationDialog() {
        return new ViaConfirmationDialog(defaultNamespace, componentDeserializer, baseSerializer);
    }

    @Override
    public ViaMultiActionDialog createMultiActionDialog() {
        return new ViaMultiActionDialog(defaultNamespace, componentDeserializer, baseSerializer);
    }

    @Override
    public ViaServerLinksDialog createServerLinksDialog() {
        return new ViaServerLinksDialog(defaultNamespace, componentDeserializer, baseSerializer);
    }

    @Override
    public ViaNoticeDialog createNoticeDialog() {
        return new ViaNoticeDialog(defaultNamespace, componentDeserializer, baseSerializer);
    }

    @Override
    public ViaDialogListDialog createDialogListDialog() {
        return new ViaDialogListDialog(defaultNamespace, componentDeserializer, baseSerializer);
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
