package io.github.projectunified.unidialog.viaversion.input;

import com.viaversion.nbt.tag.CompoundTag;
import com.viaversion.viaversion.libs.mcstructs.text.TextComponent;
import com.viaversion.viaversion.util.SerializerVersion;

import java.util.function.Function;

/**
 * A ViaVersion-based base class for dialog inputs.
 */
public abstract class ViaDialogInput {
    private final Function<String, TextComponent> componentDeserializer;
    private final SerializerVersion baseSerializer;

    /**
     * Constructor for ViaDialogInput
     *
     * @param componentDeserializer the function to deserialize components from strings
     * @param baseSerializer        the serializer of the server version the components are created for
     */
    protected ViaDialogInput(Function<String, TextComponent> componentDeserializer, SerializerVersion baseSerializer) {
        this.componentDeserializer = componentDeserializer;
        this.baseSerializer = baseSerializer;
    }

    /**
     * Write the type-specific fields of this input into the given tag
     *
     * @param tag    the tag to write into
     * @param target the serializer of the version the dialog is sent to
     */
    public abstract void write(CompoundTag tag, SerializerVersion target);

    /**
     * Get the component deserializer
     *
     * @return the component deserializer
     */
    public Function<String, TextComponent> getComponentDeserializer() {
        return componentDeserializer;
    }

    /**
     * Get the base serializer
     *
     * @return the base serializer
     */
    public SerializerVersion getBaseSerializer() {
        return baseSerializer;
    }
}
