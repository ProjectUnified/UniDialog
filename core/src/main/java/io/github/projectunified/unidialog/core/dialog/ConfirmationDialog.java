package io.github.projectunified.unidialog.core.dialog;

import io.github.projectunified.unidialog.core.action.DialogActionTypes;

import java.util.function.Consumer;

public interface ConfirmationDialog<I, T extends ConfirmationDialog<I, T>> extends Dialog<I, T> {
    <B extends DialogActionTypes> T yesAction(Consumer<B> action);

    <B extends DialogActionTypes> T noAction(Consumer<B> action);
}
