package io.github.projectunified.unidialog.adventure.input;

import io.github.projectunified.unidialog.adventure.support.AdventureSupport;
import io.github.projectunified.unidialog.core.input.NumberRangeInput;
import net.kyori.adventure.text.Component;

/**
 * An extension of {@link NumberRangeInput} that provides additional methods for handling {@link Component} labels.
 *
 * @param <T> the type of the input, for method chaining
 */
public interface AdventureNumberRangeInput<T extends AdventureNumberRangeInput<T>> extends NumberRangeInput<T>, AdventureSupport {
    /**
     * Set the label for the input
     *
     * @param label the label text
     * @return the current instance for method chaining
     */
    T label(Component label);

    @Override
    default T label(String label) {
        return label(deserialize(label));
    }
}
