package io.github.projectunified.unidialog.core.action;

public interface DynamicCustomAction<T extends DynamicCustomAction<T>> {
    T id(String id);

    T namespacedId(String namespace, String id);
}
