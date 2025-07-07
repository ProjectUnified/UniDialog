package io.github.projectunified.unidialog.core.input;

import org.jetbrains.annotations.Nullable;

/**
 * Interface for text input types in a dialog.
 *
 * @param <T> the type of the implementing class, for method chaining
 */
public interface TextInput<T extends TextInput<T>> {
    int DEFAULT_WIDTH = 200;
    int DEFAULT_MAX_LENGTH = 32;

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
     * Set the initial value for the input
     *
     * @param initial the initial text
     * @return the current instance for method chaining
     */
    T initial(String initial);

    /**
     * Set the maximum length of the input text
     *
     * @param maxLength the maximum number of characters allowed
     * @return the current instance for method chaining
     */
    T maxLength(int maxLength);

    /**
     * Set the maximum number of lines for the input
     *
     * @param maxLines the maximum number of lines allowed
     * @return the current instance for method chaining
     */
    T maxLines(@Nullable Integer maxLines);

    /**
     * Set the height of the input
     *
     * @param height the height
     * @return the current instance for method chaining
     */
    T height(@Nullable Integer height);
}
