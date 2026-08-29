package io.github.projectunified.unidialog.viaversion;

import com.viaversion.nbt.tag.CompoundTag;
import com.viaversion.nbt.tag.Tag;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.api.protocol.AbstractProtocol;
import com.viaversion.viaversion.api.protocol.Protocol;
import com.viaversion.viaversion.api.protocol.ProtocolPathEntry;
import com.viaversion.viaversion.api.protocol.packet.PacketWrapper;
import com.viaversion.viaversion.api.protocol.packet.ServerboundPacketType;
import com.viaversion.viaversion.api.protocol.packet.State;
import com.viaversion.viaversion.api.protocol.packet.provider.PacketTypeMap;
import com.viaversion.viaversion.api.protocol.packet.provider.PacketTypesProvider;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import com.viaversion.viaversion.api.type.Types;
import com.viaversion.viaversion.exception.InformativeException;
import com.viaversion.viaversion.protocols.base.packet.BaseClientboundPacket;
import com.viaversion.viaversion.protocols.base.packet.BasePacketTypesProvider;
import com.viaversion.viaversion.protocols.base.packet.BaseServerboundPacket;
import com.viaversion.viaversion.protocols.v1_21_5to1_21_6.Protocol1_21_5To1_21_6;
import io.github.projectunified.unidialog.core.payload.DialogPayload;
import io.github.projectunified.unidialog.viaversion.payload.ViaDialogPayload;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

/**
 * Serverbound base protocol intercepting {@code CUSTOM_CLICK_ACTION} packets from 1.21.6+ clients.
 */
public class ViaDialogProtocol extends AbstractProtocol<BaseClientboundPacket, BaseClientboundPacket, BaseServerboundPacket, BaseServerboundPacket> {

    private final Map<String, Consumer<DialogPayload>> actions;

    /**
     * Create the protocol
     *
     * @param actions the custom action registry, keyed by {@code namespace:id}
     */
    public ViaDialogProtocol(Map<String, Consumer<DialogPayload>> actions) {
        super(null, null, null, null);
        this.actions = actions;
    }

    @Override
    protected void registerPackets() {
        // Register CUSTOM_CLICK_ACTION for every client version's own packet id, so packet id
        // changes in newer versions are picked up automatically
        for (Protocol<?, ?, ?, ?> protocol : Via.getManager().getProtocolManager().getProtocols()) {
            registerCustomClick(protocol.getPacketTypesProvider(), State.PLAY);
            registerCustomClick(protocol.getPacketTypesProvider(), State.CONFIGURATION);
        }
    }

    private void registerCustomClick(PacketTypesProvider<?, ?, ?, ?> provider, State state) {
        ServerboundPacketType type = provider.unmappedServerboundType(state, "CUSTOM_CLICK_ACTION");
        if (type != null) {
            registerServerbound(state, type.getId(), type.getId(), this::handleCustomClickAction, true);
        }
    }

    @Override
    public boolean isBaseProtocol() {
        return true;
    }

    @Override
    protected PacketTypesProvider<BaseClientboundPacket, BaseClientboundPacket, BaseServerboundPacket, BaseServerboundPacket> createPacketTypesProvider() {
        return BasePacketTypesProvider.INSTANCE;
    }

    private void handleCustomClickAction(PacketWrapper wrapper) throws InformativeException {
        // A packet id may be CUSTOM_CLICK_ACTION for some client versions and a different packet
        // for others; verify the received id against the client version's own packet types
        ProtocolVersion clientVersion = wrapper.user().getProtocolInfo().protocolVersion();
        State state = wrapper.user().getProtocolInfo().getClientState();
        if (clientVersion == null || !isCustomClickAction(wrapper.user(), clientVersion, state, wrapper.getId())) {
            return;
        }

        String actionId = wrapper.read(Types.STRING);
        Consumer<DialogPayload> action = actions.get(actionId);
        if (action == null) {
            // Not one of our actions - let the packet continue untouched
            wrapper.resetReader();
            return;
        }

        Tag payloadTag = wrapper.read(Types.CUSTOM_CLICK_ACTION_TAG);
        CompoundTag compound = payloadTag instanceof CompoundTag compoundTag ? compoundTag : new CompoundTag();
        wrapper.cancel(); // We consumed the packet; the server has no matching dialog

        UserConnection connection = wrapper.user();
        UUID uuid = connection.getProtocolInfo().getUuid();
        action.accept(new ViaDialogPayload(uuid, compound));
    }

    private static boolean isCustomClickAction(UserConnection connection, ProtocolVersion clientVersion, State state, int id) {
        List<ProtocolPathEntry> path = Via.getManager().getProtocolManager().getProtocolPath(clientVersion, ProtocolVersion.v1_21_6);
        if (path != null && !path.isEmpty()) {
            return isCustomClickAction(path.get(0).protocol(), state, id);
        }
        // The client version equals the dialog base version
        Protocol<?, ?, ?, ?> base = Via.getManager().getProtocolManager().getProtocol(Protocol1_21_5To1_21_6.class);
        return base != null && isCustomClickAction(base, state, id);
    }

    private static boolean isCustomClickAction(Protocol<?, ?, ?, ?> protocol, State state, int id) {
        PacketTypeMap<? extends ServerboundPacketType> types = protocol.getPacketTypesProvider().unmappedServerboundPacketTypes().get(state);
        if (types == null) {
            return false;
        }
        ServerboundPacketType type = types.typeById(id);
        return type != null && type.getName().equals("CUSTOM_CLICK_ACTION");
    }
}
