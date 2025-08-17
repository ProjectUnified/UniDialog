package io.github.projectunified.unidialog.spigot;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.github.projectunified.unidialog.bungeecord.BungeeDialogManager;
import io.github.projectunified.unidialog.spigot.opener.SpigotDialogOpener;
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
import java.util.function.BiConsumer;

public class SpigotDialogManager implements BungeeDialogManager, Listener {
    private final Plugin plugin;
    private final String defaultNamespace;
    private final Map<NamespacedKey, BiConsumer<UUID, Map<String, String>>> customActions = new HashMap<>();

    public SpigotDialogManager(Plugin plugin, String defaultNamespace) {
        this.plugin = plugin;
        this.defaultNamespace = defaultNamespace;
    }

    public SpigotDialogManager(Plugin plugin) {
        this(plugin, plugin.getName().replace("[^a-zA-Z0-9]", "_").toLowerCase(Locale.ROOT));
    }

    @Override
    public String getDefaultNamespace() {
        return defaultNamespace;
    }

    @Override
    public SpigotDialogOpener getDialogOpener(Dialog dialog) {
        return new SpigotDialogOpener(dialog);
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
