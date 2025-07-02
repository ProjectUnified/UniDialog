package io.github.projectunified.unidialog.core.body;

public interface DialogBody<I> {
    <T extends ItemBody<I, T>> T item();

    <T extends TextBody<T>> T text();
}
