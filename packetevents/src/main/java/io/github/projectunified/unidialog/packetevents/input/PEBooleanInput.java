package io.github.projectunified.unidialog.packetevents.input;

import com.github.retrooper.packetevents.protocol.dialog.input.BooleanInputControl;
import com.github.retrooper.packetevents.protocol.dialog.input.InputControl;
import io.github.projectunified.unidialog.adventure.input.AdventureBooleanInput;
import net.kyori.adventure.text.Component;

import java.util.function.Function;

public class PEBooleanInput extends PEDialogInput implements AdventureBooleanInput<PEBooleanInput> {
    private Component label;
    private boolean initial;
    private String onTrue;
    private String onFalse;

    public PEBooleanInput(Function<String, Component> componentDeserializer) {
        super(componentDeserializer);
    }

    @Override
    public PEBooleanInput label(Component label) {
        this.label = label;
        return this;
    }

    @Override
    public PEBooleanInput initial(boolean initial) {
        this.initial = initial;
        return this;
    }

    @Override
    public PEBooleanInput onTrue(String onTrue) {
        this.onTrue = onTrue;
        return this;
    }

    @Override
    public PEBooleanInput onFalse(String onFalse) {
        this.onFalse = onFalse;
        return this;
    }

    @Override
    public InputControl getInput() {
        return new BooleanInputControl(
                label == null ? Component.text("Boolean Input") : label,
                initial,
                onTrue == null ? "true" : onTrue,
                onFalse == null ? "false" : onFalse
        );
    }
}
