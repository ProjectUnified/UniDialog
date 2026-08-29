package io.github.projectunified.unidialog.viaversion.payload;

import com.viaversion.nbt.tag.*;
import io.github.projectunified.unidialog.core.payload.DialogPayload;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * A ViaVersion-based implementation of {@link DialogPayload}.
 *
 * @param owner    the UUID of the player that triggered the action
 * @param compound the payload data
 */
public record ViaDialogPayload(UUID owner, CompoundTag compound) implements DialogPayload {
    @Override
    public @Nullable String textValue(String key) {
        Tag tag = compound.get(key);
        return tag instanceof StringTag stringTag ? stringTag.getValue() : null;
    }

    @Override
    public @Nullable Boolean booleanValue(String key) {
        Tag tag = compound.get(key);
        return tag instanceof ByteTag byteTag ? byteTag.asBoolean() : null;
    }

    @Override
    public @Nullable Number numberValue(String key) {
        Tag tag = compound.get(key);
        return tag instanceof NumberTag numberTag ? numberTag.getValue() : null;
    }

    @Override
    public Map<String, String> map() {
        Map<String, String> payload = new HashMap<>();
        for (Map.Entry<String, Tag> entry : compound.entrySet()) {
            String key = entry.getKey();
            String value = switch (entry.getValue()) {
                case NumberTag numberTag -> numberTag.getValue().toString();
                case StringTag stringTag -> stringTag.getValue();
                case Tag tag -> tag.toString();
            };
            payload.put(key, value);
        }
        return payload;
    }
}
