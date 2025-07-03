package io.github.projectunified.unidialog.core.action;

public interface DialogActionBuilder {
    <T extends CopyToClipboardAction<T>> T copyToClipboard();

    <T extends DynamicCustomAction<T>> T dynamicCustom();

    <T extends DynamicRunCommandAction<T>> T dynamicRunCommand();

    <B extends CopyToClipboardAction<B>> B copyToClipboard();

    <B extends DynamicCustomAction<B>> B dynamicCustom();

    <B extends DynamicRunCommandAction<B>> B dynamicRunCommand();

    <B extends OpenUrlAction<B>> B openUrl();

    <B extends RunCommandAction<B>> B runCommand();

    <B extends SuggestCommandAction<B>> B suggestCommand();
}
