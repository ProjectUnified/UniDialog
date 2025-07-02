package io.github.projectunified.unidialog.core.input;

public interface BooleanInput<T extends BooleanInput<T>> {
    T label(String label);

    T initial(boolean initial);

    T onTrue(String onTrue);

    T onFalse(String onFalse);
}
