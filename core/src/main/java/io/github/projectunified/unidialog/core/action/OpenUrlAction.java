package io.github.projectunified.unidialog.core.action;

public interface OpenUrlAction<T extends OpenUrlAction<T>> {
    T url(String url);
}
