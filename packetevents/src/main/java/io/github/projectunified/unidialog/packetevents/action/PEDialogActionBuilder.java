package io.github.projectunified.unidialog.packetevents.action;

import com.github.retrooper.packetevents.protocol.chat.clickevent.CopyToClipboardClickEvent;
import com.github.retrooper.packetevents.protocol.chat.clickevent.OpenUrlClickEvent;
import com.github.retrooper.packetevents.protocol.chat.clickevent.RunCommandClickEvent;
import com.github.retrooper.packetevents.protocol.chat.clickevent.SuggestCommandClickEvent;
import com.github.retrooper.packetevents.protocol.dialog.action.*;
import com.github.retrooper.packetevents.protocol.dialog.button.ActionButton;
import com.github.retrooper.packetevents.protocol.dialog.button.CommonButtonData;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import io.github.projectunified.unidialog.core.action.DialogActionBuilder;
import io.github.retrooper.packetevents.adventure.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;

public class PEDialogActionBuilder implements DialogActionBuilder<PEDialogActionBuilder> {
    private final String defaultNamespace;
    private Component label;
    private @Nullable Component tooltip;
    private int width;
    private @Nullable Action action;

    public PEDialogActionBuilder(String defaultNamespace) {
        this.defaultNamespace = defaultNamespace;
    }

    public PEDialogActionBuilder label(Component label) {
        this.label = label;
        return this;
    }

    @Override
    public PEDialogActionBuilder label(String label) {
        return label(LegacyComponentSerializer.legacySection().deserialize(label));
    }

    public PEDialogActionBuilder tooltip(@Nullable Component tooltip) {
        this.tooltip = tooltip;
        return this;
    }

    @Override
    public PEDialogActionBuilder tooltip(@Nullable String tooltip) {
        if (tooltip == null) {
            this.tooltip = null;
            return this;
        }
        return tooltip(LegacyComponentSerializer.legacySection().deserialize(tooltip));
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
