package io.github.projectunified.unidialog.core.dialog;

import io.github.projectunified.unidialog.core.action.DialogAction;

public interface ServerLinksDialog<I, T extends ServerLinksDialog<I, T>> extends Dialog<I, T> {
    <B extends DialogAction> T exitAction(B exitAction);

    T columns(int columns);

    T buttonWidth(int buttonWidth);
}
