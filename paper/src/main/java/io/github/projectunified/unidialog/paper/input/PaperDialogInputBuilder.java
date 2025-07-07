package io.github.projectunified.unidialog.paper.input;

import io.github.projectunified.unidialog.core.input.DialogInputBuilder;
import io.papermc.paper.registry.data.dialog.input.DialogInput;
import net.kyori.adventure.text.Component;

import java.util.function.Function;

@SuppressWarnings("unchecked")
public class PaperDialogInputBuilder extends PaperDialogInput implements DialogInputBuilder {
    private PaperDialogInput current;

    public PaperDialogInputBuilder(String key, Function<String, Component> componentDeserializer) {
        super(key, componentDeserializer);
    }

    @Override
    public PaperBooleanInput booleanInput() {
        PaperBooleanInput input = new PaperBooleanInput(key, getComponentDeserializer());
        current = input;
        return input;
    }

    @Override
    public PaperTextInput textInput() {
        PaperTextInput input = new PaperTextInput(key, getComponentDeserializer());
        current = input;
        return input;
    }

    @Override
    public PaperSingleOptionInput singleOptionInput() {
        PaperSingleOptionInput input = new PaperSingleOptionInput(key, getComponentDeserializer());
        current = input;
        return input;
    }

    @Override
    public PaperNumberRangeInput numberRangeInput() {
        PaperNumberRangeInput input = new PaperNumberRangeInput(key, getComponentDeserializer());
        current = input;
        return input;
    }

    @Override
    public DialogInput getDialogInput() {
        if (current == null) {
            throw new IllegalStateException("No dialog input has been created yet.");
        }
        return current.getDialogInput();
    }
}
