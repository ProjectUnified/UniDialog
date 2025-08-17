package io.github.projectunified.unidialog.bungeecord.dialog;

import io.github.projectunified.unidialog.bungeecord.action.BungeeDialogActionBuilder;
import io.github.projectunified.unidialog.bungeecord.body.BungeeDialogBodyBuilder;
import io.github.projectunified.unidialog.bungeecord.input.BungeeDialogInputBuilder;
import io.github.projectunified.unidialog.bungeecord.opener.BungeeDialogOpener;
import io.github.projectunified.unidialog.core.dialog.ConfirmationDialog;
import net.md_5.bungee.api.dialog.Dialog;
import net.md_5.bungee.api.dialog.DialogBase;
import net.md_5.bungee.api.dialog.action.ActionButton;

import java.util.function.Consumer;
import java.util.function.Function;

public class BungeeConfirmationDialog<O extends BungeeDialogOpener> extends BungeeDialog<O, BungeeConfirmationDialog<O>> implements ConfirmationDialog<Object, BungeeDialogBodyBuilder, BungeeDialogInputBuilder, BungeeDialog<?, ?>, BungeeDialogActionBuilder, BungeeConfirmationDialog<O>> {
    private ActionButton yesAction;
    private ActionButton noAction;

    public BungeeConfirmationDialog(String defaultNamespace, Function<Dialog, O> openerFunction) {
        super(defaultNamespace, openerFunction);
    }

    @Override
    public BungeeConfirmationDialog<O> yesAction(Consumer<BungeeDialogActionBuilder> action) {
        this.yesAction = getAction(action);
        return this;
    }

    @Override
    public BungeeConfirmationDialog<O> noAction(Consumer<BungeeDialogActionBuilder> action) {
        this.noAction = getAction(action);
        return this;
    }

    @Override
    public boolean hasYesAction() {
        return yesAction != null;
    }

    @Override
    public boolean hasNoAction() {
        return noAction != null;
    }

    @Override
    protected Dialog constructDialog(DialogBase dialogBase) {
        return new net.md_5.bungee.api.dialog.ConfirmationDialog(dialogBase, yesAction, noAction);
    }
}
