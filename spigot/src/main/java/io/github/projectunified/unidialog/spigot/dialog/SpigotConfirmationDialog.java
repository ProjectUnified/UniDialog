package io.github.projectunified.unidialog.spigot.dialog;

import io.github.projectunified.unidialog.core.dialog.ConfirmationDialog;
import io.github.projectunified.unidialog.spigot.action.SpigotDialogActionBuilder;
import io.github.projectunified.unidialog.spigot.body.SpigotDialogBodyBuilder;
import io.github.projectunified.unidialog.spigot.input.SpigotDialogInputBuilder;
import net.md_5.bungee.api.dialog.Dialog;
import net.md_5.bungee.api.dialog.DialogBase;
import net.md_5.bungee.api.dialog.action.ActionButton;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

public class SpigotConfirmationDialog extends SpigotDialog<SpigotConfirmationDialog> implements ConfirmationDialog<ItemStack, SpigotDialogBodyBuilder, SpigotDialogInputBuilder, SpigotDialog<?>, SpigotDialogActionBuilder, SpigotConfirmationDialog> {
    private ActionButton yesAction;
    private ActionButton noAction;

    public SpigotConfirmationDialog(String defaultNamespace) {
        super(defaultNamespace);
    }

    @Override
    public SpigotConfirmationDialog yesAction(Consumer<SpigotDialogActionBuilder> action) {
        this.yesAction = getAction(action);
        return this;
    }

    @Override
    public SpigotConfirmationDialog noAction(Consumer<SpigotDialogActionBuilder> action) {
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
