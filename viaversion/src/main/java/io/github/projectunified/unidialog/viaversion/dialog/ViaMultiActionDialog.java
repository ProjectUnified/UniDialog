package io.github.projectunified.unidialog.viaversion.dialog;

import com.viaversion.nbt.tag.CompoundTag;
import com.viaversion.nbt.tag.ListTag;
import com.viaversion.viaversion.libs.mcstructs.text.TextComponent;
import com.viaversion.viaversion.util.SerializerVersion;
import io.github.projectunified.unidialog.core.dialog.MultiActionDialog;
import io.github.projectunified.unidialog.viaversion.action.ViaDialogActionBuilder;
import io.github.projectunified.unidialog.viaversion.body.ViaDialogBodyBuilder;
import io.github.projectunified.unidialog.viaversion.input.ViaDialogInputBuilder;
import io.github.projectunified.unidialog.viaversion.payload.ViaItem;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * A ViaVersion-based implementation of {@link MultiActionDialog} for building multi-action dialogs.
 */
public class ViaMultiActionDialog extends ViaDialog<ViaMultiActionDialog> implements MultiActionDialog<ViaItem, ViaDialogBodyBuilder, ViaDialogInputBuilder, ViaDialog<?>, ViaDialogActionBuilder, ViaMultiActionDialog> {
    private int columns;
    private List<ViaDialogActionBuilder> actions;
    private @Nullable ViaDialogActionBuilder exitAction;

    /**
     * Constructor for ViaMultiActionDialog
     *
     * @param defaultNamespace      the default namespace for custom actions
     * @param componentDeserializer the function to deserialize components from strings
     * @param baseSerializer        the serializer of the server version the components are created for
     */
    public ViaMultiActionDialog(String defaultNamespace, Function<String, TextComponent> componentDeserializer, SerializerVersion baseSerializer) {
        super(defaultNamespace, componentDeserializer, baseSerializer);
    }

    @Override
    public ViaMultiActionDialog columns(int columns) {
        this.columns = columns;
        return this;
    }

    @Override
    public ViaMultiActionDialog action(Consumer<ViaDialogActionBuilder> action) {
        if (actions == null) {
            actions = new ArrayList<>();
        }
        actions.add(getActionBuilder(action));
        return this;
    }

    @Override
    public ViaMultiActionDialog exitAction(@Nullable Consumer<ViaDialogActionBuilder> action) {
        this.exitAction = action == null ? null : getActionBuilder(action);
        return this;
    }

    @Override
    protected void writeDialogType(CompoundTag tag, SerializerVersion target) {
        tag.putString("type", "minecraft:multi_action");
        ListTag<CompoundTag> actionsTag = new ListTag<>(CompoundTag.class);
        if (actions != null) {
            for (ViaDialogActionBuilder action : actions) {
                actionsTag.add(action.getAction(getBaseSerializer(), target));
            }
        }
        tag.put("actions", actionsTag);
        if (exitAction != null) {
            tag.put("exit_action", exitAction.getAction(getBaseSerializer(), target));
        }
        tag.putInt("columns", columns > 0 ? columns : DEFAULT_COLUMNS);
    }
}
