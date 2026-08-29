package io.github.projectunified.unidialog.viaversion.input;

import com.viaversion.nbt.tag.CompoundTag;
import com.viaversion.viaversion.libs.mcstructs.text.TextComponent;
import com.viaversion.viaversion.util.SerializerVersion;
import io.github.projectunified.unidialog.core.input.TextInput;
import io.github.projectunified.unidialog.viaversion.ViaDialogTagBuilder;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

/**
 * A ViaVersion-based implementation of {@link TextInput} for text inputs.
 */
public class ViaTextInput extends ViaDialogInput implements TextInput<ViaTextInput> {
    private int width;
    private @Nullable TextComponent label;
    private String initial;
    private int maxLength;
    private @Nullable Integer maxLines;
    private @Nullable Integer height;

    /**
     * Constructor for ViaTextInput
     *
     * @param componentDeserializer the function to deserialize components from strings
     * @param baseSerializer        the serializer of the server version the components are created for
     */
    public ViaTextInput(Function<String, TextComponent> componentDeserializer, SerializerVersion baseSerializer) {
        super(componentDeserializer, baseSerializer);
    }

    @Override
    public ViaTextInput width(int width) {
        this.width = width;
        return this;
    }

    /**
     * Set the label for the input
     *
     * @param label the label
     * @return the input itself for method chaining
     */
    public ViaTextInput label(@Nullable TextComponent label) {
        this.label = label;
        return this;
    }

    @Override
    public ViaTextInput label(@Nullable String label) {
        return label(label == null ? null : getComponentDeserializer().apply(label));
    }

    @Override
    public ViaTextInput initial(String initial) {
        this.initial = initial;
        return this;
    }

    @Override
    public ViaTextInput maxLength(int maxLength) {
        this.maxLength = maxLength;
        return this;
    }

    @Override
    public ViaTextInput maxLines(@Nullable Integer maxLines) {
        this.maxLines = maxLines;
        return this;
    }

    @Override
    public ViaTextInput height(@Nullable Integer height) {
        this.height = height;
        return this;
    }

    @Override
    public void write(CompoundTag tag, SerializerVersion target) {
        tag.putString("type", "minecraft:text");
        tag.putInt("width", width > 0 ? width : DEFAULT_WIDTH);
        ViaDialogTagBuilder.putComponent(tag, "label", label, getBaseSerializer(), target);
        tag.putBoolean("label_visible", label != null);
        tag.putString("initial", initial != null ? initial : "");
        tag.putInt("max_length", maxLength > 0 ? maxLength : DEFAULT_MAX_LENGTH);
        if (maxLines != null || height != null) {
            CompoundTag multiline = new CompoundTag();
            if (maxLines != null) {
                multiline.putInt("max_lines", maxLines);
            }
            if (height != null) {
                multiline.putInt("height", height);
            }
            tag.put("multiline", multiline);
        }
    }
}
