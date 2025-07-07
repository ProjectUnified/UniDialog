package io.github.projectunified.unidialog.spigot;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.github.projectunified.unidialog.core.DialogManager;
import io.github.projectunified.unidialog.spigot.action.SpigotDialogActionBuilder;
import io.github.projectunified.unidialog.spigot.body.SpigotDialogBodyBuilder;
import io.github.projectunified.unidialog.spigot.dialog.*;
import io.github.projectunified.unidialog.spigot.input.SpigotDialogInputBuilder;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCustomClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiConsumer;

@SuppressWarnings("unchecked")
public class SpigotDialogManager implements DialogManager<ItemStack, SpigotDialogBodyBuilder, SpigotDialogInputBuilder, SpigotDialog<?>, SpigotDialogActionBuilder>, Listener {
    private final Plugin plugin;
    private final String defaultNamespace;
    private final Map<NamespacedKey, BiConsumer<UUID, Map<String, String>>> customActions = new HashMap<>();

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
        unregisterAllCustomActions();
    }

    @EventHandler
    public void onCustomClick(PlayerCustomClickEvent event) {
        NamespacedKey key = event.getId();
        BiConsumer<UUID, Map<String, String>> action = customActions.get(key);
        if (action == null) return;

        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();

        Map<String, String> data = new HashMap<>();
        JsonElement jsonData = event.getData();
        if (jsonData != null && jsonData.isJsonObject()) {
            JsonObject jsonObject = jsonData.getAsJsonObject();
            for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
                data.put(entry.getKey(), entry.getValue().getAsString());
            }
        }

        action.accept(uuid, data);
    }

    @Override
    public void registerCustomAction(String id, BiConsumer<UUID, Map<String, String>> action) {
        registerCustomAction(defaultNamespace, id, action);
    }

    @Override
    public void registerCustomAction(String namespace, String id, BiConsumer<UUID, Map<String, String>> action) {
        NamespacedKey key = new NamespacedKey(namespace, id);
        customActions.put(key, action);
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @Override
    public void unregisterCustomAction(String id) {
        unregisterCustomAction(defaultNamespace, id);
    }

    @Override
    public void unregisterCustomAction(String namespace, String id) {
        NamespacedKey key = new NamespacedKey(namespace, id);
        customActions.remove(key);
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
