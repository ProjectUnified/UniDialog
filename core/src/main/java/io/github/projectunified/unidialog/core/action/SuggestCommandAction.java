package io.github.projectunified.unidialog.core.action;

public interface SuggestCommandAction<T extends SuggestCommandAction<T>> {
    T command(String command);
}
