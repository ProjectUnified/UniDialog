package io.github.projectunified.unidialog.packetevents.input;

import com.github.retrooper.packetevents.protocol.dialog.input.InputControl;
import com.github.retrooper.packetevents.protocol.dialog.input.NumberRangeInputControl;
import io.github.projectunified.unidialog.adventure.input.AdventureNumberRangeInput;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public class PENumberRangeInput extends PEDialogInput implements AdventureNumberRangeInput<PENumberRangeInput> {
    private int width;
    private Component label;
    private String labelFormat;
    private float start;
    private float end;
    private @Nullable Float initial;
    private @Nullable Float step;

    public PENumberRangeInput(Function<String, Component> componentDeserializer) {
        super(componentDeserializer);
    }

    @Override
    public PENumberRangeInput width(int width) {
        this.width = width;
        return this;
    }

    @Override
    public PENumberRangeInput label(Component label) {
        this.label = label;
        return this;
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
                width > 0 ? width : DEFAULT_WIDTH,
                label != null ? label : Component.text("Number Range Input"),
                labelFormat != null ? labelFormat : DEFAULT_LABEL_FORMAT,
                new NumberRangeInputControl.RangeInfo(start, end, initial, step)
        );
    }
}
