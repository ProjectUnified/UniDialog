package io.github.projectunified.unidialog.packetevents.payload;

import com.github.retrooper.packetevents.protocol.nbt.*;
import io.github.projectunified.unidialog.core.payload.DialogPayload;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public record PEDialogPayload(UUID owner, NBTCompound compound) implements DialogPayload {
    @Override
    public @Nullable String textValue(String key) {
        return compound.getStringTagValueOrNull(key);
    }

    @Override
    public @Nullable Boolean booleanValue(String key) {
        NBTByte nbt = compound.getTagOfTypeOrNull(key, NBTByte.class);
        return nbt == null ? null : nbt.getAsBool();
    }

    @Override
    public @Nullable Number numberValue(String key) {
        NBTNumber nbt = compound.getTagOfTypeOrNull(key, NBTNumber.class);
        return nbt == null ? null : nbt.getAsNumber();
    }

    @Override
    public Map<String, String> map() {
        Map<String, String> payload = new HashMap<>();
        for (Map.Entry<String, NBT> entry : compound.getTags().entrySet()) {
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
        return payload;
    }
}
