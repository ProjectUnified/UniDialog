package io.github.projectunified.unidialog.spigot;

import io.github.projectunified.unidialog.bungeecord.action.BungeeDialogActionBuilder;
import io.github.projectunified.unidialog.bungeecord.body.BungeeDialogBodyBuilder;
import io.github.projectunified.unidialog.bungeecord.dialog.*;
import io.github.projectunified.unidialog.bungeecord.input.BungeeDialogInputBuilder;
import io.github.projectunified.unidialog.core.DialogManager;
import io.github.projectunified.unidialog.core.payload.DialogPayload;
import io.github.projectunified.unidialog.spigot.opener.SpigotDialogOpener;
import io.github.projectunified.unidialog.spigot.payload.SpigotDialogPayload;
import net.md_5.bungee.api.dialog.Dialog;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCustomClickEvent;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

@SuppressWarnings("unchecked")
public class SpigotDialogManager implements DialogManager<Object, BungeeDialogBodyBuilder, BungeeDialogInputBuilder, BungeeDialog<?, ?>, BungeeDialogActionBuilder>, Listener {
    private final Plugin plugin;
    private final String defaultNamespace;
    private final Map<NamespacedKey, Consumer<DialogPayload>> customActions = new HashMap<>();

    public SpigotDialogManager(Plugin plugin, String defaultNamespace) {
        this.plugin = plugin;
        this.defaultNamespace = defaultNamespace;
    }

    public SpigotDialogManager(Plugin plugin) {
        this(plugin, plugin.getName().replace("[^a-zA-Z0-9]", "_").toLowerCase(Locale.ROOT));
    }

    private SpigotDialogOpener getDialogOpener(Dialog dialog) {
        return new SpigotDialogOpener(dialog);
    }

    @Override
    public BungeeConfirmationDialog<SpigotDialogOpener> createConfirmationDialog() {
        return new BungeeConfirmationDialog<>(defaultNamespace, this::getDialogOpener);
    }

    @Override
    public BungeeMultiActionDialog<SpigotDialogOpener> createMultiActionDialog() {
        return new BungeeMultiActionDialog<>(defaultNamespace, this::getDialogOpener);
    }

    @Override
    public BungeeServerLinksDialog<SpigotDialogOpener> createServerLinksDialog() {
        return new BungeeServerLinksDialog<>(defaultNamespace, this::getDialogOpener);
    }

    @Override
    public BungeeNoticeDialog<SpigotDialogOpener> createNoticeDialog() {
        return new BungeeNoticeDialog<>(defaultNamespace, this::getDialogOpener);
    }

    @Override
    public BungeeDialogListDialog<SpigotDialogOpener> createDialogListDialog() {
        return new BungeeDialogListDialog<>(defaultNamespace, this::getDialogOpener);
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
        NamespacedKey key = event.getId();
        Consumer<DialogPayload> action = customActions.get(key);
        if (action == null) return;

        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        action.accept(new SpigotDialogPayload(uuid, event.getData()));
    }

    @Override
    public void registerCustomAction(String id, Consumer<DialogPayload> action) {
        registerCustomAction(defaultNamespace, id, action);
    }

    @Override
    public void registerCustomAction(String namespace, String id, Consumer<DialogPayload> action) {
        customActions.put(new NamespacedKey(namespace, id), action);
    }

    @Override
    public void unregisterCustomAction(String id) {
        unregisterCustomAction(defaultNamespace, id);
    }

    @Override
    public void unregisterCustomAction(String namespace, String id) {
        customActions.remove(new NamespacedKey(namespace, id));
    }

    @Override
    public void unregisterAllCustomActions() {
        customActions.clear();
    }

    @Override
    public boolean clearDialog(UUID uuid) {
        Player player = plugin.getServer().getPlayer(uuid);
        if (player == null) return false;
        player.clearDialog();
        return true;
    }
}
