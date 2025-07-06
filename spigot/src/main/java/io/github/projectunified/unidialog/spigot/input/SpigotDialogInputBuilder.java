package io.github.projectunified.unidialog.spigot.input;

import io.github.projectunified.unidialog.core.input.DialogInputBuilder;
import net.md_5.bungee.api.dialog.input.DialogInput;

@SuppressWarnings("unchecked")
public class SpigotDialogInputBuilder extends SpigotDialogInput implements DialogInputBuilder {
    private SpigotDialogInput current;

    public SpigotDialogInputBuilder(String key) {
        super(key);
    }

    @Override
    public SpigotBooleanInput booleanInput() {
        current = new SpigotBooleanInput(key);
        return (SpigotBooleanInput) current;
    }

    @Override
    public SpigotTextInput textInput() {
        current = new SpigotTextInput(key);
        return (SpigotTextInput) current;
    }

    @Override
    public SpigotSingleOptionInput singleOptionInput() {
        current = new SpigotSingleOptionInput(key);
        return (SpigotSingleOptionInput) current;
    }

    @Override
    public SpigotNumberRangeInput numberRangeInput() {
        current = new SpigotNumberRangeInput(key);
        return (SpigotNumberRangeInput) current;
    }

    @Override
    public DialogInput getDialogInput() {
        if (current == null) {
            throw new IllegalStateException("No input type has been set.");
        }
        return current.getDialogInput();
    }
}
