package io.github.projectunified.unidialog.core.input;

/**
 * Interface for building dialog inputs.
 * This interface provides methods to create various types of inputs for a dialog.
 * It includes methods for creating boolean inputs, text inputs, single option inputs, and number range inputs.
 */
public interface DialogInputBuilder {
    /**
     * Create a boolean input
     *
     * @param <T> the type of the boolean input
     * @return a new instance of a boolean input
     */
    <T extends BooleanInput<T>> T booleanInput();

    /**
     * Create a text input
     *
     * @param <T> the type of the text input
     * @return a new instance of a text input
     */
    <T extends TextInput<T>> T textInput();

    /**
     * Create a single option input
     *
     * @param <T> the type of the single option input
     * @return a new instance of a single option input
     */
    <T extends SingleOptionInput<T>> T singleOptionInput();

    /**
     * Create a number range input
     *
     * @param <T> the type of the number range input
     * @return a new instance of a number range input
     */
    <T extends NumberRangeInput<T>> T numberRangeInput();
}
