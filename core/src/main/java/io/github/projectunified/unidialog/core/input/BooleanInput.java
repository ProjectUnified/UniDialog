package io.github.projectunified.unidialog.core.input;

/**
 * Interface for boolean input types in a dialog.
 *
 * @param <T> the type of the implementing class, for method chaining
 */
public interface BooleanInput<T extends BooleanInput<T>> {
    /**
     * Set the label for the input.
     *
     * @param label the label to display for the input
     * @return the current instance for method chaining
     */
    T label(String label);

    /**
     * Set the initial value of the boolean input.
     *
     * @param initial the initial value (true or false)
     * @return the current instance for method chaining
     */
    T initial(boolean initial);

    /**
     * Set the text to be sent when the input is true.
     *
     * @param onTrue the text to be sent when the input is true
     * @return the current instance for method chaining
     */
    T onTrue(String onTrue);

    /**
     * Set the text to be sent when the input is false.
     *
     * @param onFalse the text to be sent when the input is false
     * @return the current instance for method chaining
     */
    T onFalse(String onFalse);
}
