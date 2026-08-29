package io.github.projectunified.unidialog.viaversion.dialog;

import com.viaversion.nbt.tag.CompoundTag;
import com.viaversion.viaversion.api.minecraft.item.Item;
import com.viaversion.viaversion.libs.mcstructs.text.TextComponent;
import com.viaversion.viaversion.util.SerializerVersion;
import io.github.projectunified.unidialog.core.dialog.ConfirmationDialog;
import io.github.projectunified.unidialog.viaversion.action.ViaDialogActionBuilder;
import io.github.projectunified.unidialog.viaversion.body.ViaDialogBodyBuilder;
import io.github.projectunified.unidialog.viaversion.input.ViaDialogInputBuilder;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * A ViaVersion-based implementation of {@link ConfirmationDialog} for building confirmation dialogs.
 */
public class ViaConfirmationDialog extends ViaDialog<ViaConfirmationDialog> implements ConfirmationDialog<Item, ViaDialogBodyBuilder, ViaDialogInputBuilder, ViaDialog<?>, ViaDialogActionBuilder, ViaConfirmationDialog> {
    private ViaDialogActionBuilder yesAction;
    private ViaDialogActionBuilder noAction;

    /**
     * Constructor for ViaConfirmationDialog
     *
     * @param defaultNamespace      the default namespace for custom actions
     * @param componentDeserializer the function to deserialize components from strings
     * @param baseSerializer        the serializer of the server version the components are created for
     */
    public ViaConfirmationDialog(String defaultNamespace, Function<String, TextComponent> componentDeserializer, SerializerVersion baseSerializer) {
        super(defaultNamespace, componentDeserializer, baseSerializer);
    }

    private static CompoundTag defaultButton(String translateKey) {
        CompoundTag label = new CompoundTag();
        label.putString("translate", translateKey);
        CompoundTag tag = new CompoundTag();
        tag.put("label", label);
        tag.putInt("width", 150);
        return tag;
    }

    @Override
    public ViaConfirmationDialog yesAction(Consumer<ViaDialogActionBuilder> action) {
        this.yesAction = getActionBuilder(action);
        return this;
    }

    @Override
    public ViaConfirmationDialog noAction(Consumer<ViaDialogActionBuilder> action) {
        this.noAction = getActionBuilder(action);
        return this;
    }

    @Override
    public boolean hasYesAction() {
        return yesAction != null;
    }

    @Override
    public boolean hasNoAction() {
        return noAction != null;
    }

    @Override
    protected void writeDialogType(CompoundTag tag, SerializerVersion target) {
        tag.putString("type", "minecraft:confirmation");
        tag.put("yes", yesAction != null ? yesAction.getAction(getBaseSerializer(), target) : defaultButton("gui.yes"));
        tag.put("no", noAction != null ? noAction.getAction(getBaseSerializer(), target) : defaultButton("gui.no"));
    }
}
