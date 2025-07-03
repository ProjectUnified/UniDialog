package io.github.projectunified.unidialog.core.dialog;

import io.github.projectunified.unidialog.core.action.DialogActionBuilder;
import io.github.projectunified.unidialog.core.body.DialogBodyBuilder;
import io.github.projectunified.unidialog.core.input.DialogInputBuilder;

import java.util.function.Consumer;

public interface ServerLinksDialog<I, BB extends DialogBodyBuilder<BB>, IB extends DialogInputBuilder, AB extends DialogActionBuilder, T extends ServerLinksDialog<I, BB, IB, AB, T>> extends Dialog<I, BB, IB, T> {
    T exitAction(Consumer<DialogActionBuilder> action);

    T columns(int columns);

    T buttonWidth(int buttonWidth);
}
