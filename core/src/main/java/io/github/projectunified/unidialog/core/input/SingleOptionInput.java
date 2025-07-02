package io.github.projectunified.unidialog.core.input;

public interface SingleOptionInput<T extends SingleOptionInput<T>> {
    T width(int width);

    T label(String label);

    T option(String id, String display, boolean isDefault);

    default T option(String id, String display) {
        return option(id, display, false);
    }
}
