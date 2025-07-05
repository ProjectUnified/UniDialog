package io.github.projectunified.unidialog.packetevents.dialog;

import com.github.retrooper.packetevents.protocol.dialog.CommonDialogData;
import com.github.retrooper.packetevents.protocol.dialog.Dialog;
import com.github.retrooper.packetevents.protocol.dialog.button.ActionButton;
import com.github.retrooper.packetevents.protocol.dialog.button.CommonButtonData;
import com.github.retrooper.packetevents.protocol.item.ItemStack;
import io.github.projectunified.unidialog.core.dialog.ConfirmationDialog;
import io.github.projectunified.unidialog.packetevents.action.PEDialogActionBuilder;
import io.github.projectunified.unidialog.packetevents.body.PEDialogBodyBuilder;
import io.github.projectunified.unidialog.packetevents.input.PEDialogInputBuilder;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

public class PEConfirmationDialog extends PEDialog<PEConfirmationDialog> implements ConfirmationDialog<ItemStack, PEDialogBodyBuilder, PEDialogInputBuilder, PEDialogActionBuilder, PEConfirmationDialog> {
    private static final ActionButton DEFAULT_YES_ACTION = new ActionButton(
            new CommonButtonData(
                    Component.text("Yes"),
                    null,
                    150
            ),
            null
    );
    private static final ActionButton DEFAULT_NO_ACTION = new ActionButton(
            new CommonButtonData(
                    Component.text("No"),
                    null,
                    151
            ),
            null
    );

    private ActionButton yesAction;
    private ActionButton noAction;

    public PEConfirmationDialog(String defaultNamespace, Function<String, Component> componentDeserializer, Function<UUID, @Nullable Object> playerFunction) {
        super(defaultNamespace, componentDeserializer, playerFunction);
    }

    @Override
    public PEConfirmationDialog yesAction(Consumer<PEDialogActionBuilder> action) {
        this.yesAction = getAction(action);
        return this;
    }

    @Override
    public PEConfirmationDialog noAction(Consumer<PEDialogActionBuilder> action) {
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
    protected Dialog constructDialog(CommonDialogData commonDialogData) {
        return new com.github.retrooper.packetevents.protocol.dialog.ConfirmationDialog(
                commonDialogData,
                yesAction != null ? yesAction : DEFAULT_YES_ACTION,
                noAction != null ? noAction : DEFAULT_NO_ACTION
        );
    }
}
