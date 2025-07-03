package io.github.projectunified.unidialog.core;

import io.github.projectunified.unidialog.core.action.DialogActionBuilder;
import io.github.projectunified.unidialog.core.body.DialogBodyBuilder;
import io.github.projectunified.unidialog.core.dialog.ConfirmationDialog;
import io.github.projectunified.unidialog.core.dialog.MultiActionDialog;
import io.github.projectunified.unidialog.core.dialog.NoticeDialog;
import io.github.projectunified.unidialog.core.dialog.ServerLinksDialog;
import io.github.projectunified.unidialog.core.input.DialogInputBuilder;

import java.util.Map;
import java.util.UUID;
import java.util.function.BiConsumer;

public interface DialogManager<I, BB extends DialogBodyBuilder<I>, IB extends DialogInputBuilder, AB extends DialogActionBuilder<AB>> {
    <T extends ConfirmationDialog<I, BB, IB, AB, T>> T createConfirmationDialog();

    <T extends MultiActionDialog<I, BB, IB, AB, T>> T createMultiActionDialog();

    <T extends ServerLinksDialog<I, BB, IB, AB, T>> T createServerLinksDialog();

    <T extends NoticeDialog<I, BB, IB, AB, T>> T createNoticeDialog();

    void register();

    void unregister();

    void registerCustomAction(String id, BiConsumer<UUID, Map<String, String>> action);

    void registerCustomAction(String namespace, String id, BiConsumer<UUID, Map<String, String>> action);

    void unregisterCustomAction(String id);

    void unregisterCustomAction(String namespace, String id);

    void unregisterAllCustomActions();

    boolean clearDialog(UUID uuid);
}
