package io.github.projectunified.unidialog.core.dialog;

import io.github.projectunified.unidialog.core.action.DialogActionBuilder;
import io.github.projectunified.unidialog.core.body.DialogBodyBuilder;
import io.github.projectunified.unidialog.core.input.DialogInputBuilder;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.function.Consumer;

public interface MultiActionDialog<I, BB extends DialogBodyBuilder<I>, IB extends DialogInputBuilder, AB extends DialogActionBuilder<AB>, T extends MultiActionDialog<I, BB, IB, AB, T>> extends Dialog<I, BB, IB, T> {
    T columns(int columns);

    T action(Consumer<AB> action);

    T action(Collection<Consumer<AB>> actions);

    T exitAction(@Nullable Consumer<AB> action);
}
