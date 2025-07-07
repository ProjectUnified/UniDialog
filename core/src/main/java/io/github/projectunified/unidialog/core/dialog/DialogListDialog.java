package io.github.projectunified.unidialog.core.dialog;

import io.github.projectunified.unidialog.core.action.DialogActionBuilder;
import io.github.projectunified.unidialog.core.body.DialogBodyBuilder;
import io.github.projectunified.unidialog.core.input.DialogInputBuilder;
import io.github.projectunified.unidialog.core.opener.DialogOpener;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.function.Consumer;

/**
 * Interface representing a dialog that contains a list of other dialogs.
 *
 * @param <I>  the type of the item for the item body
 * @param <BB> the type of the dialog body builder
 * @param <IB> the type of the dialog input builder
 * @param <D>  the type of the dialog contained in the list
 * @param <AB> the type of the dialog action builder
 * @param <T>  the type of the dialog itself, for method chaining
 */
public interface DialogListDialog<I, BB extends DialogBodyBuilder<I>, IB extends DialogInputBuilder, D extends Dialog<I, BB, IB, ?>, AB extends DialogActionBuilder<D, AB>, T extends DialogListDialog<I, BB, IB, D, AB, T>> extends Dialog<I, BB, IB, T> {
    /**
     * Add a dialog to the list of dialogs in this dialog
     *
     * @param dialog the dialog to add
     * @return the dialog itself for method chaining
     */
    T dialog(D dialog);

    /**
     * Add a dialog to the list of dialogs in this dialog
     *
     * @param namespace the namespace of the dialog
     * @param dialogId  the ID of the dialog
     * @return the dialog itself for method chaining
     */
    T dialog(String namespace, String dialogId);

    /**
     * Add a dialog to the list of dialogs in this dialog
     *
     * @param dialogOpener the dialog opener that will open the dialog
     * @return the dialog itself for method chaining
     */
    T dialog(DialogOpener dialogOpener);

    /**
     * Set the action to be performed when exiting the dialog
     *
     * @param action the action to be performed when exiting the dialog
     * @return the dialog itself for method chaining
     */
    T exitAction(@Nullable Consumer<AB> action);

    /**
     * Set the number of columns in the dialog
     *
     * @param columns the number of columns in the dialog
     * @return the dialog itself for method chaining
     */
    T columns(int columns);

    /**
     * Set the width of the buttons in the dialog
     *
     * @param buttonWidth the width of the buttons in the dialog
     * @return the dialog itself for method chaining
     */
    T buttonWidth(int buttonWidth);

    /**
     * Add multiple dialogs to the list of dialogs in this dialog
     *
     * @param dialogs the collection of dialogs to add
     * @return the dialog itself for method chaining
     */
    default <B extends D> T dialog(Collection<B> dialogs) {
        for (B dialog : dialogs) {
            dialog(dialog);
        }
        //noinspection unchecked
        return (T) this;
    }
}
