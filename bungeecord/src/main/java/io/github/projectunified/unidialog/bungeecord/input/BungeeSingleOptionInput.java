package io.github.projectunified.unidialog.bungeecord.input;

import io.github.projectunified.unidialog.core.input.SingleOptionInput;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.dialog.input.DialogInput;
import net.md_5.bungee.api.dialog.input.InputOption;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class BungeeSingleOptionInput extends BungeeDialogInput implements SingleOptionInput<BungeeSingleOptionInput> {
    private int width;
    private @Nullable BaseComponent label;
    private List<InputOption> options;

    public BungeeSingleOptionInput(String key) {
        super(key);
    }

    @Override
    public BungeeSingleOptionInput width(int width) {
        this.width = width;
        return this;
    }

    /**
     * Set the label for the input
     *
     * @param label the label text
     * @return the current instance for method chaining
     */
    public BungeeSingleOptionInput label(@Nullable BaseComponent label) {
        this.label = label;
        return this;
    }

    @Override
    public BungeeSingleOptionInput label(@Nullable String label) {
        return label(label == null ? null : TextComponent.fromLegacy(label));
    }

    /**
     * Add an option to the input
     *
     * @param id        the identifier for the option
     * @param display   the display text for the option
     * @param isDefault whether this option is the default selection
     * @return the current instance for method chaining
     */
    public BungeeSingleOptionInput option(String id, BaseComponent display, boolean isDefault) {
        if (this.options == null) {
            this.options = new java.util.ArrayList<>();
        }
        this.options.add(new InputOption(id, display, isDefault));
        return this;
    }

    /**
     * Add an option to the input
     *
     * @param id      the identifier for the option
     * @param display the display text for the option
     * @return the current instance for method chaining
     */
    public BungeeSingleOptionInput option(String id, BaseComponent display) {
        return option(id, display, false);
    }

    @Override
    public BungeeSingleOptionInput option(String id, String display, boolean isDefault) {
        return option(id, TextComponent.fromLegacy(display), isDefault);
    }

    @Override
    public DialogInput getDialogInput() {
        return new net.md_5.bungee.api.dialog.input.SingleOptionInput(
                key,
                width > 0 ? width : DEFAULT_WIDTH,
                label != null ? label : TextComponent.fromLegacy(""),
                label != null,
                options != null ? options : Collections.emptyList()
        );
    }
}
