package io.github.projectunified.unidialog.viaversion.spigot;

import com.viaversion.nbt.tag.CompoundTag;
import com.viaversion.nbt.tag.Tag;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.data.BiMappings;
import com.viaversion.viaversion.api.data.FullMappings;
import com.viaversion.viaversion.api.minecraft.data.StructuredDataKey;
import com.viaversion.viaversion.api.minecraft.item.Item;
import com.viaversion.viaversion.api.minecraft.item.StructuredItem;
import com.viaversion.viaversion.protocols.v1_12_2to1_13.Protocol1_12_2To1_13;
import com.viaversion.viaversion.protocols.v1_13to1_13_1.Protocol1_13To1_13_1;
import com.viaversion.viaversion.protocols.v1_21_5to1_21_6.Protocol1_21_5To1_21_6;
import com.viaversion.viaversion.util.IdAndData;
import io.github.projectunified.unidialog.viaversion.ViaDialogTagBuilder;
import io.github.projectunified.unidialog.viaversion.payload.ViaItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
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
     * @return the converted item, an air item for null, air or empty stacks
     */
    public static @NotNull ViaItem fromItemStack(@Nullable ItemStack itemStack) {
        if (itemStack == null || itemStack.getType() == Material.AIR || itemStack.getAmount() <= 0) {
            return (baseVersion, targetVersion) -> airTag();
        }
        return ViaItem.fromItem(toItem(itemStack));
    }

    private static CompoundTag airTag() {
        CompoundTag tag = new CompoundTag();
        tag.putString("id", "minecraft:air");
        tag.putInt("count", 1);
        return tag;
    }

    private static Item toItem(ItemStack itemStack) {
        StructuredItem item = new StructuredItem(itemId(itemStack), itemStack.getAmount());
        if (itemStack.hasItemMeta()) {
            ItemMeta meta = itemStack.getItemMeta();
            String displayName = meta.getDisplayName();
            List<String> lore = meta.getLore();
            if (!displayName.isEmpty() || lore != null && !lore.isEmpty()) {
                item.dataContainer().setIdLookup(Via.getManager().getProtocolManager().getProtocol(Protocol1_21_5To1_21_6.class), false);
            }
            if (!displayName.isEmpty()) {
                item.dataContainer().set(StructuredDataKey.CUSTOM_NAME, ViaDialogTagBuilder.componentTag(ViaDialogTagBuilder.deserializeLegacy(displayName)));
            }
            if (lore != null && !lore.isEmpty()) {
                Tag[] loreTags = new Tag[lore.size()];
                for (int i = 0; i < lore.size(); i++) {
                    loreTags[i] = ViaDialogTagBuilder.componentTag(ViaDialogTagBuilder.deserializeLegacy(lore.get(i)));
                }
                item.dataContainer().set(StructuredDataKey.LORE, loreTags);
            }
        }
        return item;
    }

    private static int itemId(ItemStack itemStack) {
        FullMappings mappings = Protocol1_21_5To1_21_6.MAPPINGS.getFullItemMappings();
        // Modern Bukkit (1.13+): Material#getKey
        if (mappings != null) {
            try {
                int id = mappings.id("minecraft:" + itemStack.getType().getKey().getKey());
                if (id != -1) {
                    return id;
                }
            } catch (NoSuchMethodError ignored) {
                // Legacy Bukkit without Material#getKey
            }
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
                if (key != null && mappings != null) {
                    int id = mappings.id(key);
                    if (id != -1) {
                        return id;
                    }
                }
            }
        } catch (ReflectiveOperationException ignored) {
        }
        return mappings != null ? mappings.id("minecraft:stone") : 0;
    }

    private static String itemKey(int id) {
        FullMappings mappings = Protocol1_13To1_13_1.MAPPINGS.getFullItemMappings();
        if (mappings != null) {
            String key = mappings.identifier(id);
            return key;
        }
        return null;
    }
}
