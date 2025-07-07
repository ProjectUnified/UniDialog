package io.github.projectunified.unidialog.paper.action;

import io.github.projectunified.unidialog.adventure.action.AdventureDialogActionBuilder;
import io.github.projectunified.unidialog.core.opener.DialogOpener;
import io.github.projectunified.unidialog.paper.dialog.PaperDialog;
import io.github.projectunified.unidialog.paper.opener.PaperDialogOpener;
import io.papermc.paper.dialog.Dialog;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.data.dialog.ActionButton;
import io.papermc.paper.registry.data.dialog.action.DialogAction;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public class PaperDialogActionBuilder implements AdventureDialogActionBuilder<PaperDialog<?>, PaperDialogActionBuilder> {
    private final String defaultNamespace;
    private final Function<String, Component> componentDeserializer;

    private Component label;
    private @Nullable Component tooltip;
    private int width;
    private @Nullable DialogAction action;

    public PaperDialogActionBuilder(String defaultNamespace, Function<String, Component> componentDeserializer) {
        this.defaultNamespace = defaultNamespace;
        this.componentDeserializer = componentDeserializer;
    }

    @Override
    public PaperDialogActionBuilder label(Component label) {
        this.label = label;
        return this;
    }

    @Override
    public PaperDialogActionBuilder tooltip(@Nullable Component tooltip) {
        this.tooltip = tooltip;
        return this;
    }

    @Override
    public Function<String, Component> getComponentDeserializer() {
        return componentDeserializer;
    }

    @Override
    public PaperDialogActionBuilder width(int width) {
        this.width = width;
        return this;
    }

    private PaperDialogActionBuilder action(DialogAction action) {
        this.action = action;
        return this;
    }

    private PaperDialogActionBuilder action(ClickEvent clickEvent) {
        return action(DialogAction.staticAction(clickEvent));
    }

    @Override
    public PaperDialogActionBuilder copyToClipboard(String value) {
        return action(ClickEvent.copyToClipboard(value));
    }

    @Override
    public PaperDialogActionBuilder dynamicCustom(String id) {
        return dynamicCustom(defaultNamespace, id);
    }

    @Override
    public PaperDialogActionBuilder dynamicCustom(String namespace, String id) {
        return action(DialogAction.customClick(Key.key(namespace, id), null));
    }

    @Override
    public PaperDialogActionBuilder dynamicRunCommand(String template) {
        return action(DialogAction.commandTemplate(template));
    }

    @Override
    public PaperDialogActionBuilder openUrl(String url) {
        return action(ClickEvent.openUrl(url));
    }

    @Override
    public PaperDialogActionBuilder runCommand(String command) {
        return action(ClickEvent.runCommand(command));
    }

    @Override
    public PaperDialogActionBuilder suggestCommand(String command) {
        return action(ClickEvent.suggestCommand(command));
    }

    @Override
    public PaperDialogActionBuilder showDialog(PaperDialog<?> dialog) {
        return action(ClickEvent.showDialog(dialog.getDialog()));
    }

    @Override
    public PaperDialogActionBuilder showDialog(String namespace, String dialogId) {
        Dialog dialog = RegistryAccess.registryAccess().getRegistry(RegistryKey.DIALOG).getOrThrow(Key.key(namespace, dialogId));
        return action(ClickEvent.showDialog(dialog));
    }

    @Override
    public PaperDialogActionBuilder showDialog(DialogOpener dialogOpener) {
        if (!(dialogOpener instanceof PaperDialogOpener(Dialog dialog))) {
            throw new IllegalArgumentException("Dialog opener must be an instance of PaperDialogOpener.");
        }
        return action(ClickEvent.showDialog(dialog));
    }

    public ActionButton getAction() {
        return ActionButton.create(
                label != null ? label : Component.text("Action"),
                tooltip,
                width > 0 ? width : DEFAULT_WIDTH,
                action
        );
    }
}
