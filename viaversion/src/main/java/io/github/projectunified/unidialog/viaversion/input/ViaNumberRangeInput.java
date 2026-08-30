package io.github.projectunified.unidialog.viaversion.input;

import com.viaversion.nbt.tag.CompoundTag;
import com.viaversion.nbt.tag.FloatTag;
import com.viaversion.viaversion.libs.mcstructs.text.TextComponent;
import com.viaversion.viaversion.util.SerializerVersion;
import io.github.projectunified.unidialog.core.input.NumberRangeInput;
import io.github.projectunified.unidialog.viaversion.ViaDialogTagBuilder;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

/**
 * A ViaVersion-based implementation of {@link NumberRangeInput} for number range inputs.
 */
public class ViaNumberRangeInput extends ViaDialogInput implements NumberRangeInput<ViaNumberRangeInput> {
    private int width;
    private TextComponent label;
    private String labelFormat;
    private float start;
    private float end;
    private @Nullable Float initial;
    private @Nullable Float step;

    /**
     * Constructor for ViaNumberRangeInput
     *
     * @param componentDeserializer the function to deserialize components from strings
     */
    public ViaNumberRangeInput(Function<String, TextComponent> componentDeserializer) {
        super(componentDeserializer);
    }

    @Override
    public ViaNumberRangeInput width(int width) {
        this.width = width;
        return this;
    }

    /**
     * Set the label for the input
     *
     * @param label the label
     * @return the input itself for method chaining
     */
    public ViaNumberRangeInput label(TextComponent label) {
        this.label = label;
        return this;
    }

    @Override
    public ViaNumberRangeInput label(String label) {
        return label(getComponentDeserializer().apply(label));
    }

    @Override
    public ViaNumberRangeInput labelFormat(String labelFormat) {
        this.labelFormat = labelFormat;
        return this;
    }

    @Override
    public ViaNumberRangeInput start(float start) {
        this.start = start;
        return this;
    }

    @Override
    public ViaNumberRangeInput end(float end) {
        this.end = end;
        return this;
    }

    @Override
    public ViaNumberRangeInput initial(Float initial) {
        this.initial = initial;
        return this;
    }

    @Override
    public ViaNumberRangeInput step(Float step) {
        this.step = step;
        return this;
    }

    @Override
    public void write(CompoundTag tag, SerializerVersion baseVersion, SerializerVersion targetVersion) {
        tag.putString("type", "minecraft:number_range");
        tag.putInt("width", width > 0 ? width : DEFAULT_WIDTH);
        ViaDialogTagBuilder.putComponent(tag, "label", label, baseVersion, targetVersion);
        tag.putString("label_format", labelFormat != null ? labelFormat : DEFAULT_LABEL_FORMAT);
        tag.putFloat("start", start);
        tag.putFloat("end", end);
        if (initial != null) {
            tag.putFloat("initial", initial);
        }
        if (step != null) {
            tag.put("step", new FloatTag(step));
        }
    }
}
