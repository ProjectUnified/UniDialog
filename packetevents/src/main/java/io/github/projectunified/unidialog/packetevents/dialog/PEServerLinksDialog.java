package io.github.projectunified.unidialog.packetevents.dialog;

import com.github.retrooper.packetevents.protocol.dialog.CommonDialogData;
import com.github.retrooper.packetevents.protocol.dialog.Dialog;
import com.github.retrooper.packetevents.protocol.dialog.button.ActionButton;
import com.github.retrooper.packetevents.protocol.item.ItemStack;
import io.github.projectunified.unidialog.core.dialog.ServerLinksDialog;
import io.github.projectunified.unidialog.packetevents.action.PEDialogActionBuilder;
import io.github.projectunified.unidialog.packetevents.body.PEDialogBodyBuilder;
import io.github.projectunified.unidialog.packetevents.input.PEDialogInputBuilder;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

public class PEServerLinksDialog extends PEDialog<PEServerLinksDialog> implements ServerLinksDialog<ItemStack, PEDialogBodyBuilder, PEDialogInputBuilder, PEDialogActionBuilder, PEServerLinksDialog> {
    private ActionButton exitAction;
    private int columns;
    private int buttonWidth;

    public PEServerLinksDialog(String defaultNamespace, Function<String, Component> componentDeserializer, Function<UUID, @Nullable Object> playerFunction) {
        super(defaultNamespace, componentDeserializer, playerFunction);
    }

    @Override
    public PEServerLinksDialog exitAction(@Nullable Consumer<PEDialogActionBuilder> action) {
        exitAction = action == null ? null : getAction(action);
        return this;
    }

    @Override
    public PEServerLinksDialog columns(int columns) {
        this.columns = columns;
        return this;
    }

    @Override
    public PEServerLinksDialog buttonWidth(int buttonWidth) {
        this.buttonWidth = buttonWidth;
        return this;
    }

    @Override
    protected Dialog constructDialog(CommonDialogData commonDialogData) {
        return new com.github.retrooper.packetevents.protocol.dialog.ServerLinksDialog(
                commonDialogData,
                exitAction,
                columns > 0 ? columns : 2,
                buttonWidth > 0 ? buttonWidth : 150
        );
    }
}
