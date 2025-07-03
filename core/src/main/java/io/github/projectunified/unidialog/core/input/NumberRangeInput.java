package io.github.projectunified.unidialog.core.input;

import org.jetbrains.annotations.Nullable;

public interface NumberRangeInput<T extends NumberRangeInput<T>> {
    T width(int width);

    T label(String label);

    T labelFormat(String labelFormat);

    T start(float start);

    T end(float end);

    T initial(@Nullable Float initial);

    T step(@Nullable Float step);
}
