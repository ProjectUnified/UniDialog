package io.github.projectunified.unidialog.core.action;

import org.jetbrains.annotations.Nullable;

public interface DialogActionBuilder<T extends DialogActionBuilder<T>> {
    T label(String label);

    T tooltip(@Nullable String tooltip);

    T width(int width);

    T copyToClipboard(String value);

    T dynamicCustom(String id);

    T dynamicCustom(String namespace, String id);

    T dynamicRunCommand(String template);

    T openUrl(String url);

    T runCommand(String command);

    T suggestCommand(String command);
}
