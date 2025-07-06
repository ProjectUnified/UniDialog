package io.github.projectunified.unidialog.spigot.dialog;

import io.github.projectunified.unidialog.core.dialog.NoticeDialog;
import io.github.projectunified.unidialog.spigot.action.SpigotDialogActionBuilder;
import io.github.projectunified.unidialog.spigot.body.SpigotDialogBodyBuilder;
import io.github.projectunified.unidialog.spigot.input.SpigotDialogInputBuilder;
import net.md_5.bungee.api.dialog.Dialog;
import net.md_5.bungee.api.dialog.DialogBase;
import net.md_5.bungee.api.dialog.action.ActionButton;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

public class SpigotNoticeDialog extends SpigotDialog<SpigotNoticeDialog> implements NoticeDialog<ItemStack, SpigotDialogBodyBuilder, SpigotDialogInputBuilder, SpigotDialog<?>, SpigotDialogActionBuilder, SpigotNoticeDialog> {
    private ActionButton action;

    public SpigotNoticeDialog(String defaultNamespace) {
        super(defaultNamespace);
    }

    @Override
    public SpigotNoticeDialog action(Consumer<SpigotDialogActionBuilder> action) {
        this.action = getAction(action);
        return this;
    }

    @Override
    protected Dialog constructDialog(DialogBase dialogBase) {
        return new net.md_5.bungee.api.dialog.NoticeDialog(dialogBase, action);
    }
}
