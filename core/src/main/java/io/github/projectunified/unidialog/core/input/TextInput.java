package io.github.projectunified.unidialog.core.input;

import org.jetbrains.annotations.Nullable;

public interface TextInput<T extends TextInput<T>> {
    T width(int width);

    T label(@Nullable String label);

    T initial(String initial);

    T maxLength(int maxLength);

    T maxLines(@Nullable Integer maxLines);

    T height(@Nullable Integer height);
}
