package io.github.projectunified.unidialog.core.dialog;

import io.github.projectunified.unidialog.core.body.DialogBodyBuilder;
import io.github.projectunified.unidialog.core.input.DialogInputBuilder;
import io.github.projectunified.unidialog.core.opener.DialogOpener;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Interface representing a dialog.
 *
 * @param <I>  the type of the item for the item body
 * @param <BB> the type of the dialog body builder
 * @param <IB> the type of the dialog input builder
 * @param <T>  the type of the dialog itself, for method chaining
 */
public interface Dialog<I, BB extends DialogBodyBuilder<I>, IB extends DialogInputBuilder, T extends Dialog<I, BB, IB, T>> {
    /**
     * Set the title of the dialog
     *
     * @param title the title of the dialog
     * @return the dialog itself for method chaining
     */
    T title(String title);

    /**
     * Set the external title of the dialog, which is used for displaying the dialog in a multi-dialog dialog
     *
     * @param externalTitle the external title of the dialog
     * @return the dialog itself for method chaining
     */
    T externalTitle(@Nullable String externalTitle);

    /**
     * Set the option to allow the dialog to be closed with the escape key
     *
     * @param canCloseWithEscape whether the dialog can be closed with the escape key
     * @return the dialog itself for method chaining
     */
    T canCloseWithEscape(boolean canCloseWithEscape);

    /**
     * Set the option to pause the game while the dialog is open
     *
     * @param pause whether the dialog should pause the game
     * @return the dialog itself for method chaining
     */
    T pause(boolean pause);

    /**
     * Set the action to be performed after an action is taken in the dialog
     *
     * @param afterAction the action to be performed after an action is taken
     * @return the dialog itself for method chaining
     */
    T afterAction(AfterAction afterAction);

    /**
     * Set the body of the dialog
     *
     * @param bodyBuilder the body builder for the dialog
     * @return the dialog itself for method chaining
     */
    T body(Consumer<BB> bodyBuilder);

    /**
     * Set the body of the dialog using a collection of body builders
     *
     * @param bodyBuilders the collection of body builders for the dialog
     * @return the dialog itself for method chaining
     */
    T body(Collection<Consumer<BB>> bodyBuilders);

    /**
     * Set the input for the dialog
     *
     * @param key          the key for the input
     * @param inputBuilder the input builder for the dialog
     * @return the dialog itself for method chaining
     */
    T input(String key, Consumer<IB> inputBuilder);

    /**
     * Set the input for the dialog using a map of input builders
     *
     * @param inputBuilders the map of input builders for the dialog
     * @return the dialog itself for method chaining
     */
    T input(Map<String, Consumer<IB>> inputBuilders);

    /**
     * Create an opener for the dialog
     *
     * @return the dialog opener
     */
    DialogOpener opener();

    /**
     * The action to be performed after an action is taken in the dialog.
     * This can be used to determine whether the dialog should close, wait for a response,
     * or take no action.
     */
    enum AfterAction {
        /**
         * Close the dialog after the action is taken.
         */
        CLOSE,
        /**
         * Take no action after the action is taken.
         */
        NONE,
        /**
         * Wait for a response after the action is taken.
         */
        WAIT_FOR_RESPONSE
    }
}
