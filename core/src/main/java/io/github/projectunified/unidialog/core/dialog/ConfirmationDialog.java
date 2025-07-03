package io.github.projectunified.unidialog.core.dialog;

import io.github.projectunified.unidialog.core.action.DialogActionBuilder;
import io.github.projectunified.unidialog.core.body.DialogBodyBuilder;
import io.github.projectunified.unidialog.core.input.DialogInputBuilder;

import java.util.function.Consumer;

public interface ConfirmationDialog<I, BB extends DialogBodyBuilder<I>, IB extends DialogInputBuilder, AB extends DialogActionBuilder<AB>, T extends ConfirmationDialog<I, BB, IB, AB, T>> extends Dialog<I, BB, IB, T> {
    T yesAction(Consumer<AB> action);

    T noAction(Consumer<AB> action);

    boolean hasYesAction();

    boolean hasNoAction();
}
