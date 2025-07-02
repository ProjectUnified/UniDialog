package io.github.projectunified.unidialog.core.input;

public interface DialogInputTypes {
    <T extends BooleanInput<T>> T booleanInput();

    <T extends TextInput<T>> T textInput();

    <T extends SingleOptionInput<T>> T singleOptionInput();

    <T extends NumberRangeInput<T>> T numberRangeInput();
}
