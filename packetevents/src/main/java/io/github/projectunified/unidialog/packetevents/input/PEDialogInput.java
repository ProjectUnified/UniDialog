package io.github.projectunified.unidialog.packetevents.input;

import com.github.retrooper.packetevents.protocol.dialog.input.InputControl;
import io.github.projectunified.unidialog.adventure.support.AdventureSupport;
import net.kyori.adventure.text.Component;

import java.util.function.Function;

/**
 * A PacketEvents-based base class for dialog inputs.
 */
public abstract class PEDialogInput implements AdventureSupport {
    private final Function<String, Component> componentDeserializer;

    /**
     * Constructor for PEDialogInput
     *
     * @param componentDeserializer a function to deserialize components from strings
     */
    protected PEDialogInput(Function<String, Component> componentDeserializer) {
        this.componentDeserializer = componentDeserializer;
    }

    /**
     * Get the PacketEvents input control for this input
     *
     * @return the input control
     */
    public abstract InputControl getInput();

    @Override
    public Function<String, Component> getComponentDeserializer() {
        return componentDeserializer;
    }
}
