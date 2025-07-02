package io.github.projectunified.unidialog.core.input;

public interface TextInput<T extends TextInput<T>> {
    T width(int width);

    T label(String label);

    T labelVisible(boolean visible);

    T initial(String initial);

    T maxLength(int maxLength);

    T maxLines(int maxLines);

    T height(int height);
}
