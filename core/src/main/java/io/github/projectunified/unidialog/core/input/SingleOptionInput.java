package io.github.projectunified.unidialog.core.input;

import org.jetbrains.annotations.Nullable;

/**
 * Interface for single option input types in a dialog.
 *
 * @param <T> the type of the implementing class, for method chaining
 */
public interface SingleOptionInput<T extends SingleOptionInput<T>> {
    int DEFAULT_WIDTH = 200;

    /**
     * Set the width of the input
     *
     * @param width the width
     * @return the current instance for method chaining
     */
    T width(int width);

    /**
     * Set the label for the input
     *
     * @param label the label text
     * @return the current instance for method chaining
     */
    T label(@Nullable String label);

    /**
     * Add an option to the input
     *
     * @param id        the identifier for the option
     * @param display   the display text for the option
     * @param isDefault whether this option is the default selection
     * @return the current instance for method chaining
     */
    T option(String id, String display, boolean isDefault);

    /**
     * Add an option to the input
     *
     * @param id      the identifier for the option
     * @param display the display text for the option
     * @return the current instance for method chaining
     */
    default T option(String id, String display) {
        return option(id, display, false);
    }
}
