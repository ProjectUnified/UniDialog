package io.github.projectunified.unidialog.core.body;

import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

/**
 * Interface for building item bodies in a dialog.
 *
 * @param <I> the type of item for the item body
 * @param <B> the type of text body for the description
 * @param <T> the type of item body for method chaining
 */
public interface ItemBody<I, B extends TextBody<B>, T extends ItemBody<I, B, T>> {
    /**
     * Set the item for the body
     *
     * @param item the item to set
     * @return the instance of the item body for method chaining
     */
    T item(I item);

    /**
     * Set the description for the body
     *
     * @param descriptionBuilder a consumer to build the description
     * @return the instance of the item body for method chaining
     */
    T description(@Nullable Consumer<B> descriptionBuilder);

    /**
     * Set the option to show decorations in the body
     *
     * @param showDecorations whether to show decorations
     * @return the instance of the item body for method chaining
     */
    T showDecorations(boolean showDecorations);

    /**
     * Set the option to show a tooltip in the body
     *
     * @param showTooltip whether to show a tooltip
     * @return the instance of the item body for method chaining
     */
    T showTooltip(boolean showTooltip);

    /**
     * Set the width of the item body
     *
     * @param width the width to set
     * @return the instance of the item body for method chaining
     */
    T width(int width);

    /**
     * Set the height of the item body
     *
     * @param height the height to set
     * @return the instance of the item body for method chaining
     */
    T height(int height);
}
