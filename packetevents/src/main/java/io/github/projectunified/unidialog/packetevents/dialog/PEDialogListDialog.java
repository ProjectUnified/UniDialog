package io.github.projectunified.unidialog.packetevents.dialog;

import com.github.retrooper.packetevents.protocol.dialog.CommonDialogData;
import com.github.retrooper.packetevents.protocol.dialog.Dialog;
import com.github.retrooper.packetevents.protocol.dialog.Dialogs;
import com.github.retrooper.packetevents.protocol.dialog.button.ActionButton;
import com.github.retrooper.packetevents.protocol.item.ItemStack;
import com.github.retrooper.packetevents.protocol.mapper.MappedEntitySet;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import io.github.projectunified.unidialog.core.dialog.DialogListDialog;
import io.github.projectunified.unidialog.core.opener.DialogOpener;
import io.github.projectunified.unidialog.packetevents.action.PEDialogActionBuilder;
import io.github.projectunified.unidialog.packetevents.body.PEDialogBodyBuilder;
import io.github.projectunified.unidialog.packetevents.input.PEDialogInputBuilder;
import io.github.projectunified.unidialog.packetevents.opener.PEDialogOpener;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

public class PEDialogListDialog extends PEDialog<PEDialogListDialog> implements DialogListDialog<ItemStack, PEDialogBodyBuilder, PEDialogInputBuilder, PEDialog<?>, PEDialogActionBuilder, PEDialogListDialog> {
    private List<Dialog> dialogs;
    private @Nullable ActionButton exitAction;
    private int columns;
    private int buttonWidth;

    public PEDialogListDialog(String defaultNamespace, Function<String, Component> componentDeserializer, Function<UUID, @Nullable Object> playerFunction) {
        super(defaultNamespace, componentDeserializer, playerFunction);
    }

    private void addDialog(Dialog dialog) {
        if (dialogs == null) {
            dialogs = new ArrayList<>();
        }
        dialogs.add(dialog);
    }

    @Override
    public PEDialogListDialog dialog(PEDialog<?> dialog) {
        addDialog(dialog.getDialog());
        return this;
    }

    @Override
    public PEDialogListDialog dialog(String namespace, String dialogId) {
        Dialog dialog = Dialogs.getRegistry().getByName(new ResourceLocation(namespace, dialogId));
        if (dialog == null) {
            throw new IllegalArgumentException("Dialog with namespace '" + namespace + "' and id '" + dialogId + "' does not exist.");
        }
        addDialog(dialog);
        return this;
    }

    @Override
    public PEDialogListDialog dialog(DialogOpener dialogOpener) {
        if (!(dialogOpener instanceof PEDialogOpener peDialogOpener)) {
            throw new IllegalArgumentException("Dialog opener must be an instance of PEDialogOpener.");
        }
        addDialog(peDialogOpener.dialog());
        return this;
    }

    @Override
    public PEDialogListDialog exitAction(Consumer<PEDialogActionBuilder> action) {
        this.exitAction = action == null ? null : getAction(action);
        return this;
    }

    @Override
    public PEDialogListDialog columns(int columns) {
        this.columns = columns;
        return this;
    }

    @Override
    public PEDialogListDialog buttonWidth(int buttonWidth) {
        this.buttonWidth = buttonWidth;
        return this;
    }

    @Override
    protected Dialog constructDialog(CommonDialogData commonDialogData) {
        return new com.github.retrooper.packetevents.protocol.dialog.DialogListDialog(
                commonDialogData,
                dialogs == null || dialogs.isEmpty() ? MappedEntitySet.createEmpty() : new MappedEntitySet<>(dialogs),
                exitAction,
                columns > 0 ? columns : 2,
                buttonWidth > 0 ? buttonWidth : 150
        );
    }
}
