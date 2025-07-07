package io.github.projectunified.unidialog.paper.input;

import io.github.projectunified.unidialog.adventure.input.AdventureBooleanInput;
import io.papermc.paper.registry.data.dialog.input.DialogInput;
import net.kyori.adventure.text.Component;

import java.util.function.Function;

public class PaperBooleanInput extends PaperDialogInput implements AdventureBooleanInput<PaperBooleanInput> {
    private Component label;
    private boolean initial;
    private String onTrue;
    private String onFalse;

    public PaperBooleanInput(String key, Function<String, Component> componentDeserializer) {
        super(key, componentDeserializer);
    }

    @Override
    public PaperBooleanInput label(Component label) {
        this.label = label;
        return this;
    }

    @Override
    public PaperBooleanInput initial(boolean initial) {
        this.initial = initial;
        return this;
    }

    @Override
    public PaperBooleanInput onTrue(String onTrue) {
        this.onTrue = onTrue;
        return this;
    }

    @Override
    public PaperBooleanInput onFalse(String onFalse) {
        this.onFalse = onFalse;
        return this;
    }

    @Override
    public DialogInput getDialogInput() {
        return DialogInput.bool(
                key,
                label != null ? label : Component.text(""),
                initial,
                onTrue != null ? onTrue : "true",
                onFalse != null ? onFalse : "false"
        );
    }
}
