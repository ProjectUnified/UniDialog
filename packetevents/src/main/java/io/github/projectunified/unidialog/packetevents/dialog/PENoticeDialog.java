package io.github.projectunified.unidialog.packetevents.dialog;

import com.github.retrooper.packetevents.protocol.dialog.CommonDialogData;
import com.github.retrooper.packetevents.protocol.dialog.Dialog;
import com.github.retrooper.packetevents.protocol.dialog.button.ActionButton;
import com.github.retrooper.packetevents.protocol.item.ItemStack;
import io.github.projectunified.unidialog.core.dialog.NoticeDialog;
import io.github.projectunified.unidialog.packetevents.action.PEDialogActionBuilder;
import io.github.projectunified.unidialog.packetevents.body.PEDialogBodyBuilder;
import io.github.projectunified.unidialog.packetevents.input.PEDialogInputBuilder;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

public class PENoticeDialog extends PEDialog<PENoticeDialog> implements NoticeDialog<ItemStack, PEDialogBodyBuilder, PEDialogInputBuilder, PEDialogActionBuilder, PENoticeDialog> {
    private ActionButton action;

    public PENoticeDialog(String defaultNamespace, Function<String, Component> componentDeserializer, Function<UUID, @Nullable Object> playerFunction) {
        super(defaultNamespace, componentDeserializer, playerFunction);
    }

    @Override
    public PENoticeDialog action(Consumer<PEDialogActionBuilder> action) {
        this.action = getAction(action);
        return this;
    }

    @Override
    protected Dialog constructDialog(CommonDialogData commonDialogData) {
        return new com.github.retrooper.packetevents.protocol.dialog.NoticeDialog(
                commonDialogData,
                action == null ? com.github.retrooper.packetevents.protocol.dialog.NoticeDialog.DEFAULT_ACTION : action
        );
    }
}
