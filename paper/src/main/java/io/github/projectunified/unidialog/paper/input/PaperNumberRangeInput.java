package io.github.projectunified.unidialog.paper.input;

import io.github.projectunified.unidialog.adventure.input.AdventureNumberRangeInput;
import io.papermc.paper.registry.data.dialog.input.DialogInput;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public class PaperNumberRangeInput extends PaperDialogInput implements AdventureNumberRangeInput<PaperNumberRangeInput> {
    private Component label;
    private int width;
    private String labelFormat;
    private float start;
    private float end;
    private @Nullable Float initial;
    private @Nullable Float step;

    public PaperNumberRangeInput(String key, Function<String, Component> componentDeserializer) {
        super(key, componentDeserializer);
    }

    @Override
    public PaperNumberRangeInput label(Component label) {
        this.label = label;
        return this;
    }

    @Override
    public PaperNumberRangeInput width(int width) {
        this.width = width;
        return this;
    }

    @Override
    public PaperNumberRangeInput labelFormat(String labelFormat) {
        this.labelFormat = labelFormat;
        return this;
    }

    @Override
    public PaperNumberRangeInput start(float start) {
        this.start = start;
        return this;
    }

    @Override
    public PaperNumberRangeInput end(float end) {
        this.end = end;
        return this;
    }

    @Override
    public PaperNumberRangeInput initial(@Nullable Float initial) {
        this.initial = initial;
        return this;
    }

    @Override
    public PaperNumberRangeInput step(@Nullable Float step) {
        this.step = step;
        return this;
    }

    @Override
    public DialogInput getDialogInput() {
        return DialogInput.numberRange(
                key,
                width > 0 ? width : DEFAULT_WIDTH,
                label != null ? label : Component.text(""),
                labelFormat != null ? labelFormat : DEFAULT_LABEL_FORMAT,
                start,
                end,
                initial,
                step
        );
    }
}
