package io.github.projectunified.unidialog.packetevents.input;

import com.github.retrooper.packetevents.protocol.dialog.input.InputControl;
import com.github.retrooper.packetevents.protocol.dialog.input.SingleOptionInputControl;
import io.github.projectunified.unidialog.adventure.input.AdventureSingleOptionInput;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class PESingleOptionInput implements AdventureSingleOptionInput<PESingleOptionInput>, PEDialogInput {
    private final Function<String, Component> componentDeserializer;
    private int width;
    private @Nullable Component label;
    private List<SingleOptionInputControl.Entry> entries;

    public PESingleOptionInput(Function<String, Component> componentDeserializer) {
        this.componentDeserializer = componentDeserializer;
    }

    @Override
    public Function<String, Component> getComponentDeserializer() {
        return componentDeserializer;
    }

    @Override
    public PESingleOptionInput width(int width) {
        this.width = width;
        return this;
    }

    @Override
    public PESingleOptionInput label(@Nullable Component label) {
        this.label = label;
        return this;
    }

    @Override
    public PESingleOptionInput option(String id, Component display, boolean isDefault) {
        if (this.entries == null) {
            this.entries = new ArrayList<>();
        }
        this.entries.add(new SingleOptionInputControl.Entry(id, display, isDefault));
        return this;
    }

    @Override
    public InputControl getInput() {
        return new SingleOptionInputControl(
                width > 0 ? width : 200,
                entries != null ? entries : Collections.emptyList(),
                label == null ? Component.empty() : label,
                label != null
        );
    }
}
