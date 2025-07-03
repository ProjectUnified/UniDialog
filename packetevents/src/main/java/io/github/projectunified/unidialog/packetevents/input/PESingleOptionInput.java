package io.github.projectunified.unidialog.packetevents.input;

import com.github.retrooper.packetevents.protocol.dialog.input.InputControl;
import com.github.retrooper.packetevents.protocol.dialog.input.SingleOptionInputControl;
import io.github.projectunified.unidialog.core.input.SingleOptionInput;
import io.github.retrooper.packetevents.adventure.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PESingleOptionInput implements SingleOptionInput<PESingleOptionInput>, PEDialogInput {
    private int width;
    private @Nullable Component label;
    private List<SingleOptionInputControl.Entry> entries;

    @Override
    public PESingleOptionInput width(int width) {
        this.width = width;
        return this;
    }

    public PESingleOptionInput label(@Nullable Component label) {
        this.label = label;
        return this;
    }

    @Override
    public PESingleOptionInput label(@Nullable String label) {
        if (label == null) {
            this.label = null;
            return this;
        }
        return label(LegacyComponentSerializer.legacySection().deserialize(label));
    }

    public PESingleOptionInput option(String id, Component display, boolean isDefault) {
        if (this.entries == null) {
            this.entries = new ArrayList<>();
        }
        this.entries.add(new SingleOptionInputControl.Entry(id, display, isDefault));
        return this;
    }

    @Override
    public PESingleOptionInput option(String id, String display, boolean isDefault) {
        return option(id, LegacyComponentSerializer.legacySection().deserialize(display), isDefault);
    }

    public PESingleOptionInput option(String id, Component display) {
        return option(id, display, false);
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
