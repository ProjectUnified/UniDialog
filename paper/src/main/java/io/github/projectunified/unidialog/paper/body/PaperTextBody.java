package io.github.projectunified.unidialog.paper.body;

import io.github.projectunified.unidialog.adventure.body.AdventureTextBody;
import io.papermc.paper.registry.data.dialog.body.DialogBody;
import io.papermc.paper.registry.data.dialog.body.PlainMessageDialogBody;
import net.kyori.adventure.text.Component;

import java.util.function.Function;

/**
 * Represents a text dialog body for the Paper platform
 */
public class PaperTextBody implements AdventureTextBody<PaperTextBody>, PaperDialogBody {
    private final Function<String, Component> componentDeserializer;
    private Component text;
    private int width;

    /**
     * Constructor for PaperTextBody
     *
     * @param componentDeserializer a function to deserialize components from strings
     */
    public PaperTextBody(Function<String, Component> componentDeserializer) {
        this.componentDeserializer = componentDeserializer;
    }

    @Override
    public Function<String, Component> getComponentDeserializer() {
        return componentDeserializer;
    }

    @Override
    public PaperTextBody text(Component text) {
        this.text = text;
        return this;
    }

    @Override
    public PaperTextBody width(int width) {
        this.width = width;
        return this;
    }

    @Override
    public PlainMessageDialogBody getDialogBody() {
        return DialogBody.plainMessage(
                text != null ? text : Component.text(""),
                width > 0 ? width : DEFAULT_WIDTH
        );
    }
}
