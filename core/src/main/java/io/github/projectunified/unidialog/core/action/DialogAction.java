package io.github.projectunified.unidialog.core.action;

public interface DialogAction {
    <T extends CopyToClipboardAction<T>> T copyToClipboard();

    <T extends DynamicCustomAction<T>> T dynamicCustom();

    <T extends DynamicRunCommandAction<T>> T dynamicRunCommand();

    <T extends OpenUrlAction<T>> T openUrl();

    <T extends RunCommandAction<T>> T runCommand();
}
