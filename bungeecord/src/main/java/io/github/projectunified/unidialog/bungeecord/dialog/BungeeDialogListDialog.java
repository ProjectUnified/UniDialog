package io.github.projectunified.unidialog.bungeecord.dialog;

import io.github.projectunified.unidialog.bungeecord.action.BungeeDialogActionBuilder;
import io.github.projectunified.unidialog.bungeecord.body.BungeeDialogBodyBuilder;
import io.github.projectunified.unidialog.bungeecord.input.BungeeDialogInputBuilder;
import io.github.projectunified.unidialog.bungeecord.opener.BungeeDialogOpener;
import io.github.projectunified.unidialog.core.dialog.DialogListDialog;
import io.github.projectunified.unidialog.core.opener.DialogOpener;
import net.md_5.bungee.api.dialog.Dialog;
import net.md_5.bungee.api.dialog.DialogBase;
import net.md_5.bungee.api.dialog.action.ActionButton;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * The Bungee implementation of the dialog list dialog
 *
 * @param <O> the type of the dialog opener
 */
public class BungeeDialogListDialog<O extends BungeeDialogOpener> extends BungeeDialog<O, BungeeDialogListDialog<O>> implements DialogListDialog<Object, BungeeDialogBodyBuilder, BungeeDialogInputBuilder, BungeeDialog<?, ?>, BungeeDialogActionBuilder, BungeeDialogListDialog<O>> {
    private List<Dialog> dialogs;
    private @Nullable ActionButton exitAction;
    private int columns;
    private int buttonWidth;

    /**
     * Create a new dialog list dialog
     *
     * @param defaultNamespace the default namespace for the dialog actions
     * @param openerFunction   the function to create the dialog opener from the Bungee dialog
     */
    public BungeeDialogListDialog(String defaultNamespace, Function<Dialog, O> openerFunction) {
        super(defaultNamespace, openerFunction);
    }

    private void addDialog(Dialog dialog) {
        if (dialogs == null) {
            dialogs = new ArrayList<>();
        }
        dialogs.add(dialog);
    }

    @Override
    public BungeeDialogListDialog<O> dialog(BungeeDialog<?, ?> dialog) {
        addDialog(dialog.getDialog());
        return this;
    }

    @Override
    public BungeeDialogListDialog<O> dialog(String namespace, String dialogId) {
        throw new UnsupportedOperationException("DialogListDialog does not support dialog by namespace and ID in Bungee.");
    }

    @Override
    public BungeeDialogListDialog<O> dialog(DialogOpener dialogOpener) {
        if (!(dialogOpener instanceof BungeeDialogOpener bungeeDialogOpener)) {
            throw new IllegalArgumentException("Dialog opener must be an instance of BungeeDialogOpener.");
        }
        addDialog(bungeeDialogOpener.getDialog());
        return this;
    }

    @Override
    public BungeeDialogListDialog<O> exitAction(@Nullable Consumer<BungeeDialogActionBuilder> action) {
        this.exitAction = action == null ? null : getAction(action);
        return this;
    }

    @Override
    public BungeeDialogListDialog<O> columns(int columns) {
        this.columns = columns;
        return this;
    }

    @Override
    public BungeeDialogListDialog<O> buttonWidth(int buttonWidth) {
        this.buttonWidth = buttonWidth;
        return this;
    }

    @Override
    protected Dialog constructDialog(DialogBase dialogBase) {
        return new net.md_5.bungee.api.dialog.DialogListDialog(
                dialogBase,
                dialogs != null ? dialogs : Collections.emptyList(),
                exitAction,
                columns > 0 ? columns : DEFAULT_COLUMNS,
                buttonWidth > 0 ? buttonWidth : DEFAULT_BUTTON_WIDTH
        );
    }
}
