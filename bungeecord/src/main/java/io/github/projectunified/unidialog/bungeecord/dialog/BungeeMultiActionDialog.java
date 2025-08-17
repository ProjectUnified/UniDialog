package io.github.projectunified.unidialog.bungeecord.dialog;

import io.github.projectunified.unidialog.bungeecord.action.BungeeDialogActionBuilder;
import io.github.projectunified.unidialog.bungeecord.body.BungeeDialogBodyBuilder;
import io.github.projectunified.unidialog.bungeecord.input.BungeeDialogInputBuilder;
import io.github.projectunified.unidialog.bungeecord.opener.BungeeDialogOpener;
import io.github.projectunified.unidialog.core.dialog.MultiActionDialog;
import net.md_5.bungee.api.dialog.Dialog;
import net.md_5.bungee.api.dialog.DialogBase;
import net.md_5.bungee.api.dialog.action.ActionButton;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class BungeeMultiActionDialog extends BungeeDialog<BungeeMultiActionDialog> implements MultiActionDialog<Object, BungeeDialogBodyBuilder, BungeeDialogInputBuilder, BungeeDialog<?>, BungeeDialogActionBuilder, BungeeMultiActionDialog> {
    private int columns;
    private List<ActionButton> actions;
    private @Nullable ActionButton exitAction;

    public BungeeMultiActionDialog(String defaultNamespace, Function<Dialog, BungeeDialogOpener> openerFunction) {
        super(defaultNamespace, openerFunction);
    }

    @Override
    public BungeeMultiActionDialog columns(int columns) {
        this.columns = columns;
        return this;
    }

    @Override
    public BungeeMultiActionDialog action(Consumer<BungeeDialogActionBuilder> action) {
        if (actions == null) {
            actions = new ArrayList<>();
        }
        ActionButton actionButton = getAction(action);
        actions.add(actionButton);
        return this;
    }

    @Override
    public BungeeMultiActionDialog exitAction(@Nullable Consumer<BungeeDialogActionBuilder> action) {
        this.exitAction = action == null ? null : getAction(action);
        return this;
    }

    @Override
    protected Dialog constructDialog(DialogBase dialogBase) {
        return new net.md_5.bungee.api.dialog.MultiActionDialog(
                dialogBase,
                actions != null ? actions : Collections.emptyList(),
                columns > 0 ? columns : DEFAULT_COLUMNS,
                exitAction
        );
    }
}
