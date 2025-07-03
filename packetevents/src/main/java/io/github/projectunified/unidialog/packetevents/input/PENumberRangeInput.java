package io.github.projectunified.unidialog.packetevents.input;

import com.github.retrooper.packetevents.protocol.dialog.input.InputControl;
import com.github.retrooper.packetevents.protocol.dialog.input.NumberRangeInputControl;
import io.github.projectunified.unidialog.core.input.NumberRangeInput;
import io.github.retrooper.packetevents.adventure.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;

public class PENumberRangeInput implements NumberRangeInput<PENumberRangeInput>, PEDialogInput {
    private int width;
    private Component label;
    private String labelFormat;
    private float start;
    private float end;
    private @Nullable Float initial;
    private @Nullable Float step;

    @Override
    public PENumberRangeInput width(int width) {
        this.width = width;
        return this;
    }

    public PENumberRangeInput label(Component label) {
        this.label = label;
        return this;
    }

    @Override
    public PENumberRangeInput label(String label) {
        return label(LegacyComponentSerializer.legacySection().deserialize(label));
    }

    @Override
    public PENumberRangeInput labelFormat(String labelFormat) {
        this.labelFormat = labelFormat;
        return this;
    }

    @Override
    public PENumberRangeInput start(float start) {
        this.start = start;
        return this;
    }

    @Override
    public PENumberRangeInput end(float end) {
        this.end = end;
        return this;
    }

    @Override
    public PENumberRangeInput initial(Float initial) {
        this.initial = initial;
        return this;
    }

    @Override
    public PENumberRangeInput step(Float step) {
        this.step = step;
        return this;
    }

    @Override
    public InputControl getInput() {
        return new NumberRangeInputControl(
                width > 0 ? width : 200,
                label == null ? Component.text("Number Range Input") : label,
                labelFormat == null ? "options.generic_value" : labelFormat,
                new NumberRangeInputControl.RangeInfo(start, end, initial, step)
        );
    }
}
