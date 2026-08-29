package io.github.projectunified.unidialog.viaversion.input;

import com.viaversion.nbt.tag.CompoundTag;
import com.viaversion.nbt.tag.ListTag;
import com.viaversion.viaversion.libs.mcstructs.text.TextComponent;
import com.viaversion.viaversion.util.SerializerVersion;
import io.github.projectunified.unidialog.core.input.SingleOptionInput;
import io.github.projectunified.unidialog.viaversion.ViaDialogTagBuilder;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * A ViaVersion-based implementation of {@link SingleOptionInput} for single option inputs.
 */
public class ViaSingleOptionInput extends ViaDialogInput implements SingleOptionInput<ViaSingleOptionInput> {
    private int width;
    private @Nullable TextComponent label;
    private List<Entry> entries;

    /**
     * Constructor for ViaSingleOptionInput
     *
     * @param componentDeserializer the function to deserialize components from strings
     * @param baseSerializer        the serializer of the server version the components are created for
     */
    public ViaSingleOptionInput(Function<String, TextComponent> componentDeserializer, SerializerVersion baseSerializer) {
        super(componentDeserializer, baseSerializer);
    }

    @Override
    public ViaSingleOptionInput width(int width) {
        this.width = width;
        return this;
    }

    /**
     * Set the label for the input
     *
     * @param label the label
     * @return the input itself for method chaining
     */
    public ViaSingleOptionInput label(@Nullable TextComponent label) {
        this.label = label;
        return this;
    }

    @Override
    public ViaSingleOptionInput label(@Nullable String label) {
        return label(label == null ? null : getComponentDeserializer().apply(label));
    }

    /**
     * Add an option to the input
     *
     * @param id        the identifier for the option
     * @param display   the display text for the option
     * @param isDefault whether this option is the default selection
     * @return the input itself for method chaining
     */
    public ViaSingleOptionInput option(String id, TextComponent display, boolean isDefault) {
        if (entries == null) {
            entries = new ArrayList<>();
        }
        entries.add(new Entry(id, display, isDefault));
        return this;
    }

    @Override
    public ViaSingleOptionInput option(String id, String display, boolean isDefault) {
        return option(id, getComponentDeserializer().apply(display), isDefault);
    }

    @Override
    public void write(CompoundTag tag, SerializerVersion target) {
        tag.putString("type", "minecraft:single_option");
        tag.putInt("width", width > 0 ? width : DEFAULT_WIDTH);
        ViaDialogTagBuilder.putComponent(tag, "label", label, getBaseSerializer(), target);
        tag.putBoolean("label_visible", label != null);
        ListTag<CompoundTag> options = new ListTag<>(CompoundTag.class);
        if (entries != null) {
            for (Entry entry : entries) {
                CompoundTag optionTag = new CompoundTag();
                optionTag.putString("id", entry.id);
                optionTag.put("display", ViaDialogTagBuilder.component(entry.display, getBaseSerializer(), target));
                optionTag.putBoolean("initial", entry.isDefault);
                options.add(optionTag);
            }
        }
        tag.put("options", options);
    }

    private record Entry(String id, TextComponent display, boolean isDefault) {
    }
}
