package io.github.projectunified.unidialog.spigot.input;

import io.github.projectunified.unidialog.core.input.NumberRangeInput;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.dialog.input.DialogInput;
import org.jetbrains.annotations.Nullable;

public class SpigotNumberRangeInput extends SpigotDialogInput implements NumberRangeInput<SpigotNumberRangeInput> {
    private int width;
    private BaseComponent label;
    private String labelFormat;
    private float start;
    private float end;
    private @Nullable Float initial;
    private @Nullable Float step;

    public SpigotNumberRangeInput(String key) {
        super(key);
    }

    @Override
    public SpigotNumberRangeInput width(int width) {
        this.width = width;
        return this;
    }

    /**
     * Set the label for the input
     *
     * @param label the label text
     * @return the current instance for method chaining
     */
    public SpigotNumberRangeInput label(BaseComponent label) {
        this.label = label;
        return this;
    }

    @Override
    public SpigotNumberRangeInput label(String label) {
        return label(TextComponent.fromLegacy(label));
    }

    @Override
    public SpigotNumberRangeInput labelFormat(String labelFormat) {
        this.labelFormat = labelFormat;
        return this;
    }

    @Override
    public SpigotNumberRangeInput start(float start) {
        this.start = start;
        return this;
    }

    @Override
    public SpigotNumberRangeInput end(float end) {
        this.end = end;
        return this;
    }

    @Override
    public SpigotNumberRangeInput initial(@Nullable Float initial) {
        this.initial = initial;
        return this;
    }

    @Override
    public SpigotNumberRangeInput step(@Nullable Float step) {
        this.step = step;
        return this;
    }

    @Override
    public DialogInput getDialogInput() {
        return new net.md_5.bungee.api.dialog.input.NumberRangeInput(
                key,
                width > 0 ? width : DEFAULT_WIDTH,
                label != null ? label : new TextComponent(""),
                labelFormat != null ? labelFormat : DEFAULT_LABEL_FORMAT,
                start,
                end,
                initial,
                step
        );
    }
}
