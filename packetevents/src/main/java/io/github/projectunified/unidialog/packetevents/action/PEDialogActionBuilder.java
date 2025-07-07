package io.github.projectunified.unidialog.packetevents.action;

import com.github.retrooper.packetevents.protocol.chat.clickevent.*;
import com.github.retrooper.packetevents.protocol.dialog.Dialog;
import com.github.retrooper.packetevents.protocol.dialog.Dialogs;
import com.github.retrooper.packetevents.protocol.dialog.action.*;
import com.github.retrooper.packetevents.protocol.dialog.button.ActionButton;
import com.github.retrooper.packetevents.protocol.dialog.button.CommonButtonData;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import io.github.projectunified.unidialog.adventure.action.AdventureDialogActionBuilder;
import io.github.projectunified.unidialog.core.opener.DialogOpener;
import io.github.projectunified.unidialog.packetevents.dialog.PEDialog;
import io.github.projectunified.unidialog.packetevents.opener.PEDialogOpener;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public class PEDialogActionBuilder implements AdventureDialogActionBuilder<PEDialog<?>, PEDialogActionBuilder> {
    private final String defaultNamespace;
    private final Function<String, Component> componentDeserializer;
    private Component label;
    private @Nullable Component tooltip;
    private int width;
    private @Nullable Action action;

    public PEDialogActionBuilder(String defaultNamespace, Function<String, Component> componentDeserializer) {
        this.defaultNamespace = defaultNamespace;
        this.componentDeserializer = componentDeserializer;
    }

    @Override
    public Function<String, Component> getComponentDeserializer() {
        return componentDeserializer;
    }

    @Override
    public PEDialogActionBuilder label(Component label) {
        this.label = label;
        return this;
    }

    @Override
    public PEDialogActionBuilder tooltip(@Nullable Component tooltip) {
        this.tooltip = tooltip;
        return this;
    }

    @Override
    public PEDialogActionBuilder width(int width) {
        this.width = width;
        return this;
    }

    private PEDialogActionBuilder action(Action action) {
        this.action = action;
        return this;
    }

    @Override
    public PEDialogActionBuilder copyToClipboard(String value) {
        return action(new StaticAction(new CopyToClipboardClickEvent(value)));
    }

    @Override
    public PEDialogActionBuilder dynamicCustom(String id) {
        return dynamicCustom(defaultNamespace, id);
    }

    @Override
    public PEDialogActionBuilder dynamicCustom(String namespace, String id) {
        return action(new DynamicCustomAction(new ResourceLocation(namespace, id), null));
    }

    @Override
    public PEDialogActionBuilder dynamicRunCommand(String template) {
        return action(new DynamicRunCommandAction(new DialogTemplate(template)));
    }

    @Override
    public PEDialogActionBuilder openUrl(String url) {
        return action(new StaticAction(new OpenUrlClickEvent(url)));
    }

    @Override
    public PEDialogActionBuilder runCommand(String command) {
        return action(new StaticAction(new RunCommandClickEvent(command)));
    }

    @Override
    public PEDialogActionBuilder suggestCommand(String command) {
        return action(new StaticAction(new SuggestCommandClickEvent(command)));
    }

    @Override
    public PEDialogActionBuilder showDialog(PEDialog<?> dialog) {
        return action(new StaticAction(new ShowDialogClickEvent(dialog.getDialog())));
    }

    @Override
    public PEDialogActionBuilder showDialog(String namespace, String dialogId) {
        Dialog dialog = Dialogs.getRegistry().getByName(new ResourceLocation(namespace, dialogId));
        if (dialog == null) {
            throw new IllegalArgumentException("Dialog not found: " + namespace + ":" + dialogId);
        }
        return action(new StaticAction(new ShowDialogClickEvent(dialog)));
    }

    @Override
    public PEDialogActionBuilder showDialog(DialogOpener dialogOpener) {
        if (!(dialogOpener instanceof PEDialogOpener peDialogOpener)) {
            throw new IllegalArgumentException("DialogOpener must be an instance of PEDialogOpener.");
        }
        return action(new StaticAction(new ShowDialogClickEvent(peDialogOpener.dialog())));
    }

    public ActionButton getAction() {
        return new ActionButton(
                new CommonButtonData(
                        label == null ? Component.text("Action") : label,
                        tooltip,
                        width > 0 ? width : 150
                ),
                action
        );
    }
}
