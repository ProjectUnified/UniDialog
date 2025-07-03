package io.github.projectunified.unidialog.packetevents.input;

import com.github.retrooper.packetevents.protocol.dialog.input.InputControl;
import io.github.projectunified.unidialog.core.input.DialogInputBuilder;

@SuppressWarnings("unchecked")
public class PEDialogInputBuilder implements DialogInputBuilder, PEDialogInput {
    private PEDialogInput current;

    @Override
    public PEBooleanInput booleanInput() {
        PEBooleanInput input = new PEBooleanInput();
        current = input;
        return input;
    }

    @Override
    public PETextInput textInput() {
        PETextInput input = new PETextInput();
        current = input;
        return input;
    }

    @Override
    public PESingleOptionInput singleOptionInput() {
        PESingleOptionInput input = new PESingleOptionInput();
        current = input;
        return input;
    }

    @Override
    public PENumberRangeInput numberRangeInput() {
        PENumberRangeInput input = new PENumberRangeInput();
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
