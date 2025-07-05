package io.github.projectunified.unidialog.adventure.action;

import io.github.projectunified.unidialog.adventure.support.AdventureSupport;
import io.github.projectunified.unidialog.core.action.DialogActionBuilder;
import io.github.projectunified.unidialog.core.dialog.Dialog;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;

/**
 * An extension of {@link DialogActionBuilder} that provides additional methods for {@link Component} labels and tooltips.
 *
 * @param <D> the type of the base dialog
 * @param <T> the type of the builder, for method chaining
 */
public interface AdventureDialogActionBuilder<D extends Dialog<?, ?, ?, ?>, T extends AdventureDialogActionBuilder<D, T>> extends DialogActionBuilder<D, T>, AdventureSupport {
    /**
     * Set the label for the dialog action
     *
     * @param label the label to set
     * @return the current instance of the builder for method chaining
     */
    T label(Component label);

    @Override
    default T label(String label) {
        return label(deserialize(label));
    }

    /**
     * Set the tooltip for the dialog action
     *
     * @param tooltip the tooltip to set, can be null
     * @return the current instance of the builder for method chaining
     */
    T tooltip(@Nullable Component tooltip);

    @Override
    default T tooltip(@Nullable String tooltip) {
        return tooltip(tooltip == null ? null : deserialize(tooltip));
    }
}
