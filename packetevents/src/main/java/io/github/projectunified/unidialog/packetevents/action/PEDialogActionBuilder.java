package io.github.projectunified.unidialog.packetevents.action;

import com.github.retrooper.packetevents.protocol.dialog.button.ActionButton;
import com.github.retrooper.packetevents.protocol.dialog.button.CommonButtonData;
import io.github.projectunified.unidialog.core.action.DialogActionBuilder;
import io.github.retrooper.packetevents.adventure.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("unchecked")
public class PEDialogActionBuilder implements DialogActionBuilder<PEDialogActionBuilder> {
    private final String defaultNamespace;
    private Component label;
    private @Nullable Component tooltip;
    private int width;
    private @Nullable PEDialogAction action;

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

    @Override
    public PECopyToClipboardAction copyToClipboard() {
        PECopyToClipboardAction action = new PECopyToClipboardAction();
        this.action = action;
        return action;
    }

    @Override
    public PEDynamicCustomAction dynamicCustom() {
        PEDynamicCustomAction action = new PEDynamicCustomAction(defaultNamespace);
        this.action = action;
        return action;
    }

    @Override
    public PEDynamicRunCommandAction dynamicRunCommand() {
        PEDynamicRunCommandAction action = new PEDynamicRunCommandAction();
        this.action = action;
        return action;
    }

    @Override
    public PEOpenUrlAction openUrl() {
        PEOpenUrlAction action = new PEOpenUrlAction();
        this.action = action;
        return action;
    }

    @Override
    public PERunCommandAction runCommand() {
        PERunCommandAction action = new PERunCommandAction();
        this.action = action;
        return action;
    }

    @Override
    public PESuggestCommandAction suggestCommand() {
        PESuggestCommandAction action = new PESuggestCommandAction();
        this.action = action;
        return action;
    }

    public ActionButton getAction() {
        return new ActionButton(
                new CommonButtonData(
                        label == null ? Component.text("Action") : label,
                        tooltip,
                        width > 0 ? width : 150
                ),
                action == null ? null : action.getAction()
        );
    }
}
