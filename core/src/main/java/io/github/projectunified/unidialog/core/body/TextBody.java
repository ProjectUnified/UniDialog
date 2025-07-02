package io.github.projectunified.unidialog.core.body;

public interface TextBody<T extends TextBody<T>> {
    T text(String text);

    T width(int width);
}
