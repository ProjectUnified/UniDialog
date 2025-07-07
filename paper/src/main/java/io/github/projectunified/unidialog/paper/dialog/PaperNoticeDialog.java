package io.github.projectunified.unidialog.paper.dialog;

import io.github.projectunified.unidialog.core.dialog.NoticeDialog;
import io.github.projectunified.unidialog.paper.action.PaperDialogActionBuilder;
import io.github.projectunified.unidialog.paper.body.PaperDialogBodyBuilder;
import io.github.projectunified.unidialog.paper.input.PaperDialogInputBuilder;
import io.papermc.paper.registry.data.dialog.ActionButton;
import io.papermc.paper.registry.data.dialog.type.DialogType;
import net.kyori.adventure.text.Component;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;
import java.util.function.Function;

public class PaperNoticeDialog extends PaperDialog<PaperNoticeDialog> implements NoticeDialog<ItemStack, PaperDialogBodyBuilder, PaperDialogInputBuilder, PaperDialog<?>, PaperDialogActionBuilder, PaperNoticeDialog> {
    private ActionButton action;

    public PaperNoticeDialog(String defaultNamespace, Function<String, Component> componentDeserializer) {
        super(defaultNamespace, componentDeserializer);
    }

    @Override
    public PaperNoticeDialog action(Consumer<PaperDialogActionBuilder> action) {
        this.action = getAction(action);
        return this;
    }

    @Override
    protected DialogType getDialogType() {
        return action != null ? DialogType.notice(action) : DialogType.notice();
    }
}
