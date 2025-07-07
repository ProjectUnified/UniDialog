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

public class PESingleOptionInput extends PEDialogInput implements AdventureSingleOptionInput<PESingleOptionInput> {
    private int width;
    private @Nullable Component label;
    private List<SingleOptionInputControl.Entry> entries;

    public PESingleOptionInput(Function<String, Component> componentDeserializer) {
        super(componentDeserializer);
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
                width > 0 ? width : DEFAULT_WIDTH,
                entries != null ? entries : Collections.emptyList(),
                label != null ? label : Component.empty(),
                label != null
        );
    }
}
