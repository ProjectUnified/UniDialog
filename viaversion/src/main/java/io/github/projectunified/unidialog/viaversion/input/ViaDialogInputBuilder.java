package io.github.projectunified.unidialog.viaversion.input;

import com.viaversion.viaversion.libs.mcstructs.text.TextComponent;
import com.viaversion.viaversion.util.SerializerVersion;
import io.github.projectunified.unidialog.core.input.DialogInputBuilder;

import java.util.function.Function;

/**
 * A ViaVersion-based implementation of {@link DialogInputBuilder} for building dialog inputs.
 */
@SuppressWarnings("unchecked")
public class ViaDialogInputBuilder implements DialogInputBuilder {
    private final Function<String, TextComponent> componentDeserializer;
    private final SerializerVersion baseSerializer;
    private ViaDialogInput current;

    /**
     * Constructor for ViaDialogInputBuilder
     *
     * @param componentDeserializer the function to deserialize components from strings
     * @param baseSerializer        the serializer of the server version the components are created for
     */
    public ViaDialogInputBuilder(Function<String, TextComponent> componentDeserializer, SerializerVersion baseSerializer) {
        this.componentDeserializer = componentDeserializer;
        this.baseSerializer = baseSerializer;
    }

    @Override
    public ViaBooleanInput booleanInput() {
        ViaBooleanInput input = new ViaBooleanInput(componentDeserializer, baseSerializer);
        current = input;
        return input;
    }

    @Override
    public ViaTextInput textInput() {
        ViaTextInput input = new ViaTextInput(componentDeserializer, baseSerializer);
        current = input;
        return input;
    }

    @Override
    public ViaSingleOptionInput singleOptionInput() {
        ViaSingleOptionInput input = new ViaSingleOptionInput(componentDeserializer, baseSerializer);
        current = input;
        return input;
    }

    @Override
    public ViaNumberRangeInput numberRangeInput() {
        ViaNumberRangeInput input = new ViaNumberRangeInput(componentDeserializer, baseSerializer);
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
