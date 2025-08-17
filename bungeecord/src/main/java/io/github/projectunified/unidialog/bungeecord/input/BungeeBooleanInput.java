package io.github.projectunified.unidialog.bungeecord.input;

import io.github.projectunified.unidialog.core.input.BooleanInput;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.dialog.input.DialogInput;

public class BungeeBooleanInput extends BungeeDialogInput implements BooleanInput<BungeeBooleanInput> {
    private BaseComponent label;
    private Boolean initial;
    private String onTrue;
    private String onFalse;

    public BungeeBooleanInput(String key) {
        super(key);
    }

    /**
     * Set the label for the input.
     *
     * @param label the label to display for the input
     * @return the current instance for method chaining
     */
    public BungeeBooleanInput label(BaseComponent label) {
        this.label = label;
        return this;
    }

    @Override
    public BungeeBooleanInput label(String label) {
        return label(TextComponent.fromLegacy(label));
    }

    @Override
    public BungeeBooleanInput initial(boolean initial) {
        this.initial = initial;
        return this;
    }

    @Override
    public BungeeBooleanInput onTrue(String onTrue) {
        this.onTrue = onTrue;
        return this;
    }

    @Override
    public BungeeBooleanInput onFalse(String onFalse) {
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
