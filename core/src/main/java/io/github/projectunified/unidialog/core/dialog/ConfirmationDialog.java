package io.github.projectunified.unidialog.core.dialog;

import io.github.projectunified.unidialog.core.action.DialogActionBuilder;

import java.util.function.Consumer;

public interface ConfirmationDialog<I, T extends ConfirmationDialog<I, T>> extends Dialog<I, T> {
    <B extends DialogActionBuilder> T yesAction(Consumer<B> action);

    <B extends DialogActionBuilder> T noAction(Consumer<B> action);
}
