package io.github.projectunified.unidialog.core.action;

public interface DynamicRunCommandAction<T extends DynamicRunCommandAction<T>> {
    T template(String template);
}
