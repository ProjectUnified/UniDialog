package io.github.projectunified.unidialog.packetevents.dialog;

import com.github.retrooper.packetevents.protocol.dialog.CommonDialogData;
import com.github.retrooper.packetevents.protocol.dialog.Dialog;
import com.github.retrooper.packetevents.protocol.dialog.button.ActionButton;
import com.github.retrooper.packetevents.protocol.item.ItemStack;
import io.github.projectunified.unidialog.core.dialog.MultiActionDialog;
import io.github.projectunified.unidialog.packetevents.action.PEDialogActionBuilder;
import io.github.projectunified.unidialog.packetevents.body.PEDialogBodyBuilder;
import io.github.projectunified.unidialog.packetevents.input.PEDialogInputBuilder;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

public class PEMultiActionDialog extends PEDialog<PEMultiActionDialog> implements MultiActionDialog<ItemStack, PEDialogBodyBuilder, PEDialogInputBuilder, PEDialogActionBuilder, PEMultiActionDialog> {
    private int columns;
    private List<ActionButton> actions;
    private @Nullable ActionButton exitAction;

    public PEMultiActionDialog(String defaultNamespace, Function<UUID, @Nullable Object> playerFunction) {
        super(defaultNamespace, playerFunction);
    }

    @Override
    public PEMultiActionDialog columns(int columns) {
        this.columns = columns;
        return this;
    }

    @Override
    public PEMultiActionDialog action(Consumer<PEDialogActionBuilder> action) {
        if (actions == null) {
            actions = new ArrayList<>();
        }
        ActionButton actionButton = getAction(action);
        actions.add(actionButton);
        return this;
    }

    @Override
    public PEMultiActionDialog exitAction(@Nullable Consumer<PEDialogActionBuilder> action) {
        this.exitAction = action == null ? null : getAction(action);
        return this;
    }

    @Override
    protected Dialog constructDialog(CommonDialogData commonDialogData) {
        return new com.github.retrooper.packetevents.protocol.dialog.MultiActionDialog(
                commonDialogData,
                actions != null ? actions : Collections.emptyList(),
                exitAction,
                columns > 0 ? columns : 2
        );
    }
}
