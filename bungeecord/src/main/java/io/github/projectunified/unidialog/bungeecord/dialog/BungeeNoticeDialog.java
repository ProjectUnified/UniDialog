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

public class BungeeNoticeDialog extends BungeeDialog<BungeeNoticeDialog> implements NoticeDialog<Object, BungeeDialogBodyBuilder, BungeeDialogInputBuilder, BungeeDialog<?>, BungeeDialogActionBuilder, BungeeNoticeDialog> {
    private ActionButton action;

    public BungeeNoticeDialog(String defaultNamespace, Function<Dialog, BungeeDialogOpener> openerFunction) {
        super(defaultNamespace, openerFunction);
    }

    @Override
    public BungeeNoticeDialog action(Consumer<BungeeDialogActionBuilder> action) {
        this.action = getAction(action);
        return this;
    }

    @Override
    protected Dialog constructDialog(DialogBase dialogBase) {
        return new net.md_5.bungee.api.dialog.NoticeDialog(dialogBase, action);
    }
}
