package io.github.projectunified.unidialog.core.dialog;

import io.github.projectunified.unidialog.core.action.DialogActionBuilder;
import io.github.projectunified.unidialog.core.body.DialogBodyBuilder;
import io.github.projectunified.unidialog.core.input.DialogInputBuilder;

import java.util.function.Consumer;

/**
 * Interface representing a notice dialog.
 *
 * @param <I>  the type of the item for the item body
 * @param <BB> the type of the dialog body builder
 * @param <IB> the type of the dialog input builder
 * @param <D>  the type of the base dialog
 * @param <AB> the type of the dialog action builder
 * @param <T>  the type of the notice dialog itself, for method chaining
 */
public interface NoticeDialog<I, BB extends DialogBodyBuilder<I>, IB extends DialogInputBuilder, D extends Dialog<I, BB, IB, ?>, AB extends DialogActionBuilder<D, AB>, T extends NoticeDialog<I, BB, IB, D, AB, T>> extends Dialog<I, BB, IB, T> {
    /**
     * Set the action of the notice dialog
     *
     * @param action the action to be performed
     * @return the notice dialog itself for method chaining
     */
    T action(Consumer<AB> action);
}
