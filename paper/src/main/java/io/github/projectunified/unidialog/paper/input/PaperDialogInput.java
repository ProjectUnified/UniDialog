package io.github.projectunified.unidialog.paper.input;

import io.github.projectunified.unidialog.adventure.support.AdventureSupport;
import io.papermc.paper.registry.data.dialog.input.DialogInput;
import net.kyori.adventure.text.Component;

import java.util.function.Function;

/**
 * Base class for dialog inputs on the Paper platform
 */
public abstract class PaperDialogInput implements AdventureSupport {
    /**
     * The key of the input
     */
    protected final String key;
    private final Function<String, Component> componentDeserializer;

    /**
     * Constructor for PaperDialogInput
     *
     * @param key                   the input key
     * @param componentDeserializer a function to deserialize components from strings
     */
    protected PaperDialogInput(String key, Function<String, Component> componentDeserializer) {
        this.key = key;
        this.componentDeserializer = componentDeserializer;
    }

    /**
     * Get the dialog input
     *
     * @return the dialog input
     */
    public abstract DialogInput getDialogInput();

    @Override
    public Function<String, Component> getComponentDeserializer() {
        return componentDeserializer;
    }
}
