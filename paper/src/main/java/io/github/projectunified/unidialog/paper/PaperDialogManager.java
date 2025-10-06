package io.github.projectunified.unidialog.paper;

import io.github.projectunified.unidialog.core.DialogManager;
import io.github.projectunified.unidialog.core.payload.DialogPayload;
import io.github.projectunified.unidialog.paper.action.PaperDialogActionBuilder;
import io.github.projectunified.unidialog.paper.body.PaperDialogBodyBuilder;
import io.github.projectunified.unidialog.paper.dialog.*;
import io.github.projectunified.unidialog.paper.input.PaperDialogInputBuilder;
import io.github.projectunified.unidialog.paper.payload.PaperDialogPayload;
import io.papermc.paper.connection.PlayerConfigurationConnection;
import io.papermc.paper.connection.PlayerGameConnection;
import io.papermc.paper.event.player.PlayerCustomClickEvent;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

@SuppressWarnings("unchecked")
public class PaperDialogManager implements DialogManager<ItemStack, PaperDialogBodyBuilder, PaperDialogInputBuilder, PaperDialog<?>, PaperDialogActionBuilder>, Listener {
    private final Plugin plugin;
    private final String defaultNamespace;
    private final Function<String, Component> componentDeserializer;
    private final Map<Key, Consumer<DialogPayload>> customActions = new HashMap<>();

    /**
     * Constructor for PaperDialogManager
     *
     * @param plugin                the plugin instance
     * @param defaultNamespace      the default namespace, used by {@link #registerCustomAction(String, BiConsumer)} and {@link io.github.projectunified.unidialog.core.action.DialogActionBuilder#dynamicCustom(String)}
     * @param componentDeserializer a function to deserialize components from strings
     */
    public PaperDialogManager(Plugin plugin, String defaultNamespace, Function<String, Component> componentDeserializer) {
        this.plugin = plugin;
        this.defaultNamespace = defaultNamespace;
        this.componentDeserializer = componentDeserializer;
    }

    /**
     * Constructor for PaperDialogManager.
     * Uses {@link LegacyComponentSerializer#legacySection()} to deserialize components.
     *
     * @param plugin           the plugin instance
     * @param defaultNamespace the default namespace, used by {@link #registerCustomAction(String, BiConsumer)} and {@link io.github.projectunified.unidialog.core.action.DialogActionBuilder#dynamicCustom(String)}
     */
    public PaperDialogManager(Plugin plugin, String defaultNamespace) {
        this(plugin, defaultNamespace, LegacyComponentSerializer.legacySection()::deserialize);
    }

    /**
     * Constructor for PaperDialogManager that uses the plugin's name as the default namespace.
     *
     * @param plugin                the plugin instance
     * @param componentDeserializer a function to deserialize components from strings
     */
    public PaperDialogManager(Plugin plugin, Function<String, Component> componentDeserializer) {
        this(plugin, plugin.getName().replace("[^a-zA-Z0-9]", "_").toLowerCase(Locale.ROOT), componentDeserializer);
    }

    /**
     * Constructor for PaperDialogManager that uses the plugin's name as the default namespace.
     * Uses {@link LegacyComponentSerializer#legacySection()} to deserialize components.
     *
     * @param plugin the plugin instance
     */
    public PaperDialogManager(Plugin plugin) {
        this(plugin, LegacyComponentSerializer.legacySection()::deserialize);
    }

    @Override
    public PaperConfirmationDialog createConfirmationDialog() {
        return new PaperConfirmationDialog(defaultNamespace, componentDeserializer);
    }

    @Override
    public PaperMultiActionDialog createMultiActionDialog() {
        return new PaperMultiActionDialog(defaultNamespace, componentDeserializer);
    }

    @Override
    public PaperServerLinksDialog createServerLinksDialog() {
        return new PaperServerLinksDialog(defaultNamespace, componentDeserializer);
    }

    @Override
    public PaperNoticeDialog createNoticeDialog() {
        return new PaperNoticeDialog(defaultNamespace, componentDeserializer);
    }

    @Override
    public PaperDialogListDialog createDialogListDialog() {
        return new PaperDialogListDialog(defaultNamespace, componentDeserializer);
    }

    @Override
    public void register() {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @Override
    public void unregister() {
        HandlerList.unregisterAll(this);
        unregisterAllCustomActions();
    }

    @EventHandler
    public void onCustomClick(PlayerCustomClickEvent event) {
        Key key = event.getIdentifier();
        Consumer<DialogPayload> action = customActions.get(key);
        if (action == null) return;

        UUID uuid = switch (event.getCommonConnection()) {
            case PlayerGameConnection pgc -> pgc.getPlayer().getUniqueId();
            case PlayerConfigurationConnection pcc -> pcc.getProfile().getUniqueId();
            default -> null;
        };
        if (uuid == null) return;

        action.accept(new PaperDialogPayload(uuid, event.getDialogResponseView()));
    }

    @Override
    public void unregisterCustomAction(String id) {
        unregisterCustomAction(defaultNamespace, id);
    }

    @Override
    public void unregisterCustomAction(String namespace, String id) {
        customActions.remove(Key.key(namespace, id));
    }

    @Override
    public void unregisterAllCustomActions() {
        customActions.clear();
    }

    @Override
    public void registerCustomAction(String id, Consumer<DialogPayload> action) {
        registerCustomAction(defaultNamespace, id, action);
    }

    @Override
    public void registerCustomAction(String namespace, String id, Consumer<DialogPayload> action) {
        customActions.put(Key.key(namespace, id), action);
    }

    @Override
    public boolean clearDialog(UUID uuid) {
        Player player = plugin.getServer().getPlayer(uuid);
        if (player == null) return false;

        try {
            player.closeDialog();
        } catch (Throwable e) {
            player.closeInventory();
        }

        return true;
    }
}
