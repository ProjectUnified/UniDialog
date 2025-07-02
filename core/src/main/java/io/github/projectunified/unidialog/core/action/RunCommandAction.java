package io.github.projectunified.unidialog.core.action;

public interface RunCommandAction<T extends RunCommandAction<T>> {
    T command(String command);
}
