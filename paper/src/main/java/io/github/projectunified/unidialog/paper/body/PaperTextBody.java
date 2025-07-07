package io.github.projectunified.unidialog.paper.body;

import io.github.projectunified.unidialog.adventure.body.AdventureTextBody;
import io.papermc.paper.registry.data.dialog.body.DialogBody;
import io.papermc.paper.registry.data.dialog.body.PlainMessageDialogBody;
import net.kyori.adventure.text.Component;

import java.util.function.Function;

public class PaperTextBody implements AdventureTextBody<PaperTextBody>, PaperDialogBody {
    private final Function<String, Component> componentDeserializer;
    private Component text;
    private int width;

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
