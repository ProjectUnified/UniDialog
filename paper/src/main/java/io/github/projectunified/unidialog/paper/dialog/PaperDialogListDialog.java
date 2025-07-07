package io.github.projectunified.unidialog.paper.dialog;

import io.github.projectunified.unidialog.core.dialog.DialogListDialog;
import io.github.projectunified.unidialog.core.opener.DialogOpener;
import io.github.projectunified.unidialog.paper.action.PaperDialogActionBuilder;
import io.github.projectunified.unidialog.paper.body.PaperDialogBodyBuilder;
import io.github.projectunified.unidialog.paper.input.PaperDialogInputBuilder;
import io.github.projectunified.unidialog.paper.opener.PaperDialogOpener;
import io.papermc.paper.dialog.Dialog;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.data.dialog.ActionButton;
import io.papermc.paper.registry.data.dialog.type.DialogType;
import io.papermc.paper.registry.set.RegistrySet;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class PaperDialogListDialog extends PaperDialog<PaperDialogListDialog> implements DialogListDialog<ItemStack, PaperDialogBodyBuilder, PaperDialogInputBuilder, PaperDialog<?>, PaperDialogActionBuilder, PaperDialogListDialog> {
    private List<Dialog> dialogs;
    private @Nullable ActionButton exitAction;
    private int columns;
    private int buttonWidth;

    public PaperDialogListDialog(String defaultNamespace, Function<String, Component> componentDeserializer) {
        super(defaultNamespace, componentDeserializer);
    }

    private void addDialog(Dialog dialog) {
        if (dialogs == null) {
            dialogs = new ArrayList<>();
        }
        dialogs.add(dialog);
    }

    @Override
    public PaperDialogListDialog dialog(PaperDialog<?> dialog) {
        addDialog(dialog.getDialog());
        return this;
    }

    @Override
    public PaperDialogListDialog dialog(String namespace, String dialogId) {
        Dialog dialog = RegistryAccess.registryAccess().getRegistry(RegistryKey.DIALOG).getOrThrow(Key.key(namespace, dialogId));
        addDialog(dialog);
        return this;
    }

    @Override
    public PaperDialogListDialog dialog(DialogOpener dialogOpener) {
        if (!(dialogOpener instanceof PaperDialogOpener(Dialog dialog))) {
            throw new IllegalArgumentException("Dialog opener must be an instance of PaperDialogOpener");
        }
        addDialog(dialog);
        return this;
    }

    @Override
    public PaperDialogListDialog exitAction(@Nullable Consumer<PaperDialogActionBuilder> action) {
        this.exitAction = action == null ? null : getAction(action);
        return this;
    }

    @Override
    public PaperDialogListDialog columns(int columns) {
        this.columns = columns;
        return this;
    }

    @Override
    public PaperDialogListDialog buttonWidth(int buttonWidth) {
        this.buttonWidth = buttonWidth;
        return this;
    }

    @Override
    protected DialogType getDialogType() {
        return DialogType.dialogList(
                RegistrySet.valueSet(RegistryKey.DIALOG, dialogs != null ? dialogs : Collections.emptyList()),
                exitAction,
                columns > 0 ? columns : DEFAULT_COLUMNS,
                buttonWidth > 0 ? buttonWidth : DEFAULT_BUTTON_WIDTH
        );
    }
}
