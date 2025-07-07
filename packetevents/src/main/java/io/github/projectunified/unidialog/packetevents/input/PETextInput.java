package io.github.projectunified.unidialog.packetevents.input;

import com.github.retrooper.packetevents.protocol.dialog.input.InputControl;
import com.github.retrooper.packetevents.protocol.dialog.input.TextInputControl;
import io.github.projectunified.unidialog.adventure.input.AdventureTextInput;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public class PETextInput extends PEDialogInput implements AdventureTextInput<PETextInput> {
    private int width;
    private @Nullable Component label;
    private String initial;
    private int maxLength;
    private Integer maxLines;
    private Integer height;

    public PETextInput(Function<String, Component> componentDeserializer) {
        super(componentDeserializer);
    }

    @Override
    public PETextInput width(int width) {
        this.width = width;
        return this;
    }

    @Override
    public PETextInput label(@Nullable Component label) {
        this.label = label;
        return this;
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
                width > 0 ? width : DEFAULT_WIDTH,
                label != null ? label : Component.empty(),
                label != null,
                initial != null ? initial : "",
                maxLength > 0 ? maxLength : DEFAULT_MAX_LENGTH,
                new TextInputControl.MultilineOptions(maxLines, height)
        );
    }
}
