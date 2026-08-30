package io.github.projectunified.unidialog.viaversion.spigot;

import com.viaversion.nbt.tag.CompoundTag;
import com.viaversion.nbt.tag.ListTag;
import com.viaversion.viaversion.api.data.BiMappings;
import com.viaversion.viaversion.api.data.FullMappings;
import com.viaversion.viaversion.protocols.v1_12_2to1_13.Protocol1_12_2To1_13;
import com.viaversion.viaversion.protocols.v1_13to1_13_1.Protocol1_13To1_13_1;
import com.viaversion.viaversion.protocols.v1_21_5to1_21_6.Protocol1_21_5To1_21_6;
import com.viaversion.viaversion.util.IdAndData;
import com.viaversion.viaversion.util.SerializerVersion;
import io.github.projectunified.unidialog.viaversion.ViaDialogTagBuilder;
import io.github.projectunified.unidialog.viaversion.payload.ViaItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;
import java.util.List;

/**
 * Utility to convert Bukkit {@link ItemStack}s to {@link ViaItem}s.
 */
public final class ViaItemUtil {
    private ViaItemUtil() {
    }

    /**
     * Convert a Bukkit {@link ItemStack} to a {@link ViaItem}
     *
     * @param itemStack the item stack to convert, may be null
     * @return the converted item, an air item for air or empty stacks, or null if the input is null
     */
    public static @Nullable ViaItem fromItemStack(@Nullable ItemStack itemStack) {
        if (itemStack == null) {
            return null;
        }
        if (itemStack.getType() == Material.AIR || itemStack.getAmount() <= 0) {
            return target -> airTag();
        }
        String key = itemKey(itemStack);
        int amount = itemStack.getAmount();
        final String displayName;
        final List<String> lore;
        if (itemStack.hasItemMeta()) {
            ItemMeta meta = itemStack.getItemMeta();
            displayName = meta.getDisplayName();
            lore = meta.getLore();
        } else {
            displayName = null;
            lore = null;
        }
        return target -> itemTag(key, amount, displayName, lore, target);
    }

    private static CompoundTag itemTag(String key, int amount, String displayName, List<String> lore, SerializerVersion target) {
        CompoundTag tag = new CompoundTag();
        tag.putString("id", key);
        tag.putInt("count", amount > 0 ? amount : 1);
        CompoundTag components = new CompoundTag();
        if (displayName != null && !displayName.isEmpty()) {
            components.put("minecraft:custom_name", target.toTag(ViaDialogTagBuilder.deserializeLegacy(displayName)));
        }
        if (lore != null && !lore.isEmpty()) {
            ListTag<CompoundTag> loreTag = new ListTag<>(CompoundTag.class);
            for (String line : lore) {
                loreTag.add(ViaDialogTagBuilder.loreEntry(ViaDialogTagBuilder.deserializeLegacy(line), target));
            }
            components.put("minecraft:lore", loreTag);
        }
        if (!components.isEmpty()) {
            tag.put("components", components);
        }
        return tag;
    }

    private static CompoundTag airTag() {
        CompoundTag tag = new CompoundTag();
        tag.putString("id", "minecraft:air");
        tag.putInt("count", 1);
        return tag;
    }

    private static String itemKey(ItemStack itemStack) {
        // Modern Bukkit (1.13+): Material#getKey
        try {
            String key = "minecraft:" + itemStack.getType().getKey().getKey();
            FullMappings mappings = Protocol1_21_5To1_21_6.MAPPINGS.getFullItemMappings();
            if (mappings != null && mappings.id(key) != -1) {
                return key;
            }
        } catch (NoSuchMethodError ignored) {
            // Legacy Bukkit without Material#getKey
        }
        // Legacy Bukkit: ItemStack#getTypeId and durability, via ViaVersion's 1.12.2 to 1.13 mappings
        try {
            Method getTypeId = ItemStack.class.getMethod("getTypeId");
            int legacyId = (Integer) getTypeId.invoke(itemStack);
            int rawId = IdAndData.toRawData(legacyId, itemStack.getDurability());
            BiMappings itemMappings = Protocol1_12_2To1_13.MAPPINGS.getItemMappings();
            int modernId = itemMappings != null ? itemMappings.getNewId(rawId) : -1;
            if (modernId == -1 && itemMappings != null) {
                modernId = itemMappings.getNewId(IdAndData.removeData(rawId));
            }
            if (modernId != -1) {
                String key = itemKey(modernId);
                if (key != null) {
                    return key;
                }
            }
        } catch (ReflectiveOperationException ignored) {
        }
        return "minecraft:stone";
    }

    private static String itemKey(int id) {
        FullMappings mappings = Protocol1_13To1_13_1.MAPPINGS.getFullItemMappings();
        if (mappings != null) {
            String key = mappings.identifier(id);
            if (key != null) {
                return key;
            }
        }
        return null;
    }
}
