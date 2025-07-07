package io.github.projectunified.unidialog.paper.dialog;

import io.github.projectunified.unidialog.core.dialog.MultiActionDialog;
import io.github.projectunified.unidialog.paper.action.PaperDialogActionBuilder;
import io.github.projectunified.unidialog.paper.body.PaperDialogBodyBuilder;
import io.github.projectunified.unidialog.paper.input.PaperDialogInputBuilder;
import io.papermc.paper.registry.data.dialog.ActionButton;
import io.papermc.paper.registry.data.dialog.type.DialogType;
import net.kyori.adventure.text.Component;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class PaperMultiActionDialog extends PaperDialog<PaperMultiActionDialog> implements MultiActionDialog<ItemStack, PaperDialogBodyBuilder, PaperDialogInputBuilder, PaperDialog<?>, PaperDialogActionBuilder, PaperMultiActionDialog> {
    private List<ActionButton> actions;
    private @Nullable ActionButton exitAction;
    private int columns;

    public PaperMultiActionDialog(String defaultNamespace, Function<String, Component> componentDeserializer) {
        super(defaultNamespace, componentDeserializer);
    }

    @Override
    public PaperMultiActionDialog columns(int columns) {
        this.columns = columns;
        return this;
    }

    @Override
    public PaperMultiActionDialog action(Consumer<PaperDialogActionBuilder> action) {
        if (actions == null) {
            actions = new ArrayList<>();
        }
        ActionButton actionButton = getAction(action);
        actions.add(actionButton);
        return this;
    }

    @Override
    public PaperMultiActionDialog exitAction(@Nullable Consumer<PaperDialogActionBuilder> action) {
        this.exitAction = action == null ? null : getAction(action);
        return this;
    }

    @Override
    protected DialogType getDialogType() {
        return DialogType.multiAction(
                actions != null ? actions : Collections.emptyList(),
                exitAction,
                columns > 0 ? columns : DEFAULT_COLUMNS
        );
    }
}
