package io.github.projectunified.unidialog.viaversion.body;

import com.viaversion.nbt.tag.CompoundTag;
import com.viaversion.viaversion.util.SerializerVersion;

/**
 * A ViaVersion-based body for a dialog.
 */
public interface ViaDialogBody {
    /**
     * Serialize this body for the given target serializer
     *
     * @param target the serializer of the version the dialog is sent to
     * @return the dialog body tag
     */
    CompoundTag toTag(SerializerVersion target);
}
