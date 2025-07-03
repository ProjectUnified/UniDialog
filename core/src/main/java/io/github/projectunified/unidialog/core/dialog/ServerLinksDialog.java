package io.github.projectunified.unidialog.core.dialog;

import io.github.projectunified.unidialog.core.action.DialogActionBuilder;
import io.github.projectunified.unidialog.core.body.DialogBodyBuilder;
import io.github.projectunified.unidialog.core.input.DialogInputBuilder;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

/**
 * Interface representing a server links dialog.
 *
 * @param <I>  the type of the item for the item body
 * @param <BB> the type of the dialog body builder
 * @param <IB> the type of the dialog input builder
 * @param <AB> the type of the dialog action builder
 * @param <T>  the type of the server links dialog itself, for method chaining
 */
public interface ServerLinksDialog<I, BB extends DialogBodyBuilder<I>, IB extends DialogInputBuilder, AB extends DialogActionBuilder<AB>, T extends ServerLinksDialog<I, BB, IB, AB, T>> extends Dialog<I, BB, IB, T> {
    /**
     * Set the exit action for the dialog
     *
     * @param action the action to be performed when exiting the dialog
     * @return the server links dialog itself for method chaining
     */
    T exitAction(@Nullable Consumer<AB> action);

    /**
     * Set the number of columns for the dialog
     *
     * @param columns the number of columns to set
     * @return the server links dialog itself for method chaining
     */
    T columns(int columns);

    /**
     * Set the width of the buttons in the dialog
     *
     * @param buttonWidth the width of the buttons
     * @return the server links dialog itself for method chaining
     */
    T buttonWidth(int buttonWidth);
}
