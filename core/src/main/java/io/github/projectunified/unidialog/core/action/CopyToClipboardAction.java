package io.github.projectunified.unidialog.core.action;

public interface CopyToClipboardAction<T extends CopyToClipboardAction<T>> {
    T value(String value);
}
