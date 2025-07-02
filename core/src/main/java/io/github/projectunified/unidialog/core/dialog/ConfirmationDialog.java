package io.github.projectunified.unidialog.core.dialog;

import io.github.projectunified.unidialog.core.action.DialogAction;

import java.util.function.Consumer;

public interface ConfirmationDialog<I, T extends ConfirmationDialog<I, T>> extends Dialog<I, T> {
    <B extends DialogAction> T yesAction(Consumer<B> action);

    <B extends DialogAction> T noAction(Consumer<B> action);
}
