package io.github.projectunified.unidialog.spigot.input;

import io.github.projectunified.unidialog.core.input.BooleanInput;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.dialog.input.DialogInput;

public class SpigotBooleanInput extends SpigotDialogInput implements BooleanInput<SpigotBooleanInput> {
    private BaseComponent label;
    private Boolean initial;
    private String onTrue;
    private String onFalse;

    public SpigotBooleanInput(String key) {
        super(key);
    }

    /**
     * Set the label for the input.
     *
     * @param label the label to display for the input
     * @return the current instance for method chaining
     */
    public SpigotBooleanInput label(BaseComponent label) {
        this.label = label;
        return this;
    }

    @Override
    public SpigotBooleanInput label(String label) {
        return label(TextComponent.fromLegacy(label));
    }

    @Override
    public SpigotBooleanInput initial(boolean initial) {
        this.initial = initial;
        return this;
    }

    @Override
    public SpigotBooleanInput onTrue(String onTrue) {
        this.onTrue = onTrue;
        return this;
    }

    @Override
    public SpigotBooleanInput onFalse(String onFalse) {
        this.onFalse = onFalse;
        return this;
    }

    @Override
    public DialogInput getDialogInput() {
        return new net.md_5.bungee.api.dialog.input.BooleanInput(
                key,
                label != null ? label : TextComponent.fromLegacy(""),
                initial,
                onTrue != null ? onTrue : "true",
                onFalse != null ? onFalse : "false"
        );
    }
}
