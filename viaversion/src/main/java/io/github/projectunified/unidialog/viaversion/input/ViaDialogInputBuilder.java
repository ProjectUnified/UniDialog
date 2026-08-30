package io.github.projectunified.unidialog.viaversion.input;

import com.viaversion.viaversion.libs.mcstructs.text.TextComponent;
import io.github.projectunified.unidialog.core.input.DialogInputBuilder;

import java.util.function.Function;

/**
 * A ViaVersion-based implementation of {@link DialogInputBuilder} for building dialog inputs.
 */
@SuppressWarnings("unchecked")
public class ViaDialogInputBuilder implements DialogInputBuilder {
    private final Function<String, TextComponent> componentDeserializer;
    private ViaDialogInput current;

    /**
     * Constructor for ViaDialogInputBuilder
     *
     * @param componentDeserializer the function to deserialize components from strings
     */
    public ViaDialogInputBuilder(Function<String, TextComponent> componentDeserializer) {
        this.componentDeserializer = componentDeserializer;
    }

    @Override
    public ViaBooleanInput booleanInput() {
        ViaBooleanInput input = new ViaBooleanInput(componentDeserializer);
        current = input;
        return input;
    }

    @Override
    public ViaTextInput textInput() {
        ViaTextInput input = new ViaTextInput(componentDeserializer);
        current = input;
        return input;
    }

    @Override
    public ViaSingleOptionInput singleOptionInput() {
        ViaSingleOptionInput input = new ViaSingleOptionInput(componentDeserializer);
        current = input;
        return input;
    }

    @Override
    public ViaNumberRangeInput numberRangeInput() {
        ViaNumberRangeInput input = new ViaNumberRangeInput(componentDeserializer);
        current = input;
        return input;
    }

    /**
     * Get the built input
     *
     * @return the input
     */
    public ViaDialogInput getInput() {
        if (current == null) {
            throw new IllegalStateException("No input has been created yet");
        }
        return current;
    }
}
