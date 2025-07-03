package io.github.projectunified.unidialog.core.action;

import org.jetbrains.annotations.Nullable;

public interface DialogActionBuilder<T extends DialogActionBuilder<T>> {
    T label(String label);

    T tooltip(@Nullable String tooltip);

    T width(int width);

    <B extends CopyToClipboardAction<B>> B copyToClipboard();

    <B extends DynamicCustomAction<B>> B dynamicCustom();

    <B extends DynamicRunCommandAction<B>> B dynamicRunCommand();

    <B extends OpenUrlAction<B>> B openUrl();

    <B extends RunCommandAction<B>> B runCommand();

    <B extends SuggestCommandAction<B>> B suggestCommand();
}
