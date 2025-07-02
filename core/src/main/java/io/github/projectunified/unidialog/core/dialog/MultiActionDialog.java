package io.github.projectunified.unidialog.core.dialog;

import io.github.projectunified.unidialog.core.action.DialogActionTypes;

import java.util.Collection;
import java.util.function.Consumer;

public interface MultiActionDialog<I, T extends MultiActionDialog<I, T>> extends Dialog<I, T> {
    T width(int width);

    <B extends DialogActionTypes> T action(Consumer<B> action);

    <B extends DialogActionTypes> T action(Collection<Consumer<B>> actions);

    <B extends DialogActionTypes> T exitAction(Consumer<B> action);
}
