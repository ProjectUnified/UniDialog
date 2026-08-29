package io.github.projectunified.unidialog.viaversion.action;

import com.viaversion.nbt.tag.CompoundTag;
import com.viaversion.viaversion.libs.mcstructs.text.TextComponent;
import com.viaversion.viaversion.util.SerializerVersion;
import io.github.projectunified.unidialog.core.action.DialogActionBuilder;
import io.github.projectunified.unidialog.core.opener.DialogOpener;
import io.github.projectunified.unidialog.viaversion.ViaDialogTagBuilder;
import io.github.projectunified.unidialog.viaversion.dialog.ViaDialog;
import io.github.projectunified.unidialog.viaversion.opener.ViaDialogOpener;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

/**
 * A ViaVersion-based implementation of {@link DialogActionBuilder} for building dialog actions.
 */
public class ViaDialogActionBuilder implements DialogActionBuilder<ViaDialog<?>, ViaDialogActionBuilder> {
    private final String defaultNamespace;
    private final Function<String, TextComponent> componentDeserializer;
    private TextComponent label;
    private @Nullable TextComponent tooltip;
    private int width;
    private @Nullable CompoundTag action;
    private @Nullable ViaDialog<?> showDialog;

    /**
     * Constructor for ViaDialogActionBuilder
     *
     * @param defaultNamespace      the default namespace for custom actions
     * @param componentDeserializer the function to deserialize components from strings
     */
    public ViaDialogActionBuilder(String defaultNamespace, Function<String, TextComponent> componentDeserializer) {
        this.defaultNamespace = defaultNamespace;
        this.componentDeserializer = componentDeserializer;
    }

    /**
     * Set the label for the action
     *
     * @param label the label
     * @return the builder itself for method chaining
     */
    public ViaDialogActionBuilder label(TextComponent label) {
        this.label = label;
        return this;
    }

    @Override
    public ViaDialogActionBuilder label(String label) {
        return label(componentDeserializer.apply(label));
    }

    /**
     * Set the tooltip for the action
     *
     * @param tooltip the tooltip
     * @return the builder itself for method chaining
     */
    public ViaDialogActionBuilder tooltip(@Nullable TextComponent tooltip) {
        this.tooltip = tooltip;
        return this;
    }

    @Override
    public ViaDialogActionBuilder tooltip(@Nullable String tooltip) {
        return tooltip(tooltip == null ? null : componentDeserializer.apply(tooltip));
    }

    @Override
    public ViaDialogActionBuilder width(int width) {
        this.width = width;
        return this;
    }

    private ViaDialogActionBuilder action(CompoundTag action) {
        this.action = action;
        return this;
    }

    @Override
    public ViaDialogActionBuilder copyToClipboard(String value) {
        CompoundTag action = new CompoundTag();
        action.putString("type", "minecraft:copy_to_clipboard");
        action.putString("value", value);
        return action(action);
    }

    @Override
    public ViaDialogActionBuilder dynamicCustom(String id) {
        return dynamicCustom(defaultNamespace, id);
    }

    @Override
    public ViaDialogActionBuilder dynamicCustom(String namespace, String id) {
        CompoundTag action = new CompoundTag();
        action.putString("type", "minecraft:dynamic/custom");
        action.putString("id", namespace + ":" + id);
        action.put("additions", new CompoundTag());
        return action(action);
    }

    @Override
    public ViaDialogActionBuilder dynamicRunCommand(String template) {
        CompoundTag action = new CompoundTag();
        action.putString("type", "minecraft:dynamic/run_command");
        action.putString("template", template);
        return action(action);
    }

    @Override
    public ViaDialogActionBuilder openUrl(String url) {
        CompoundTag action = new CompoundTag();
        action.putString("type", "minecraft:open_url");
        action.putString("url", url);
        return action(action);
    }

    @Override
    public ViaDialogActionBuilder runCommand(String command) {
        CompoundTag action = new CompoundTag();
        action.putString("type", "minecraft:run_command");
        action.putString("command", command);
        return action(action);
    }

    @Override
    public ViaDialogActionBuilder suggestCommand(String command) {
        CompoundTag action = new CompoundTag();
        action.putString("type", "minecraft:suggest_command");
        action.putString("command", command);
        return action(action);
    }

    @Override
    public ViaDialogActionBuilder showDialog(ViaDialog<?> dialog) {
        this.action = null;
        this.showDialog = dialog;
        return this;
    }

    @Override
    public ViaDialogActionBuilder showDialog(String namespace, String dialogId) {
        CompoundTag action = new CompoundTag();
        action.putString("type", "minecraft:show_dialog");
        action.putString("dialog", namespace + ":" + dialogId);
        return action(action);
    }

    @Override
    public ViaDialogActionBuilder showDialog(DialogOpener dialogOpener) {
        if (!(dialogOpener instanceof ViaDialogOpener viaDialogOpener)) {
            throw new IllegalArgumentException("DialogOpener must be an instance of ViaDialogOpener.");
        }
        return showDialog(viaDialogOpener.dialog());
    }

    /**
     * Build the button tag for the given target serializer
     *
     * @param base   the serializer of the version the components are created for
     * @param target the serializer of the version the dialog is sent to
     * @return the button tag
     */
    public CompoundTag getAction(SerializerVersion base, SerializerVersion target) {
        CompoundTag tag = new CompoundTag();
        tag.put("label", ViaDialogTagBuilder.component(label != null ? label : new com.viaversion.viaversion.libs.mcstructs.text.components.StringComponent("Action"), base, target));
        ViaDialogTagBuilder.putComponent(tag, "tooltip", tooltip, base, target);
        tag.putInt("width", width > 0 ? width : DEFAULT_WIDTH);
        CompoundTag actionTag = null;
        if (action != null) {
            actionTag = action;
        } else if (showDialog != null) {
            actionTag = new CompoundTag();
            actionTag.putString("type", "minecraft:show_dialog");
            actionTag.put("dialog", showDialog.getDialogTag(target));
        }
        if (actionTag != null) {
            tag.put("action", actionTag);
        }
        return tag;
    }
}
