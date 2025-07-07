package io.github.projectunified.unidialog.paper.input;

import io.github.projectunified.unidialog.adventure.input.AdventureTextInput;
import io.papermc.paper.registry.data.dialog.input.DialogInput;
import io.papermc.paper.registry.data.dialog.input.TextDialogInput;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public class PaperTextInput extends PaperDialogInput implements AdventureTextInput<PaperTextInput> {
    private @Nullable Component label;
    private int width;
    private @Nullable String initial;
    private int maxLength;
    private @Nullable Integer maxLines;
    private @Nullable Integer height;

    public PaperTextInput(String key, Function<String, Component> componentDeserializer) {
        super(key, componentDeserializer);
    }

    @Override
    public PaperTextInput label(@Nullable Component label) {
        this.label = label;
        return this;
    }

    @Override
    public PaperTextInput width(int width) {
        this.width = width;
        return this;
    }

    @Override
    public PaperTextInput initial(String initial) {
        this.initial = initial;
        return this;
    }

    @Override
    public PaperTextInput maxLength(int maxLength) {
        this.maxLength = maxLength;
        return this;
    }

    @Override
    public PaperTextInput maxLines(@Nullable Integer maxLines) {
        this.maxLines = maxLines;
        return this;
    }

    @Override
    public PaperTextInput height(@Nullable Integer height) {
        this.height = height;
        return this;
    }

    @Override
    public DialogInput getDialogInput() {
        return DialogInput.text(
                key,
                width > 0 ? width : DEFAULT_WIDTH,
                label != null ? label : Component.empty(),
                label != null,
                initial != null ? initial : "",
                maxLength > 0 ? maxLength : DEFAULT_MAX_LENGTH,
                TextDialogInput.MultilineOptions.create(maxLines, height)
        );
    }
}
