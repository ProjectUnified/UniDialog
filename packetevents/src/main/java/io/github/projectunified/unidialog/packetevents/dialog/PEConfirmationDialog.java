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
    private static final ActionButton DEFAULT_YES_BUTTON = new ActionButton(
            new CommonButtonData(
                    Component.text("Yes"),
                    null,
                    150
            ),
            null
    );
    private static final ActionButton DEFAULT_NO_BUTTON = new ActionButton(
            new CommonButtonData(
                    Component.text("No"),
                    null,
                    151
            ),
            null
    );

    private ActionButton yesButton;
    private ActionButton noButton;

    public PEConfirmationDialog(String defaultNamespace, Function<UUID, @Nullable Object> playerFunction) {
        super(defaultNamespace, playerFunction);
    }

    @Override
    public PEConfirmationDialog yesAction(Consumer<PEDialogActionBuilder> action) {
        this.yesButton = getAction(action);
        return this;
    }

    @Override
    public PEConfirmationDialog noAction(Consumer<PEDialogActionBuilder> action) {
        this.noButton = getAction(action);
        return this;
    }

    @Override
    protected Dialog constructDialog(CommonDialogData commonDialogData) {
        return new com.github.retrooper.packetevents.protocol.dialog.ConfirmationDialog(
                commonDialogData,
                yesButton != null ? yesButton : DEFAULT_YES_BUTTON,
                noButton != null ? noButton : DEFAULT_NO_BUTTON
        );
    }
}
