package io.github.projectunified.unidialog.bungeecord;

import io.github.projectunified.unidialog.bungeecord.action.BungeeDialogActionBuilder;
import io.github.projectunified.unidialog.bungeecord.body.BungeeDialogBodyBuilder;
import io.github.projectunified.unidialog.bungeecord.dialog.*;
import io.github.projectunified.unidialog.bungeecord.input.BungeeDialogInputBuilder;
import io.github.projectunified.unidialog.bungeecord.opener.BungeeDialogOpener;
import io.github.projectunified.unidialog.core.DialogManager;
import net.md_5.bungee.api.dialog.Dialog;

@SuppressWarnings("unchecked")
public interface BungeeDialogManager<O extends BungeeDialogOpener> extends DialogManager<Object, BungeeDialogBodyBuilder, BungeeDialogInputBuilder, BungeeDialog<?, ?>, BungeeDialogActionBuilder> {
    String getDefaultNamespace();

    O getDialogOpener(Dialog dialog);

    @Override
    default BungeeConfirmationDialog<O> createConfirmationDialog() {
        return new BungeeConfirmationDialog<>(getDefaultNamespace(), this::getDialogOpener);
    }

    @Override
    default BungeeMultiActionDialog<O> createMultiActionDialog() {
        return new BungeeMultiActionDialog<>(getDefaultNamespace(), this::getDialogOpener);
    }

    @Override
    default BungeeServerLinksDialog<O> createServerLinksDialog() {
        return new BungeeServerLinksDialog<>(getDefaultNamespace(), this::getDialogOpener);
    }

    @Override
    default BungeeNoticeDialog<O> createNoticeDialog() {
        return new BungeeNoticeDialog<>(getDefaultNamespace(), this::getDialogOpener);
    }

    @Override
    default BungeeDialogListDialog<O> createDialogListDialog() {
        return new BungeeDialogListDialog<>(getDefaultNamespace(), this::getDialogOpener);
    }
}
