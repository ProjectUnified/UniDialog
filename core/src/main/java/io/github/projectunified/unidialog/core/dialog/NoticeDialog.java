package io.github.projectunified.unidialog.core.dialog;

import io.github.projectunified.unidialog.core.action.DialogActionBuilder;

import java.util.function.Consumer;

public interface NoticeDialog<I, T extends NoticeDialog<I, T>> extends Dialog<I, T> {
    <B extends DialogActionBuilder> T action(Consumer<B> action);
}
