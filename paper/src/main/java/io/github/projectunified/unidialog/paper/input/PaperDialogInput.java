package io.github.projectunified.unidialog.paper.input;

import io.github.projectunified.unidialog.adventure.support.AdventureSupport;
import io.papermc.paper.registry.data.dialog.input.DialogInput;
import net.kyori.adventure.text.Component;

import java.util.function.Function;

public abstract class PaperDialogInput implements AdventureSupport {
    protected final String key;
    private final Function<String, Component> componentDeserializer;

    protected PaperDialogInput(String key, Function<String, Component> componentDeserializer) {
        this.key = key;
        this.componentDeserializer = componentDeserializer;
    }

    public abstract DialogInput getDialogInput();

    @Override
    public Function<String, Component> getComponentDeserializer() {
        return componentDeserializer;
    }
}
