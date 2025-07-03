package io.github.projectunified.unidialog.core.dialog;

import io.github.projectunified.unidialog.core.action.DialogActionBuilder;
import io.github.projectunified.unidialog.core.body.DialogBodyBuilder;
import io.github.projectunified.unidialog.core.input.DialogInputBuilder;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.function.Consumer;

/**
 * Interface representing a multi-action dialog.
 *
 * @param <I>  the type of the item for the item body
 * @param <BB> the type of the dialog body builder
 * @param <IB> the type of the dialog input builder
 * @param <AB> the type of the dialog action builder
 * @param <T>  the type of the multi-action dialog itself, for method chaining
 */
public interface MultiActionDialog<I, BB extends DialogBodyBuilder<I>, IB extends DialogInputBuilder, AB extends DialogActionBuilder<AB>, T extends MultiActionDialog<I, BB, IB, AB, T>> extends Dialog<I, BB, IB, T> {
    /**
     * Set the number of columns for the dialog
     *
     * @param columns the number of columns to set
     * @return the dialog itself for method chaining
     */
    T columns(int columns);

    /**
     * Add an action to the dialog
     *
     * @param action the action to be performed
     * @return the dialog itself for method chaining
     */
    T action(Consumer<AB> action);

    /**
     * Add multiple actions to the dialog
     *
     * @param actions a collection of actions to be performed
     * @return the dialog itself for method chaining
     */
    T action(Collection<Consumer<AB>> actions);

    /**
     * Set the exit action for the dialog
     *
     * @param action the action to be performed when exiting the dialog
     * @return the dialog itself for method chaining
     */
    T exitAction(@Nullable Consumer<AB> action);
}
