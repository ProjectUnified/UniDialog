package io.github.projectunified.unidialog.packetevents.input;

import com.github.retrooper.packetevents.protocol.dialog.input.InputControl;
import io.github.projectunified.unidialog.core.input.DialogInputBuilder;
import net.kyori.adventure.text.Component;

import java.util.function.Function;

@SuppressWarnings("unchecked")
public class PEDialogInputBuilder implements DialogInputBuilder, PEDialogInput {
    private final Function<String, Component> componentDeserializer;
    private PEDialogInput current;

    public PEDialogInputBuilder(Function<String, Component> componentDeserializer) {
        this.componentDeserializer = componentDeserializer;
    }

    @Override
    public PEBooleanInput booleanInput() {
        PEBooleanInput input = new PEBooleanInput(componentDeserializer);
        current = input;
        return input;
    }

    @Override
    public PETextInput textInput() {
        PETextInput input = new PETextInput(componentDeserializer);
        current = input;
        return input;
    }

    @Override
    public PESingleOptionInput singleOptionInput() {
        PESingleOptionInput input = new PESingleOptionInput(componentDeserializer);
        current = input;
        return input;
    }

    @Override
    public PENumberRangeInput numberRangeInput() {
        PENumberRangeInput input = new PENumberRangeInput(componentDeserializer);
        current = input;
        return input;
    }

    @Override
    public InputControl getInput() {
        if (current == null) {
            throw new IllegalStateException("No input has been created yet");
        }
        return current.getInput();
    }
}
