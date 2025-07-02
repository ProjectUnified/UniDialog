package io.github.projectunified.unidialog.core.body;

public interface DialogBodyTypes<I> {
    <T extends ItemBody<I, T>> T item();

    <T extends TextBody<T>> T text();
}
