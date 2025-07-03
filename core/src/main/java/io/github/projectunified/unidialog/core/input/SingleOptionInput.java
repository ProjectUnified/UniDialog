package io.github.projectunified.unidialog.core.input;

import org.jetbrains.annotations.Nullable;

public interface SingleOptionInput<T extends SingleOptionInput<T>> {
    T width(int width);

    T label(@Nullable String label);

    T option(String id, String display, boolean isDefault);

    default T option(String id, String display) {
        return option(id, display, false);
    }
}
