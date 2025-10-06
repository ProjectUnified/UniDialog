package io.github.projectunified.unidialog.core;

import io.github.projectunified.unidialog.core.action.DialogActionBuilder;
import io.github.projectunified.unidialog.core.body.DialogBodyBuilder;
import io.github.projectunified.unidialog.core.dialog.*;
import io.github.projectunified.unidialog.core.input.DialogInputBuilder;
import io.github.projectunified.unidialog.core.payload.DialogPayload;

import java.util.Map;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * The main interface for managing dialogs in the UniDialog framework.
 * It provides methods to create different types of dialogs, register and unregister custom actions,
 * and manage dialog instances.
 * This interface is generic and can be used with various types of dialog builders and input builders.
 *
 * @param <I>  the type of the item for the item body
 * @param <BB> the type of the dialog body builder
 * @param <IB> the type of the dialog input builder
 * @param <D>  the type of the base dialog
 * @param <AB> the type of the dialog action builder
 */
public interface DialogManager<I, BB extends DialogBodyBuilder<I>, IB extends DialogInputBuilder, D extends Dialog<I, BB, IB, ?>, AB extends DialogActionBuilder<D, AB>> {
    /**
     * Create a confirmation dialog
     *
     * @param <T> the type of the confirmation dialog, extending ConfirmationDialog
     * @return a new instance of ConfirmationDialog
     */
    <T extends ConfirmationDialog<I, BB, IB, D, AB, T>> T createConfirmationDialog();

    /**
     * Create a multi-action dialog
     *
     * @param <T> the type of the multi-action dialog, extending MultiActionDialog
     * @return a new instance of MultiActionDialog
     */
    <T extends MultiActionDialog<I, BB, IB, D, AB, T>> T createMultiActionDialog();

    /**
     * Create a server links dialog
     *
     * @param <T> the type of the server links dialog, extending ServerLinksDialog
     * @return a new instance of ServerLinksDialog
     */
    <T extends ServerLinksDialog<I, BB, IB, D, AB, T>> T createServerLinksDialog();

    /**
     * Create a notice dialog
     *
     * @param <T> the type of the notice dialog, extending NoticeDialog
     * @return a new instance of NoticeDialog
     */
    <T extends NoticeDialog<I, BB, IB, D, AB, T>> T createNoticeDialog();

    /**
     * Create a dialog list dialog
     *
     * @param <T> the type of the dialog list dialog, extending DialogListDialog
     * @return a new instance of DialogListDialog
     */
    <T extends DialogListDialog<I, BB, IB, D, AB, T>> T createDialogListDialog();

    /**
     * Register the dialog manager
     */
    void register();

    /**
     * Unregister the dialog manager
     */
    void unregister();

    /**
     * Register a custom action with a unique identifier.
     *
     * @param id     the unique identifier for the custom action
     * @param action the action to be executed, taking a payload
     */
    void registerCustomAction(String id, Consumer<DialogPayload> action);

    /**
     * Register a custom action with a unique identifier.
     *
     * @param id     the unique identifier for the custom action
     * @param action the action to be executed, taking a payload
     */
    void registerCustomAction(String namespace, String id, Consumer<DialogPayload> action);

    /**
     * Register a custom action with a unique identifier.
     *
     * @param id     the unique identifier for the custom action
     * @param action the action to be executed, taking a UUID and a map of parameters
     */
    default void registerCustomAction(String id, BiConsumer<UUID, Map<String, String>> action) {
        registerCustomAction(id, payload -> action.accept(payload.owner(), payload.map()));
    }

    /**
     * Register a custom action with a namespace and a unique identifier.
     *
     * @param namespace the namespace for the custom action
     * @param id        the unique identifier for the custom action
     * @param action    the action to be executed, taking a UUID and a map of parameters
     */
    default void registerCustomAction(String namespace, String id, BiConsumer<UUID, Map<String, String>> action) {
        registerCustomAction(namespace, id, payload -> action.accept(payload.owner(), payload.map()));
    }

    /**
     * Unregister a custom action by its unique identifier.
     *
     * @param id the unique identifier of the custom action to be unregistered
     */
    void unregisterCustomAction(String id);

    /**
     * Unregister a custom action by its namespace and unique identifier.
     *
     * @param namespace the namespace of the custom action
     * @param id        the unique identifier of the custom action to be unregistered
     */
    void unregisterCustomAction(String namespace, String id);

    /**
     * Unregister all custom actions.
     */
    void unregisterAllCustomActions();

    /**
     * Clear the current dialog associated with the given player UUID.
     *
     * @param uuid the unique identifier of the player whose dialog should be cleared
     * @return true if the dialog was successfully cleared, false otherwise
     */
    boolean clearDialog(UUID uuid);
}
