package io.github.projectunified.unidialog.core.dialog;

import io.github.projectunified.unidialog.core.action.DialogActionBuilder;

import java.util.Collection;
import java.util.function.Consumer;

public interface MultiActionDialog<I, T extends MultiActionDialog<I, T>> extends Dialog<I, T> {
    T width(int width);

    <B extends DialogActionBuilder> T action(Consumer<B> action);

    <B extends DialogActionBuilder> T action(Collection<Consumer<B>> actions);

    <B extends DialogActionBuilder> T exitAction(Consumer<B> action);
}
