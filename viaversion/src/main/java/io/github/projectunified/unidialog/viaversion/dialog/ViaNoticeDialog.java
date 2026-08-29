package io.github.projectunified.unidialog.viaversion.dialog;

import com.viaversion.nbt.tag.CompoundTag;
import com.viaversion.viaversion.api.minecraft.item.Item;
import com.viaversion.viaversion.libs.mcstructs.text.TextComponent;
import com.viaversion.viaversion.util.SerializerVersion;
import io.github.projectunified.unidialog.core.dialog.NoticeDialog;
import io.github.projectunified.unidialog.viaversion.action.ViaDialogActionBuilder;
import io.github.projectunified.unidialog.viaversion.body.ViaDialogBodyBuilder;
import io.github.projectunified.unidialog.viaversion.input.ViaDialogInputBuilder;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * A ViaVersion-based implementation of {@link NoticeDialog} for building notice dialogs.
 */
public class ViaNoticeDialog extends ViaDialog<ViaNoticeDialog> implements NoticeDialog<Item, ViaDialogBodyBuilder, ViaDialogInputBuilder, ViaDialog<?>, ViaDialogActionBuilder, ViaNoticeDialog> {
    private ViaDialogActionBuilder action;

    /**
     * Constructor for ViaNoticeDialog
     *
     * @param defaultNamespace      the default namespace for custom actions
     * @param componentDeserializer the function to deserialize components from strings
     * @param baseSerializer        the serializer of the server version the components are created for
     */
    public ViaNoticeDialog(String defaultNamespace, Function<String, TextComponent> componentDeserializer, SerializerVersion baseSerializer) {
        super(defaultNamespace, componentDeserializer, baseSerializer);
    }

    @Override
    public ViaNoticeDialog action(Consumer<ViaDialogActionBuilder> action) {
        this.action = getActionBuilder(action);
        return this;
    }

    @Override
    protected void writeDialogType(CompoundTag tag, SerializerVersion target) {
        tag.putString("type", "minecraft:notice");
        if (action != null) {
            tag.put("action", action.getAction(getBaseSerializer(), target));
        }
    }
}
