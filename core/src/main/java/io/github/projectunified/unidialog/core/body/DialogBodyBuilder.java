package io.github.projectunified.unidialog.core.body;

public interface DialogBodyBuilder<I> {
    <B extends TextBody<B>, T extends ItemBody<I, B, T>> T item();

    <T extends TextBody<T>> T text();
}
