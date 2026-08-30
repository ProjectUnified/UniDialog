package io.github.projectunified.unidialog.viaversion.payload;

import com.viaversion.nbt.tag.CompoundTag;
import com.viaversion.viaversion.api.minecraft.item.Item;
import com.viaversion.viaversion.util.SerializerVersion;
import io.github.projectunified.unidialog.viaversion.ViaDialogTagBuilder;

/**
 * A dialog item serializable to its item tag for the target version of the dialog.
 */
public interface ViaItem {
    /**
     * Wrap a ViaVersion {@link Item} as a dialog item
     *
     * @param item the item to wrap
     * @return the dialog item
     */
    static ViaItem fromItem(Item item) {
        return target -> ViaDialogTagBuilder.item(item, target);
    }

    /**
     * Serialize this item to its item tag for the given target serializer
     *
     * @param target the serializer of the version the dialog is sent to
     * @return the item tag
     */
    CompoundTag toTag(SerializerVersion target);
}
