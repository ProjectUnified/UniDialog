package io.github.projectunified.unidialog.core.input;

import org.jetbrains.annotations.Nullable;

/**
 * Interface for number range input in a dialog.
 *
 * @param <T> the type of the implementing class, for method chaining.
 */
public interface NumberRangeInput<T extends NumberRangeInput<T>> {
    int DEFAULT_WIDTH = 200;
    String DEFAULT_LABEL_FORMAT = "options.generic_value";

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
    T label(String label);

    /**
     * Set the format for the label
     *
     * @param labelFormat the format string for the label
     * @return the current instance for method chaining
     */
    T labelFormat(String labelFormat);

    /**
     * Set the start value of the range
     *
     * @param start the start value
     * @return the current instance for method chaining
     */
    T start(float start);

    /**
     * Set the end value of the range
     *
     * @param end the end value
     * @return the current instance for method chaining
     */
    T end(float end);

    /**
     * Set the initial value of the range
     *
     * @param initial the initial value, can be null
     * @return the current instance for method chaining
     */
    T initial(@Nullable Float initial);

    /**
     * Set the step value for the range
     *
     * @param step the step value, can be null
     * @return the current instance for method chaining
     */
    T step(@Nullable Float step);
}
