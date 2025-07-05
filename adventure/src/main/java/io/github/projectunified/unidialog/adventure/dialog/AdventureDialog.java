package io.github.projectunified.unidialog.adventure.dialog;

import io.github.projectunified.unidialog.adventure.support.AdventureSupport;
import io.github.projectunified.unidialog.core.body.DialogBodyBuilder;
import io.github.projectunified.unidialog.core.dialog.Dialog;
import io.github.projectunified.unidialog.core.input.DialogInputBuilder;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;

/**
 * An extension of {@link Dialog} that provides additional methods for handling {@link Component} titles and external titles.
 *
 * @param <I>  the type of the item for the item body
 * @param <BB> the type of the dialog body builder
 * @param <IB> the type of the dialog input builder
 * @param <T>  the type of the dialog itself, for method chaining
 */
public interface AdventureDialog<I, BB extends DialogBodyBuilder<I>, IB extends DialogInputBuilder, T extends AdventureDialog<I, BB, IB, T>> extends Dialog<I, BB, IB, T>, AdventureSupport {
    /**
     * Set the title of the dialog
     *
     * @param title the title of the dialog
     * @return the dialog itself for method chaining
     */
    T title(Component title);

    @Override
    default T title(String title) {
        return title(deserialize(title));
    }

    /**
     * Set the external title of the dialog, which is used for displaying the dialog in a multi-dialog dialog
     *
     * @param externalTitle the external title of the dialog
     * @return the dialog itself for method chaining
     */
    T externalTitle(@Nullable Component externalTitle);

    @Override
    default T externalTitle(@Nullable String externalTitle) {
        return externalTitle(externalTitle == null ? null : deserialize(externalTitle));
    }
}
