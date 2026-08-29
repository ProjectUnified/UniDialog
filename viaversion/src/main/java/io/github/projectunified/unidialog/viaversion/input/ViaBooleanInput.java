package io.github.projectunified.unidialog.viaversion.input;

import com.viaversion.nbt.tag.CompoundTag;
import com.viaversion.viaversion.libs.mcstructs.text.TextComponent;
import com.viaversion.viaversion.util.SerializerVersion;
import io.github.projectunified.unidialog.core.input.BooleanInput;
import io.github.projectunified.unidialog.viaversion.ViaDialogTagBuilder;

import java.util.function.Function;

/**
 * A ViaVersion-based implementation of {@link BooleanInput} for boolean inputs.
 */
public class ViaBooleanInput extends ViaDialogInput implements BooleanInput<ViaBooleanInput> {
    private TextComponent label;
    private boolean initial;
    private String onTrue;
    private String onFalse;

    /**
     * Constructor for ViaBooleanInput
     *
     * @param componentDeserializer the function to deserialize components from strings
     * @param baseSerializer        the serializer of the server version the components are created for
     */
    public ViaBooleanInput(Function<String, TextComponent> componentDeserializer, SerializerVersion baseSerializer) {
        super(componentDeserializer, baseSerializer);
    }

    /**
     * Set the label for the input
     *
     * @param label the label
     * @return the input itself for method chaining
     */
    public ViaBooleanInput label(TextComponent label) {
        this.label = label;
        return this;
    }

    @Override
    public ViaBooleanInput label(String label) {
        return label(getComponentDeserializer().apply(label));
    }

    @Override
    public ViaBooleanInput initial(boolean initial) {
        this.initial = initial;
        return this;
    }

    @Override
    public ViaBooleanInput onTrue(String onTrue) {
        this.onTrue = onTrue;
        return this;
    }

    @Override
    public ViaBooleanInput onFalse(String onFalse) {
        this.onFalse = onFalse;
        return this;
    }

    @Override
    public void write(CompoundTag tag, SerializerVersion target) {
        tag.putString("type", "minecraft:boolean");
        ViaDialogTagBuilder.putComponent(tag, "label", label, getBaseSerializer(), target);
        tag.putBoolean("initial", initial);
        tag.putString("on_true", onTrue == null ? "true" : onTrue);
        tag.putString("on_false", onFalse == null ? "false" : onFalse);
    }
}
