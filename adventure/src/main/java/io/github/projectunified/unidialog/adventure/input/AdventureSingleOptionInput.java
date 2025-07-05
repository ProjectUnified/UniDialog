package io.github.projectunified.unidialog.adventure.input;

import io.github.projectunified.unidialog.adventure.support.AdventureSupport;
import io.github.projectunified.unidialog.core.input.SingleOptionInput;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;

/**
 * An extension of {@link SingleOptionInput} that provides additional methods for handling {@link Component} labels and options.
 *
 * @param <T> the type of the input, for method chaining
 */
public interface AdventureSingleOptionInput<T extends AdventureSingleOptionInput<T>> extends SingleOptionInput<T>, AdventureSupport {
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

    /**
     * Add an option to the input
     *
     * @param id        the identifier for the option
     * @param display   the display text for the option
     * @param isDefault whether this option is the default selection
     * @return the current instance for method chaining
     */
    T option(String id, Component display, boolean isDefault);

    @Override
    default T option(String id, String display, boolean isDefault) {
        return option(id, deserialize(display), isDefault);
    }

    /**
     * Add an option to the input
     *
     * @param id      the identifier for the option
     * @param display the display text for the option
     * @return the current instance for method chaining
     */
    default T option(String id, Component display) {
        return option(id, display, false);
    }
}
