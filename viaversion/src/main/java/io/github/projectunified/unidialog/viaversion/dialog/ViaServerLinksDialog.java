package io.github.projectunified.unidialog.viaversion.dialog;

import com.viaversion.nbt.tag.CompoundTag;
import com.viaversion.viaversion.api.minecraft.item.Item;
import com.viaversion.viaversion.libs.mcstructs.text.TextComponent;
import com.viaversion.viaversion.util.SerializerVersion;
import io.github.projectunified.unidialog.core.dialog.ServerLinksDialog;
import io.github.projectunified.unidialog.viaversion.action.ViaDialogActionBuilder;
import io.github.projectunified.unidialog.viaversion.body.ViaDialogBodyBuilder;
import io.github.projectunified.unidialog.viaversion.input.ViaDialogInputBuilder;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * A ViaVersion-based implementation of {@link ServerLinksDialog} for building server links dialogs.
 */
public class ViaServerLinksDialog extends ViaDialog<ViaServerLinksDialog> implements ServerLinksDialog<Item, ViaDialogBodyBuilder, ViaDialogInputBuilder, ViaDialog<?>, ViaDialogActionBuilder, ViaServerLinksDialog> {
    private ViaDialogActionBuilder exitAction;
    private int columns;
    private int buttonWidth;

    /**
     * Constructor for ViaServerLinksDialog
     *
     * @param defaultNamespace      the default namespace for custom actions
     * @param componentDeserializer the function to deserialize components from strings
     * @param baseSerializer        the serializer of the server version the components are created for
     */
    public ViaServerLinksDialog(String defaultNamespace, Function<String, TextComponent> componentDeserializer, SerializerVersion baseSerializer) {
        super(defaultNamespace, componentDeserializer, baseSerializer);
    }

    @Override
    public ViaServerLinksDialog exitAction(@Nullable Consumer<ViaDialogActionBuilder> action) {
        this.exitAction = action == null ? null : getActionBuilder(action);
        return this;
    }

    @Override
    public ViaServerLinksDialog columns(int columns) {
        this.columns = columns;
        return this;
    }

    @Override
    public ViaServerLinksDialog buttonWidth(int buttonWidth) {
        this.buttonWidth = buttonWidth;
        return this;
    }

    @Override
    protected void writeDialogType(CompoundTag tag, SerializerVersion target) {
        tag.putString("type", "minecraft:server_links");
        if (exitAction != null) {
            tag.put("exit_action", exitAction.getAction(getBaseSerializer(), target));
        }
        tag.putInt("columns", columns > 0 ? columns : DEFAULT_COLUMNS);
        tag.putInt("button_width", buttonWidth > 0 ? buttonWidth : DEFAULT_BUTTON_WIDTH);
    }
}
