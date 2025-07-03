package io.github.projectunified.unidialog.packetevents.input;

import com.github.retrooper.packetevents.protocol.dialog.input.InputControl;
import com.github.retrooper.packetevents.protocol.dialog.input.TextInputControl;
import io.github.projectunified.unidialog.core.input.TextInput;
import io.github.retrooper.packetevents.adventure.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;

public class PETextInput implements TextInput<PETextInput>, PEDialogInput {
    private int width;
    private @Nullable Component label;
    private String initial;
    private int maxLength;
    private Integer maxLines;
    private Integer height;

    @Override
    public PETextInput width(int width) {
        this.width = width;
        return this;
    }

    public PETextInput label(@Nullable Component label) {
        this.label = label;
        return this;
    }

    @Override
    public PETextInput label(@Nullable String label) {
        if (label == null) {
            this.label = null;
            return this;
        }
        return label(LegacyComponentSerializer.legacySection().deserialize(label));
    }

    @Override
    public PETextInput initial(String initial) {
        this.initial = initial;
        return this;
    }

    @Override
    public PETextInput maxLength(int maxLength) {
        this.maxLength = maxLength;
        return this;
    }

    @Override
    public PETextInput maxLines(@Nullable Integer maxLines) {
        this.maxLines = maxLines;
        return this;
    }

    @Override
    public PETextInput height(@Nullable Integer height) {
        this.height = height;
        return this;
    }

    @Override
    public InputControl getInput() {
        return new TextInputControl(
                width > 0 ? width : 200,
                label != null ? label : Component.empty(),
                label != null,
                initial != null ? initial : "",
                maxLength > 0 ? maxLength : 32,
                new TextInputControl.MultilineOptions(maxLines, height)
        );
    }
}
