package io.github.projectunified.unidialog.viaversion.dialog;

import com.viaversion.nbt.tag.CompoundTag;
import com.viaversion.nbt.tag.ListTag;
import com.viaversion.nbt.tag.StringTag;
import com.viaversion.viaversion.api.minecraft.item.Item;
import com.viaversion.viaversion.libs.mcstructs.text.TextComponent;
import com.viaversion.viaversion.util.SerializerVersion;
import org.jetbrains.annotations.Nullable;
import io.github.projectunified.unidialog.core.dialog.DialogListDialog;
import io.github.projectunified.unidialog.core.opener.DialogOpener;
import io.github.projectunified.unidialog.viaversion.action.ViaDialogActionBuilder;
import io.github.projectunified.unidialog.viaversion.body.ViaDialogBodyBuilder;
import io.github.projectunified.unidialog.viaversion.input.ViaDialogInputBuilder;
import io.github.projectunified.unidialog.viaversion.opener.ViaDialogOpener;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * A ViaVersion-based implementation of {@link DialogListDialog} for building dialog list dialogs.
 */
public class ViaDialogListDialog extends ViaDialog<ViaDialogListDialog> implements DialogListDialog<Item, ViaDialogBodyBuilder, ViaDialogInputBuilder, ViaDialog<?>, ViaDialogActionBuilder, ViaDialogListDialog> {
    private List<ViaDialog<?>> inlinedDialogs;
    private List<StringTag> registryDialogs;
    private @Nullable ViaDialogActionBuilder exitAction;
    private int columns;
    private int buttonWidth;

    /**
     * Constructor for ViaDialogListDialog
     *
     * @param defaultNamespace      the default namespace for custom actions
     * @param componentDeserializer the function to deserialize components from strings
     * @param baseSerializer        the serializer of the server version the components are created for
     */
    public ViaDialogListDialog(String defaultNamespace, Function<String, TextComponent> componentDeserializer, SerializerVersion baseSerializer) {
        super(defaultNamespace, componentDeserializer, baseSerializer);
    }

    private void addInlined(ViaDialog<?> dialog) {
        if (inlinedDialogs == null) {
            inlinedDialogs = new ArrayList<>();
        }
        inlinedDialogs.add(dialog);
    }

    private void addRegistry(String namespace, String dialogId) {
        if (registryDialogs == null) {
            registryDialogs = new ArrayList<>();
        }
        registryDialogs.add(new StringTag(namespace + ":" + dialogId));
    }

    @Override
    public ViaDialogListDialog dialog(ViaDialog<?> dialog) {
        addInlined(dialog);
        return this;
    }

    @Override
    public ViaDialogListDialog dialog(String namespace, String dialogId) {
        addRegistry(namespace, dialogId);
        return this;
    }

    @Override
    public ViaDialogListDialog dialog(DialogOpener dialogOpener) {
        if (!(dialogOpener instanceof ViaDialogOpener viaDialogOpener)) {
            throw new IllegalArgumentException("Dialog opener must be an instance of ViaDialogOpener.");
        }
        addInlined(viaDialogOpener.dialog());
        return this;
    }

    @Override
    public ViaDialogListDialog exitAction(Consumer<ViaDialogActionBuilder> action) {
        this.exitAction = action == null ? null : getActionBuilder(action);
        return this;
    }

    @Override
    public ViaDialogListDialog columns(int columns) {
        this.columns = columns;
        return this;
    }

    @Override
    public ViaDialogListDialog buttonWidth(int buttonWidth) {
        this.buttonWidth = buttonWidth;
        return this;
    }

    @Override
    protected void writeDialogType(CompoundTag tag, SerializerVersion target) {
        tag.putString("type", "minecraft:dialog_list");
        if (inlinedDialogs != null && !inlinedDialogs.isEmpty() && registryDialogs != null && !registryDialogs.isEmpty()) {
            throw new IllegalArgumentException("Inlined and registry-referenced dialogs cannot be mixed in a dialog list");
        }
        if (inlinedDialogs != null && !inlinedDialogs.isEmpty()) {
            ListTag<CompoundTag> dialogsTag = new ListTag<>(CompoundTag.class);
            for (ViaDialog<?> dialog : inlinedDialogs) {
                dialogsTag.add(dialog.getDialogTag(target));
            }
            tag.put("dialogs", dialogsTag);
        } else if (registryDialogs != null && !registryDialogs.isEmpty()) {
            ListTag<StringTag> dialogsTag = new ListTag<>(StringTag.class);
            for (StringTag dialog : registryDialogs) {
                dialogsTag.add(dialog);
            }
            tag.put("dialogs", dialogsTag);
        }
        if (exitAction != null) {
            tag.put("exit_action", exitAction.getAction(getBaseSerializer(), target));
        }
        tag.putInt("columns", columns > 0 ? columns : DEFAULT_COLUMNS);
        tag.putInt("button_width", buttonWidth > 0 ? buttonWidth : DEFAULT_BUTTON_WIDTH);
    }
}
