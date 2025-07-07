package io.github.projectunified.unidialog.paper.dialog;

import io.github.projectunified.unidialog.core.dialog.ConfirmationDialog;
import io.github.projectunified.unidialog.paper.action.PaperDialogActionBuilder;
import io.github.projectunified.unidialog.paper.body.PaperDialogBodyBuilder;
import io.github.projectunified.unidialog.paper.input.PaperDialogInputBuilder;
import io.papermc.paper.registry.data.dialog.ActionButton;
import io.papermc.paper.registry.data.dialog.type.DialogType;
import net.kyori.adventure.text.Component;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;
import java.util.function.Function;

public class PaperConfirmationDialog extends PaperDialog<PaperConfirmationDialog> implements ConfirmationDialog<ItemStack, PaperDialogBodyBuilder, PaperDialogInputBuilder, PaperDialog<?>, PaperDialogActionBuilder, PaperConfirmationDialog> {
    private static final ActionButton DEFAULT_YES_ACTION = ActionButton.create(Component.text("Yes"), null, 150, null);
    private static final ActionButton DEFAULT_NO_ACTION = ActionButton.create(Component.text("No"), null, 150, null);

    private ActionButton yesAction;
    private ActionButton noAction;

    public PaperConfirmationDialog(String defaultNamespace, Function<String, Component> componentDeserializer) {
        super(defaultNamespace, componentDeserializer);
    }

    @Override
    public PaperConfirmationDialog yesAction(Consumer<PaperDialogActionBuilder> action) {
        this.yesAction = getAction(action);
        return this;
    }

    @Override
    public PaperConfirmationDialog noAction(Consumer<PaperDialogActionBuilder> action) {
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
    protected DialogType getDialogType() {
        return DialogType.confirmation(
                yesAction != null ? yesAction : DEFAULT_YES_ACTION,
                noAction != null ? noAction : DEFAULT_NO_ACTION
        );
    }
}
