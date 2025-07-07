package io.github.projectunified.unidialog.spigot.dialog;

import io.github.projectunified.unidialog.core.dialog.DialogListDialog;
import io.github.projectunified.unidialog.core.opener.DialogOpener;
import io.github.projectunified.unidialog.spigot.action.SpigotDialogActionBuilder;
import io.github.projectunified.unidialog.spigot.body.SpigotDialogBodyBuilder;
import io.github.projectunified.unidialog.spigot.input.SpigotDialogInputBuilder;
import io.github.projectunified.unidialog.spigot.opener.SpigotDialogOpener;
import net.md_5.bungee.api.dialog.Dialog;
import net.md_5.bungee.api.dialog.DialogBase;
import net.md_5.bungee.api.dialog.action.ActionButton;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class SpigotDialogListDialog extends SpigotDialog<SpigotDialogListDialog> implements DialogListDialog<ItemStack, SpigotDialogBodyBuilder, SpigotDialogInputBuilder, SpigotDialog<?>, SpigotDialogActionBuilder, SpigotDialogListDialog> {
    private List<Dialog> dialogs;
    private @Nullable ActionButton exitAction;
    private int columns;
    private int buttonWidth;

    public SpigotDialogListDialog(String defaultNamespace) {
        super(defaultNamespace);
    }

    private void addDialog(Dialog dialog) {
        if (dialogs == null) {
            dialogs = new ArrayList<>();
        }
        dialogs.add(dialog);
    }

    @Override
    public SpigotDialogListDialog dialog(SpigotDialog<?> dialog) {
        addDialog(dialog.getDialog());
        return this;
    }

    @Override
    public SpigotDialogListDialog dialog(String namespace, String dialogId) {
        throw new UnsupportedOperationException("DialogListDialog does not support dialog by namespace and ID in Spigot.");
    }

    @Override
    public SpigotDialogListDialog dialog(DialogOpener dialogOpener) {
        if (!(dialogOpener instanceof SpigotDialogOpener)) {
            throw new IllegalArgumentException("Dialog opener must be an instance of SpigotDialogOpener.");
        }
        addDialog(((SpigotDialogOpener) dialogOpener).dialog());
        return this;
    }

    @Override
    public SpigotDialogListDialog exitAction(@Nullable Consumer<SpigotDialogActionBuilder> action) {
        this.exitAction = action == null ? null : getAction(action);
        return this;
    }

    @Override
    public SpigotDialogListDialog columns(int columns) {
        this.columns = columns;
        return this;
    }

    @Override
    public SpigotDialogListDialog buttonWidth(int buttonWidth) {
        this.buttonWidth = buttonWidth;
        return this;
    }

    @Override
    protected Dialog constructDialog(DialogBase dialogBase) {
        return new net.md_5.bungee.api.dialog.DialogListDialog(
                dialogBase,
                dialogs != null ? dialogs : Collections.emptyList(),
                exitAction,
                columns > 0 ? columns : 2,
                buttonWidth > 0 ? buttonWidth : 150
        );
    }
}
