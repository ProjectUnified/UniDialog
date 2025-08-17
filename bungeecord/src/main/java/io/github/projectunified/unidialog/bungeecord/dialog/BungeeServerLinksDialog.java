package io.github.projectunified.unidialog.bungeecord.dialog;

import io.github.projectunified.unidialog.bungeecord.action.BungeeDialogActionBuilder;
import io.github.projectunified.unidialog.bungeecord.body.BungeeDialogBodyBuilder;
import io.github.projectunified.unidialog.bungeecord.input.BungeeDialogInputBuilder;
import io.github.projectunified.unidialog.bungeecord.opener.BungeeDialogOpener;
import io.github.projectunified.unidialog.core.dialog.ServerLinksDialog;
import net.md_5.bungee.api.dialog.Dialog;
import net.md_5.bungee.api.dialog.DialogBase;
import net.md_5.bungee.api.dialog.action.ActionButton;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;
import java.util.function.Function;

public class BungeeServerLinksDialog<O extends BungeeDialogOpener> extends BungeeDialog<O, BungeeServerLinksDialog<O>> implements ServerLinksDialog<Object, BungeeDialogBodyBuilder, BungeeDialogInputBuilder, BungeeDialog<?, ?>, BungeeDialogActionBuilder, BungeeServerLinksDialog<O>> {
    private @Nullable ActionButton exitAction;
    private int columns;
    private int buttonWidth;

    public BungeeServerLinksDialog(String defaultNamespace, Function<Dialog, O> openerFunction) {
        super(defaultNamespace, openerFunction);
    }

    @Override
    public BungeeServerLinksDialog<O> exitAction(@Nullable Consumer<BungeeDialogActionBuilder> action) {
        this.exitAction = action == null ? null : getAction(action);
        return this;
    }

    @Override
    public BungeeServerLinksDialog<O> columns(int columns) {
        this.columns = columns;
        return this;
    }

    @Override
    public BungeeServerLinksDialog<O> buttonWidth(int buttonWidth) {
        this.buttonWidth = buttonWidth;
        return this;
    }

    @Override
    protected Dialog constructDialog(DialogBase dialogBase) {
        return new net.md_5.bungee.api.dialog.ServerLinksDialog(
                dialogBase,
                exitAction,
                columns > 0 ? columns : DEFAULT_COLUMNS,
                buttonWidth > 0 ? buttonWidth : DEFAULT_BUTTON_WIDTH
        );
    }
}
