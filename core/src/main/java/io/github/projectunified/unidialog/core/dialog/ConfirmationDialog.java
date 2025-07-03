package io.github.projectunified.unidialog.core.dialog;

import io.github.projectunified.unidialog.core.action.DialogActionBuilder;
import io.github.projectunified.unidialog.core.body.DialogBodyBuilder;
import io.github.projectunified.unidialog.core.input.DialogInputBuilder;

import java.util.function.Consumer;

/**
 * Interface representing a confirmation dialog.
 *
 * @param <I>  the type of the item for the item body
 * @param <BB> the type of the dialog body builder
 * @param <IB> the type of the dialog input builder
 * @param <AB> the type of the dialog action builder
 * @param <T>  the type of the confirmation dialog itself, for method chaining
 */
public interface ConfirmationDialog<I, BB extends DialogBodyBuilder<I>, IB extends DialogInputBuilder, AB extends DialogActionBuilder<AB>, T extends ConfirmationDialog<I, BB, IB, AB, T>> extends Dialog<I, BB, IB, T> {
    /**
     * Set the action for the "yes" button in the confirmation dialog
     *
     * @param action the action to be performed when the "yes" button is clicked
     * @return the confirmation dialog itself for method chaining
     */
    T yesAction(Consumer<AB> action);

    /**
     * Set the action for the "no" button in the confirmation dialog
     *
     * @param action the action to be performed when the "no" button is clicked
     * @return the confirmation dialog itself for method chaining
     */
    T noAction(Consumer<AB> action);

    /**
     * Check if the confirmation dialog has a "yes" action defined
     *
     * @return true if a "yes" action is defined, false otherwise
     */
    boolean hasYesAction();

    /**
     * Check if the confirmation dialog has a "no" action defined
     *
     * @return true if a "no" action is defined, false otherwise
     */
    boolean hasNoAction();
}
