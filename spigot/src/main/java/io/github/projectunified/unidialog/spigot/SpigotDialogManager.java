package io.github.projectunified.unidialog.spigot;

import io.github.projectunified.unidialog.core.DialogManager;
import io.github.projectunified.unidialog.core.dialog.*;
import io.github.projectunified.unidialog.spigot.action.SpigotDialogActionBuilder;
import io.github.projectunified.unidialog.spigot.body.SpigotDialogBodyBuilder;
import io.github.projectunified.unidialog.spigot.dialog.*;
import io.github.projectunified.unidialog.spigot.input.SpigotDialogInputBuilder;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCustomClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiConsumer;

@SuppressWarnings("unchecked")
public class SpigotDialogManager implements DialogManager<ItemStack, SpigotDialogBodyBuilder, SpigotDialogInputBuilder, SpigotDialog<?>, SpigotDialogActionBuilder>, Listener {
    private final Plugin plugin;
    private final String defaultNamespace;

    public SpigotDialogManager(Plugin plugin) {
        this.plugin = plugin;
        this.defaultNamespace = plugin.getName().replace("[^a-zA-Z0-9]", "_").toLowerCase(Locale.ROOT);
    }

    @Override
    public SpigotConfirmationDialog createConfirmationDialog() {
        return new SpigotConfirmationDialog(defaultNamespace);
    }

    @Override
    public SpigotMultiActionDialog createMultiActionDialog() {
        return new SpigotMultiActionDialog(defaultNamespace);
    }

    @Override
    public SpigotServerLinksDialog createServerLinksDialog() {
        return new SpigotServerLinksDialog(defaultNamespace);
    }

    @Override
    public SpigotNoticeDialog createNoticeDialog() {
        return new SpigotNoticeDialog(defaultNamespace);
    }

    @Override
    public SpigotDialogListDialog createDialogListDialog() {
        return new SpigotDialogListDialog(defaultNamespace);
    }

    @Override
    public void register() {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @Override
    public void unregister() {
        HandlerList.unregisterAll(this);
    }

    @EventHandler
    public void onCustomClick(PlayerCustomClickEvent event) {

    }

    @Override
    public void registerCustomAction(String id, BiConsumer<UUID, Map<String, String>> action) {

    }

    @Override
    public void registerCustomAction(String namespace, String id, BiConsumer<UUID, Map<String, String>> action) {

    }

    @Override
    public void unregisterCustomAction(String id) {

    }

    @Override
    public void unregisterCustomAction(String namespace, String id) {

    }

    @Override
    public void unregisterAllCustomActions() {

    }

    @Override
    public boolean clearDialog(UUID uuid) {
        return false;
    }
}
