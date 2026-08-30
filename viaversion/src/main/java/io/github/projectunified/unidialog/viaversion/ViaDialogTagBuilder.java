package io.github.projectunified.unidialog.viaversion;

import com.viaversion.nbt.tag.CompoundTag;
import com.viaversion.nbt.tag.ListTag;
import com.viaversion.nbt.tag.Tag;
import com.viaversion.viaversion.api.data.FullMappings;
import com.viaversion.viaversion.api.data.MappingData;
import com.viaversion.viaversion.api.minecraft.data.StructuredDataContainer;
import com.viaversion.viaversion.api.minecraft.data.StructuredDataKey;
import com.viaversion.viaversion.api.minecraft.item.Item;
import com.viaversion.viaversion.api.protocol.version.ProtocolVersion;
import com.viaversion.viaversion.libs.mcstructs.text.TextComponent;
import com.viaversion.viaversion.libs.mcstructs.text.stringformat.StringFormat;
import com.viaversion.viaversion.libs.mcstructs.text.stringformat.handling.ColorHandling;
import com.viaversion.viaversion.libs.mcstructs.text.stringformat.handling.DeserializerUnknownHandling;
import com.viaversion.viaversion.protocols.v1_21_5to1_21_6.Protocol1_21_5To1_21_6;
import com.viaversion.viaversion.util.SerializerVersion;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

/**
 * Utility for building the {@link CompoundTag} of a dialog packet.
 */
public final class ViaDialogTagBuilder {
    private static final String DEFAULT_ITEM_ID = "minecraft:stone";
    /**
     * Each {@link SerializerVersion} mapped to the protocol version at which it became the current
     * serializer, derived from the serializer names
     */
    private static final NavigableMap<Integer, SerializerVersion> SERIALIZERS = buildSerializerMap();

    private ViaDialogTagBuilder() {
    }

    /**
     * Deserialize a legacy formatted string (e.g. {@code "\u00a7aHello"}) into a {@link TextComponent}
     *
     * @param input the legacy string
     * @return the parsed component
     */
    public static TextComponent deserializeLegacy(String input) {
        return StringFormat.vanilla().fromString(input, ColorHandling.RESET, DeserializerUnknownHandling.WHITE);
    }

    /**
     * Map the component from the base serializer to the target serializer and serialize it to a tag
     *
     * @param component the component to serialize
     * @param base      the serializer of the version the component was created for
     * @param target    the serializer of the version the component is sent to
     * @return the text component tag
     */
    public static Tag component(TextComponent component, SerializerVersion base, SerializerVersion target) {
        if (base != target) {
            component = target.toComponent(base.toTag(component));
        }
        return target.toTag(component);
    }

    /**
     * Write a text component tag into {@code target} under {@code key} if {@code component} is not null
     *
     * @param target    the tag to write into
     * @param key       the key to write
     * @param component the component to write, may be null
     * @param base      the serializer of the version the component was created for
     * @param to        the serializer of the version the component is sent to
     */
    public static void putComponent(CompoundTag target, String key, @Nullable TextComponent component, SerializerVersion base, SerializerVersion to) {
        if (component != null) {
            target.put(key, component(component, base, to));
        }
    }

    private static NavigableMap<Integer, SerializerVersion> buildSerializerMap() {
        NavigableMap<Integer, SerializerVersion> map = new TreeMap<>();
        for (SerializerVersion serializer : SerializerVersion.values()) {
            ProtocolVersion start = startVersion(serializer);
            if (start != null) {
                map.put(start.getVersion(), serializer);
            }
        }
        map.putIfAbsent(Integer.MIN_VALUE, SerializerVersion.V1_6);
        return map;
    }

    private static ProtocolVersion startVersion(SerializerVersion serializer) {
        String name = serializer.name().substring(1).replace('_', '.');
        for (ProtocolVersion version : ProtocolVersion.getProtocols()) { // oldest to newest
            if (version.getName().startsWith(name)) {
                return version;
            }
        }
        return null;
    }

    /**
     * Return the {@link SerializerVersion} for the given protocol version
     *
     * @param version the protocol version
     * @return the matching serializer
     */
    public static SerializerVersion serializerFor(ProtocolVersion version) {
        if (version == null) {
            return SerializerVersion.V1_21_6;
        }
        Map.Entry<Integer, SerializerVersion> entry = SERIALIZERS.floorEntry(version.getVersion());
        return entry != null ? entry.getValue() : SerializerVersion.V1_6;
    }

    /**
     * Serialize a {@link Item} to the item tag used by the dialog item body
     *
     * @param item       the item to serialize
     * @param serializer the serializer to serialize the item's components with
     * @return the item tag
     */
    public static CompoundTag item(Item item, SerializerVersion serializer) {
        CompoundTag tag = new CompoundTag();
        tag.putString("id", itemIdentifier(item.identifier()));
        tag.putInt("count", item.amount() > 0 ? item.amount() : 1);
        CompoundTag components = components(item, serializer);
        if (!components.isEmpty()) {
            tag.put("components", components);
        }
        return tag;
    }

    private static CompoundTag components(Item item, SerializerVersion base) {
        CompoundTag components = new CompoundTag();
        final StructuredDataContainer data;
        try {
            data = item.dataContainer();
        } catch (UnsupportedOperationException e) {
            // Legacy items (e.g. DataItem) do not carry a data container
            return components;
        }
        Tag customName = data.get(StructuredDataKey.CUSTOM_NAME);
        if (customName != null) {
            components.put("minecraft:custom_name", customName);
        }
        Tag itemName = data.get(StructuredDataKey.ITEM_NAME);
        if (itemName != null) {
            components.put("minecraft:item_name", itemName);
        }
        Tag[] lore = data.get(StructuredDataKey.LORE);
        if (lore != null && lore.length > 0) {
            ListTag<CompoundTag> loreTag = new ListTag<>(CompoundTag.class);
            for (Tag line : lore) {
                loreTag.add(loreEntry(base.toComponent(line), base));
            }
            components.put("minecraft:lore", loreTag);
        }
        return components;
    }

    /**
     * Wrap a text component as a lore line, mirroring ViaVersion's updateComponentList
     *
     * @param component  the component to wrap
     * @param serializer the serializer to serialize the component with
     * @return the lore entry
     */
    public static CompoundTag loreEntry(TextComponent component, SerializerVersion serializer) {
        CompoundTag entry = new CompoundTag();
        entry.putString("text", "");
        entry.put("extra", new ListTag<>(List.of(serializer.toTag(component))));
        return entry;
    }

    private static String itemIdentifier(int id) {
        MappingData mappingData = Protocol1_21_5To1_21_6.MAPPINGS;
        FullMappings mappings = mappingData.getFullItemMappings();
        if (mappings != null) {
            String identifier = mappings.identifier(id);
            if (identifier != null) {
                return identifier;
            }
        }
        return DEFAULT_ITEM_ID;
    }
}
