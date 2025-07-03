package io.github.projectunified.unidialog.core.body;

/**
 * Interface for building text bodies in a dialog.
 *
 * @param <T> the type of text body for method chaining
 */
public interface TextBody<T extends TextBody<T>> {
    /**
     * Set the text for the body
     *
     * @param text the text to set
     * @return the instance of the text body for method chaining
     */
    T text(String text);

    /**
     * Set the width of the text body
     *
     * @param width the width to set
     * @return the instance of the text body for method chaining
     */
    T width(int width);
}
