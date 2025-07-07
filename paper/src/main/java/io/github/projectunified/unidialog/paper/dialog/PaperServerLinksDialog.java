package io.github.projectunified.unidialog.paper.dialog;

import io.github.projectunified.unidialog.core.dialog.ServerLinksDialog;
import io.github.projectunified.unidialog.paper.action.PaperDialogActionBuilder;
import io.github.projectunified.unidialog.paper.body.PaperDialogBodyBuilder;
import io.github.projectunified.unidialog.paper.input.PaperDialogInputBuilder;
import io.papermc.paper.registry.data.dialog.ActionButton;
import io.papermc.paper.registry.data.dialog.type.DialogType;
import net.kyori.adventure.text.Component;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;
import java.util.function.Function;

public class PaperServerLinksDialog extends PaperDialog<PaperServerLinksDialog> implements ServerLinksDialog<ItemStack, PaperDialogBodyBuilder, PaperDialogInputBuilder, PaperDialog<?>, PaperDialogActionBuilder, PaperServerLinksDialog> {
    private @Nullable ActionButton exitAction;
    private int columns;
    private int buttonWidth;

    public PaperServerLinksDialog(String defaultNamespace, Function<String, Component> componentDeserializer) {
        super(defaultNamespace, componentDeserializer);
    }

    @Override
    public PaperServerLinksDialog exitAction(@Nullable Consumer<PaperDialogActionBuilder> action) {
        this.exitAction = action == null ? null : getAction(action);
        return this;
    }

    @Override
    public PaperServerLinksDialog columns(int columns) {
        this.columns = columns;
        return this;
    }

    @Override
    public PaperServerLinksDialog buttonWidth(int buttonWidth) {
        this.buttonWidth = buttonWidth;
        return this;
    }

    @Override
    protected DialogType getDialogType() {
        return DialogType.serverLinks(
                exitAction,
                columns > 0 ? columns : DEFAULT_COLUMNS,
                buttonWidth > 0 ? buttonWidth : DEFAULT_BUTTON_WIDTH
        );
    }
}
