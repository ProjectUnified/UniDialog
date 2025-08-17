package io.github.projectunified.unidialog.bungeecord.input;

import io.github.projectunified.unidialog.core.input.DialogInputBuilder;
import net.md_5.bungee.api.dialog.input.DialogInput;

@SuppressWarnings("unchecked")
public class BungeeDialogInputBuilder extends BungeeDialogInput implements DialogInputBuilder {
    private BungeeDialogInput current;

    public BungeeDialogInputBuilder(String key) {
        super(key);
    }

    @Override
    public BungeeBooleanInput booleanInput() {
        current = new BungeeBooleanInput(key);
        return (BungeeBooleanInput) current;
    }

    @Override
    public BungeeTextInput textInput() {
        current = new BungeeTextInput(key);
        return (BungeeTextInput) current;
    }

    @Override
    public BungeeSingleOptionInput singleOptionInput() {
        current = new BungeeSingleOptionInput(key);
        return (BungeeSingleOptionInput) current;
    }

    @Override
    public BungeeNumberRangeInput numberRangeInput() {
        current = new BungeeNumberRangeInput(key);
        return (BungeeNumberRangeInput) current;
    }

    @Override
    public DialogInput getDialogInput() {
        if (current == null) {
            throw new IllegalStateException("No input type has been set.");
        }
        return current.getDialogInput();
    }
}
