package io.github.projectunified.unidialog.adventure.input;

import io.github.projectunified.unidialog.adventure.support.AdventureSupport;
import io.github.projectunified.unidialog.core.input.TextInput;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;

/**
 * An extension of {@link TextInput} that provides additional methods for handling {@link Component} labels.
 *
 * @param <T> the type of the input, for method chaining
 */
public interface AdventureTextInput<T extends AdventureTextInput<T>> extends TextInput<T>, AdventureSupport {
    /**
     * Set the label for the input
     *
     * @param label the label text
     * @return the current instance for method chaining
     */
    T label(@Nullable Component label);

    @Override
    default T label(@Nullable String label) {
        return label(label == null ? null : deserialize(label));
    }
}
