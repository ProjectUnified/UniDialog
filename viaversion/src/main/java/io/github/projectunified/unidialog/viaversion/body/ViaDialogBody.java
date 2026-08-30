package io.github.projectunified.unidialog.viaversion.body;

import com.viaversion.nbt.tag.CompoundTag;
import com.viaversion.viaversion.util.SerializerVersion;

/**
 * A ViaVersion-based body for a dialog.
 */
public interface ViaDialogBody {
    /**
     * Serialize this body for the given base and target serializers
     *
     * @param baseVersion   the serializer of the server version the dialog is created for
     * @param targetVersion the serializer of the version the dialog is sent to
     * @return the dialog body tag
     */
    CompoundTag toTag(SerializerVersion baseVersion, SerializerVersion targetVersion);
}
