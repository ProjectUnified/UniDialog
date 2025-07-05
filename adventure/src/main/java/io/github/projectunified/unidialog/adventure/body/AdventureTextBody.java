package io.github.projectunified.unidialog.adventure.body;

import io.github.projectunified.unidialog.adventure.support.AdventureSupport;
import io.github.projectunified.unidialog.core.body.TextBody;
import net.kyori.adventure.text.Component;

/**
 * An extension of {@link TextBody} that provides additional methods for handling {@link Component} text.
 *
 * @param <T> the type of the body, for method chaining
 */
public interface AdventureTextBody<T extends AdventureTextBody<T>> extends TextBody<T>, AdventureSupport {
    /**
     * Set the text for the body
     *
     * @param text the text to set
     * @return the instance of the text body for method chaining
     */
    T text(Component text);

    @Override
    default T text(String text) {
        return text(deserialize(text));
    }
}
