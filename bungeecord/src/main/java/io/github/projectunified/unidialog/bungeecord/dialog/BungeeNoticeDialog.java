package io.github.projectunified.unidialog.bungeecord.dialog;

import io.github.projectunified.unidialog.bungeecord.action.BungeeDialogActionBuilder;
import io.github.projectunified.unidialog.bungeecord.body.BungeeDialogBodyBuilder;
import io.github.projectunified.unidialog.bungeecord.input.BungeeDialogInputBuilder;
import io.github.projectunified.unidialog.bungeecord.opener.BungeeDialogOpener;
import io.github.projectunified.unidialog.core.dialog.NoticeDialog;
import net.md_5.bungee.api.dialog.Dialog;
import net.md_5.bungee.api.dialog.DialogBase;
import net.md_5.bungee.api.dialog.action.ActionButton;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * The Bungee implementation of the notice dialog
 *
 * @param <O> the type of the dialog opener
 */
public class BungeeNoticeDialog<O extends BungeeDialogOpener> extends BungeeDialog<O, BungeeNoticeDialog<O>> implements NoticeDialog<Object, BungeeDialogBodyBuilder, BungeeDialogInputBuilder, BungeeDialog<?, ?>, BungeeDialogActionBuilder, BungeeNoticeDialog<O>> {
    private ActionButton action;

    /**
     * Create a new notice dialog
     *
     * @param defaultNamespace the default namespace for the dialog actions
     * @param openerFunction   the function to create the dialog opener from the Bungee dialog
     */
    public BungeeNoticeDialog(String defaultNamespace, Function<Dialog, O> openerFunction) {
        super(defaultNamespace, openerFunction);
    }

    @Override
    public BungeeNoticeDialog<O> action(Consumer<BungeeDialogActionBuilder> action) {
        this.action = getAction(action);
        return this;
    }

    @Override
    protected Dialog constructDialog(DialogBase dialogBase) {
        return new net.md_5.bungee.api.dialog.NoticeDialog(dialogBase, action);
    }
}
