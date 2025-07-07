package io.github.projectunified.unidialog.packetevents.input;

import com.github.retrooper.packetevents.protocol.dialog.input.InputControl;
import io.github.projectunified.unidialog.adventure.support.AdventureSupport;
import net.kyori.adventure.text.Component;

import java.util.function.Function;

public abstract class PEDialogInput implements AdventureSupport {
    private final Function<String, Component> componentDeserializer;

    protected PEDialogInput(Function<String, Component> componentDeserializer) {
        this.componentDeserializer = componentDeserializer;
    }

    public abstract InputControl getInput();

    @Override
    public Function<String, Component> getComponentDeserializer() {
        return componentDeserializer;
    }
}
