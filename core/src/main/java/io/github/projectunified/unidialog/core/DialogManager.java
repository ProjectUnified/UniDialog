package io.github.projectunified.unidialog.core;

import io.github.projectunified.unidialog.core.dialog.ConfirmationDialog;
import io.github.projectunified.unidialog.core.dialog.MultiActionDialog;
import io.github.projectunified.unidialog.core.dialog.NoticeDialog;
import io.github.projectunified.unidialog.core.dialog.ServerLinksDialog;

import java.util.Map;
import java.util.UUID;
import java.util.function.BiConsumer;

public interface DialogManager<I> {
    <T extends ConfirmationDialog<I, T>> T createConfirmationDialog();

    <T extends MultiActionDialog<I, T>> T createMultiActionDialog();

    <T extends ServerLinksDialog<I, T>> T createServerLinksDialog();

    <T extends NoticeDialog<I, T>> T createNoticeDialog();

    void registerAction(String id, BiConsumer<UUID, Map<String, String>> action);

    void clearDialog(UUID uuid);
}
