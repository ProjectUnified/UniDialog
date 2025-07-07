package io.github.projectunified.unidialog.paper.input;

import io.github.projectunified.unidialog.adventure.input.AdventureSingleOptionInput;
import io.papermc.paper.registry.data.dialog.input.DialogInput;
import io.papermc.paper.registry.data.dialog.input.SingleOptionDialogInput;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class PaperSingleOptionInput extends PaperDialogInput implements AdventureSingleOptionInput<PaperSingleOptionInput> {
    private @Nullable Component label;
    private int width;
    private List<SingleOptionDialogInput.OptionEntry> options;

    public PaperSingleOptionInput(String key, Function<String, Component> componentDeserializer) {
        super(key, componentDeserializer);
    }

    @Override
    public PaperSingleOptionInput label(@Nullable Component label) {
        this.label = label;
        return this;
    }

    @Override
    public PaperSingleOptionInput option(String id, Component display, boolean isDefault) {
        if (options == null) {
            options = new ArrayList<>();
        }
        options.add(SingleOptionDialogInput.OptionEntry.create(id, display, isDefault));
        return this;
    }

    @Override
    public PaperSingleOptionInput width(int width) {
        this.width = width;
        return this;
    }

    @Override
    public DialogInput getDialogInput() {
        return DialogInput.singleOption(
                key,
                width > 0 ? width : DEFAULT_WIDTH,
                options != null ? options : Collections.emptyList(),
                label != null ? label : Component.empty(),
                label != null
        );
    }
}
