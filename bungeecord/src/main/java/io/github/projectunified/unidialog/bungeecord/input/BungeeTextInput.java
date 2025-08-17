package io.github.projectunified.unidialog.bungeecord.input;

import io.github.projectunified.unidialog.core.input.TextInput;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.dialog.input.DialogInput;
import org.jetbrains.annotations.Nullable;

public class BungeeTextInput extends BungeeDialogInput implements TextInput<BungeeTextInput> {
    private int width;
    private @Nullable BaseComponent label;
    private String initial;
    private int maxLength;
    private @Nullable Integer maxLines;
    private @Nullable Integer height;

    public BungeeTextInput(String key) {
        super(key);
    }

    @Override
    public BungeeTextInput width(int width) {
        this.width = width;
        return this;
    }

    /**
     * Set the label for the input
     *
     * @param label the label text
     * @return the current instance for method chaining
     */
    public BungeeTextInput label(@Nullable BaseComponent label) {
        this.label = label;
        return this;
    }

    @Override
    public BungeeTextInput label(@Nullable String label) {
        return label(label == null ? null : TextComponent.fromLegacy(label));
    }

    @Override
    public BungeeTextInput initial(String initial) {
        this.initial = initial;
        return this;
    }

    @Override
    public BungeeTextInput maxLength(int maxLength) {
        this.maxLength = maxLength;
        return this;
    }

    @Override
    public BungeeTextInput maxLines(@Nullable Integer maxLines) {
        this.maxLines = maxLines;
        return this;
    }

    @Override
    public BungeeTextInput height(@Nullable Integer height) {
        this.height = height;
        return this;
    }

    @Override
    public DialogInput getDialogInput() {
        return new net.md_5.bungee.api.dialog.input.TextInput(
                key,
                width > 0 ? width : DEFAULT_WIDTH,
                label != null ? label : TextComponent.fromLegacy(""),
                label != null,
                initial != null ? initial : "",
                maxLength > 0 ? maxLength : DEFAULT_MAX_LENGTH,
                maxLines == null && height == null ? null : new net.md_5.bungee.api.dialog.input.TextInput.Multiline(maxLines, height)
        );
    }
}
