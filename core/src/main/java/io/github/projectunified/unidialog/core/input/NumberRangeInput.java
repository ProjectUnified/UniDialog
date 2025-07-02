package io.github.projectunified.unidialog.core.input;

public interface NumberRangeInput<T extends NumberRangeInput<T>> {
    T width(int width);

    T label(String label);

    T labelFormat(String labelFormat);

    T start(float start);

    T end(float end);

    T initial(float initial);

    T step(float step);
}
