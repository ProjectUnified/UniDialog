package io.github.projectunified.unidialog.packetevents.input;

import com.github.retrooper.packetevents.protocol.dialog.input.BooleanInputControl;
import com.github.retrooper.packetevents.protocol.dialog.input.InputControl;
import io.github.projectunified.unidialog.core.input.BooleanInput;
import io.github.retrooper.packetevents.adventure.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.Component;

public class PEBooleanInput implements BooleanInput<PEBooleanInput>, PEDialogInput {
    private Component label;
    private boolean initial;
    private String onTrue;
    private String onFalse;

    public PEBooleanInput label(Component label) {
        this.label = label;
        return this;
    }

    @Override
    public PEBooleanInput label(String label) {
        return label(LegacyComponentSerializer.legacySection().deserialize(label));
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
