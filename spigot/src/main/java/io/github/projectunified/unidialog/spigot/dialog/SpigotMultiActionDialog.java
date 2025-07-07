package io.github.projectunified.unidialog.spigot.dialog;

import io.github.projectunified.unidialog.core.dialog.MultiActionDialog;
import io.github.projectunified.unidialog.spigot.action.SpigotDialogActionBuilder;
import io.github.projectunified.unidialog.spigot.body.SpigotDialogBodyBuilder;
import io.github.projectunified.unidialog.spigot.input.SpigotDialogInputBuilder;
import net.md_5.bungee.api.dialog.Dialog;
import net.md_5.bungee.api.dialog.DialogBase;
import net.md_5.bungee.api.dialog.action.ActionButton;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class SpigotMultiActionDialog extends SpigotDialog<SpigotMultiActionDialog> implements MultiActionDialog<ItemStack, SpigotDialogBodyBuilder, SpigotDialogInputBuilder, SpigotDialog<?>, SpigotDialogActionBuilder, SpigotMultiActionDialog> {
    private int columns;
    private List<ActionButton> actions;
    private @Nullable ActionButton exitAction;

    public SpigotMultiActionDialog(String defaultNamespace) {
        super(defaultNamespace);
    }

    @Override
    public SpigotMultiActionDialog columns(int columns) {
        this.columns = columns;
        return this;
    }

    @Override
    public SpigotMultiActionDialog action(Consumer<SpigotDialogActionBuilder> action) {
        if (actions == null) {
            actions = new ArrayList<>();
        }
        ActionButton actionButton = getAction(action);
        actions.add(actionButton);
        return this;
    }

    @Override
    public SpigotMultiActionDialog exitAction(@Nullable Consumer<SpigotDialogActionBuilder> action) {
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
