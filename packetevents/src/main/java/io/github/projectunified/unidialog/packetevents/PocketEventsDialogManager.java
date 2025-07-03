package io.github.projectunified.unidialog.packetevents;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.PacketListenerCommon;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.item.ItemStack;
import com.github.retrooper.packetevents.protocol.nbt.*;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientCustomClickAction;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerClearDialog;
import io.github.projectunified.unidialog.core.DialogManager;
import io.github.projectunified.unidialog.packetevents.action.PEDialogActionBuilder;
import io.github.projectunified.unidialog.packetevents.body.PEDialogBodyBuilder;
import io.github.projectunified.unidialog.packetevents.dialog.PEConfirmationDialog;
import io.github.projectunified.unidialog.packetevents.dialog.PEMultiActionDialog;
import io.github.projectunified.unidialog.packetevents.dialog.PENoticeDialog;
import io.github.projectunified.unidialog.packetevents.dialog.PEServerLinksDialog;
import io.github.projectunified.unidialog.packetevents.input.PEDialogInputBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiConsumer;

@SuppressWarnings("unchecked")
public abstract class PocketEventsDialogManager implements DialogManager<ItemStack, PEDialogBodyBuilder, PEDialogInputBuilder, PEDialogActionBuilder> {
    private final String defaultNamespace;
    private final Map<ResourceLocation, BiConsumer<UUID, Map<String, String>>> actions = new HashMap<>();
    private PacketListenerCommon packetListener;

    public PocketEventsDialogManager(String defaultNamespace) {
        this.defaultNamespace = defaultNamespace;
    }

    protected abstract @Nullable Object getPlayer(UUID uuid);

    protected abstract UUID getPlayerId(Object player);

    @Override
    public PEConfirmationDialog createConfirmationDialog() {
        return new PEConfirmationDialog(defaultNamespace, this::getPlayer);
    }

    @Override
    public PEMultiActionDialog createMultiActionDialog() {
        return new PEMultiActionDialog(defaultNamespace, this::getPlayer);
    }

    @Override
    public PEServerLinksDialog createServerLinksDialog() {
        return new PEServerLinksDialog(defaultNamespace, this::getPlayer);
    }

    @Override
    public PENoticeDialog createNoticeDialog() {
        return new PENoticeDialog(defaultNamespace, this::getPlayer);
    }

    @Override
    public void register() {
        if (packetListener != null) {
            PacketEvents.getAPI().getEventManager().unregisterListener(packetListener);
        }
        this.packetListener = new PacketListenerAbstract() {
            @Override
            public void onPacketReceive(@NotNull PacketReceiveEvent event) {
                if (event.getPacketType() != PacketType.Play.Client.CUSTOM_CLICK_ACTION) return;

                WrapperPlayClientCustomClickAction packet = new WrapperPlayClientCustomClickAction(event);
                ResourceLocation namespacedId = packet.getId();
                NBT data = packet.getPayload();

                BiConsumer<UUID, Map<String, String>> action = actions.get(namespacedId);
                if (action == null) return;

                UUID uuid = getPlayerId(event.getPlayer());
                Map<String, String> payload = new HashMap<>();
                if (data instanceof NBTCompound nbtCompound) {
                    for (Map.Entry<String, NBT> entry : nbtCompound.getTags().entrySet()) {
                        String key = entry.getKey();
                        String value = switch (entry.getValue()) {
                            case NBTInt nbtInt -> Integer.toString(nbtInt.getAsInt());
                            case NBTLong nbtLong -> Long.toString(nbtLong.getAsLong());
                            case NBTFloat nbtFloat -> Float.toString(nbtFloat.getAsFloat());
                            case NBTDouble nbtDouble -> Double.toString(nbtDouble.getAsDouble());
                            case NBTString nbtString -> nbtString.getValue();
                            case NBTByte nbtByte -> Boolean.toString(nbtByte.getAsBool());
                            case NBT nbt -> nbt.toString(); // Fallback to string representation
                        };
                        payload.put(key, value);
                    }
                }

                action.accept(uuid, payload);
            }
        };
        PacketEvents.getAPI().getEventManager().registerListener(packetListener);
    }

    @Override
    public void unregister() {
        if (packetListener != null) {
            PacketEvents.getAPI().getEventManager().unregisterListener(packetListener);
            packetListener = null;
        }
        unregisterAllCustomActions();
    }

    @Override
    public void registerCustomAction(String id, BiConsumer<UUID, Map<String, String>> action) {
        registerCustomAction(defaultNamespace, id, action);
    }

    @Override
    public void registerCustomAction(String namespace, String id, BiConsumer<UUID, Map<String, String>> action) {
        actions.put(new ResourceLocation(namespace, id), action);
    }

    @Override
    public void unregisterCustomAction(String id) {
        unregisterCustomAction(defaultNamespace, id);
    }

    @Override
    public void unregisterCustomAction(String namespace, String id) {
        actions.remove(new ResourceLocation(namespace, id));
    }

    @Override
    public void unregisterAllCustomActions() {
        actions.clear();
    }

    @Override
    public boolean clearDialog(UUID uuid) {
        Object player = getPlayer(uuid);
        if (player == null) return false;

        WrapperPlayServerClearDialog wrapper = new WrapperPlayServerClearDialog();
        PacketEvents.getAPI().getPlayerManager().sendPacket(player, wrapper);
        return true;
    }
}
