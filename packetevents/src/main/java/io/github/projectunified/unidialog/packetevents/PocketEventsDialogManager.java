package io.github.projectunified.unidialog.packetevents;

import com.github.retrooper.packetevents.protocol.item.ItemStack;
import io.github.projectunified.unidialog.core.DialogManager;
import io.github.projectunified.unidialog.packetevents.action.PEDialogActionBuilder;
import io.github.projectunified.unidialog.packetevents.body.PEDialogBodyBuilder;
import io.github.projectunified.unidialog.packetevents.dialog.PEConfirmationDialog;
import io.github.projectunified.unidialog.packetevents.dialog.PEMultiActionDialog;
import io.github.projectunified.unidialog.packetevents.dialog.PENoticeDialog;
import io.github.projectunified.unidialog.packetevents.dialog.PEServerLinksDialog;
import io.github.projectunified.unidialog.packetevents.input.PEDialogInputBuilder;

@SuppressWarnings("unchecked")
public class PocketEventsDialogManager implements DialogManager<ItemStack, PEDialogBodyBuilder, PEDialogInputBuilder, PEDialogActionBuilder> {
    private final String defaultNamespace;

    public PocketEventsDialogManager(String defaultNamespace) {
        this.defaultNamespace = defaultNamespace;
    }

    @Override
    public PEConfirmationDialog createConfirmationDialog() {
        return new PEConfirmationDialog(defaultNamespace);
    }

    @Override
    public PEMultiActionDialog createMultiActionDialog() {
        return new PEMultiActionDialog(defaultNamespace);
    }

    @Override
    public PEServerLinksDialog createServerLinksDialog() {
        return new PEServerLinksDialog(defaultNamespace);
    }

    @Override
    public PENoticeDialog createNoticeDialog() {
        return new PENoticeDialog(defaultNamespace);
    }
}
