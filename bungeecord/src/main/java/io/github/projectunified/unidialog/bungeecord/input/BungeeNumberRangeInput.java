package io.github.projectunified.unidialog.bungeecord.input;

import io.github.projectunified.unidialog.core.input.NumberRangeInput;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.dialog.input.DialogInput;
import org.jetbrains.annotations.Nullable;

public class BungeeNumberRangeInput extends BungeeDialogInput implements NumberRangeInput<BungeeNumberRangeInput> {
    private int width;
    private BaseComponent label;
    private String labelFormat;
    private float start;
    private float end;
    private @Nullable Float initial;
    private @Nullable Float step;

    public BungeeNumberRangeInput(String key) {
        super(key);
    }

    @Override
    public BungeeNumberRangeInput width(int width) {
        this.width = width;
        return this;
    }

    /**
     * Set the label for the input
     *
     * @param label the label text
     * @return the current instance for method chaining
     */
    public BungeeNumberRangeInput label(BaseComponent label) {
        this.label = label;
        return this;
    }

    @Override
    public BungeeNumberRangeInput label(String label) {
        return label(TextComponent.fromLegacy(label));
    }

    @Override
    public BungeeNumberRangeInput labelFormat(String labelFormat) {
        this.labelFormat = labelFormat;
        return this;
    }

    @Override
    public BungeeNumberRangeInput start(float start) {
        this.start = start;
        return this;
    }

    @Override
    public BungeeNumberRangeInput end(float end) {
        this.end = end;
        return this;
    }

    @Override
    public BungeeNumberRangeInput initial(@Nullable Float initial) {
        this.initial = initial;
        return this;
    }

    @Override
    public BungeeNumberRangeInput step(@Nullable Float step) {
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
