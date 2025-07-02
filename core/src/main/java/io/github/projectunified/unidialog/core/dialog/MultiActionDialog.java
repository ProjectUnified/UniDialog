package io.github.projectunified.unidialog.core.dialog;

import io.github.projectunified.unidialog.core.action.DialogAction;

import java.util.Collection;
import java.util.function.Consumer;

public interface MultiActionDialog<I, T extends MultiActionDialog<I, T>> extends Dialog<I, T> {
    T width(int width);

    <B extends DialogAction> T action(Consumer<B> action);

    <B extends DialogAction> T action(Collection<Consumer<B>> actions);

    <B extends DialogAction> T exitAction(Consumer<B> action);
}
