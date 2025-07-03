package io.github.projectunified.unidialog.core.body;

/**
 * Interface for building dialog bodies.
 *
 * @param <I> the type of item for the item body
 */
public interface DialogBodyBuilder<I> {
    /**
     * Create an item body
     *
     * @param <B> the type of text body
     * @param <T> the type of item body
     * @return the instance of the item body
     */
    <B extends TextBody<B>, T extends ItemBody<I, B, T>> T item();

    /**
     * Create a text body
     *
     * @param <T> the type of text body
     * @return the instance of the text body
     */
    <T extends TextBody<T>> T text();
}
