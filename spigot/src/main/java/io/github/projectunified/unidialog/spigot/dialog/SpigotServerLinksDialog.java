package io.github.projectunified.unidialog.spigot.dialog;

import io.github.projectunified.unidialog.core.dialog.ServerLinksDialog;
import io.github.projectunified.unidialog.spigot.action.SpigotDialogActionBuilder;
import io.github.projectunified.unidialog.spigot.body.SpigotDialogBodyBuilder;
import io.github.projectunified.unidialog.spigot.input.SpigotDialogInputBuilder;
import net.md_5.bungee.api.dialog.Dialog;
import net.md_5.bungee.api.dialog.DialogBase;
import net.md_5.bungee.api.dialog.action.ActionButton;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class SpigotServerLinksDialog extends SpigotDialog<SpigotServerLinksDialog> implements ServerLinksDialog<ItemStack, SpigotDialogBodyBuilder, SpigotDialogInputBuilder, SpigotDialog<?>, SpigotDialogActionBuilder, SpigotServerLinksDialog> {
    private @Nullable ActionButton exitAction;
    private int columns;
    private int buttonWidth;

    public SpigotServerLinksDialog(String defaultNamespace) {
        super(defaultNamespace);
    }

    @Override
    public SpigotServerLinksDialog exitAction(@Nullable Consumer<SpigotDialogActionBuilder> action) {
        this.exitAction = action == null ? null : getAction(action);
        return this;
    }

    @Override
    public SpigotServerLinksDialog columns(int columns) {
        this.columns = columns;
        return this;
    }

    @Override
    public SpigotServerLinksDialog buttonWidth(int buttonWidth) {
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
