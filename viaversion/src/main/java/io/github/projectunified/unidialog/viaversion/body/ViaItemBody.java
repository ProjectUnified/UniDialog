package io.github.projectunified.unidialog.viaversion.body;

import com.viaversion.nbt.tag.CompoundTag;
import com.viaversion.viaversion.api.minecraft.item.Item;
import com.viaversion.viaversion.libs.mcstructs.text.TextComponent;
import com.viaversion.viaversion.util.SerializerVersion;
import io.github.projectunified.unidialog.core.body.ItemBody;
import io.github.projectunified.unidialog.viaversion.ViaDialogTagBuilder;
import io.github.projectunified.unidialog.viaversion.payload.ViaItem;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * A ViaVersion-based implementation of {@link ItemBody} for building item bodies.
 */
public class ViaItemBody implements ItemBody<ViaItem, ViaTextBody, ViaItemBody>, ViaDialogBody {
    private final Function<String, TextComponent> componentDeserializer;
    private ViaItem item;
    private @Nullable ViaTextBody description;
    private boolean showDecorations = true;
    private boolean showTooltip = true;
    private int width;
    private int height;

    /**
     * Constructor for ViaItemBody
     *
     * @param componentDeserializer the function to deserialize components from strings
     */
    public ViaItemBody(Function<String, TextComponent> componentDeserializer) {
        this.componentDeserializer = componentDeserializer;
    }

    @Override
    public ViaItemBody item(ViaItem item) {
        this.item = item;
        return this;
    }

    /**
     * Set the item of the body, wrapping a ViaVersion {@link Item}
     *
     * @param item the item to set
     * @return the item body itself for method chaining
     */
    public ViaItemBody item(Item item) {
        return item(ViaItem.fromItem(item));
    }

    @Override
    public ViaItemBody description(@Nullable Consumer<ViaTextBody> descriptionBuilder) {
        if (descriptionBuilder == null) {
            this.description = null;
        } else {
            ViaTextBody textBody = new ViaTextBody(componentDeserializer);
            descriptionBuilder.accept(textBody);
            this.description = textBody;
        }
        return this;
    }

    @Override
    public ViaItemBody showDecorations(boolean showDecorations) {
        this.showDecorations = showDecorations;
        return this;
    }

    @Override
    public ViaItemBody showTooltip(boolean showTooltip) {
        this.showTooltip = showTooltip;
        return this;
    }

    @Override
    public ViaItemBody width(int width) {
        this.width = width;
        return this;
    }

    @Override
    public ViaItemBody height(int height) {
        this.height = height;
        return this;
    }

    @Override
    public CompoundTag toTag(SerializerVersion baseVersion, SerializerVersion targetVersion) {
        if (item == null) {
            throw new IllegalStateException("Item must be set");
        }
        CompoundTag tag = new CompoundTag();
        tag.putString("type", "minecraft:item");
        tag.put("item", item.toTag(baseVersion, targetVersion));
        if (description != null) {
            CompoundTag descriptionTag = new CompoundTag();
            descriptionTag.put("contents", ViaDialogTagBuilder.component(description.text(), baseVersion, targetVersion));
            descriptionTag.putInt("width", description.width() > 0 ? description.width() : DEFAULT_WIDTH);
            tag.put("description", descriptionTag);
        }
        tag.putBoolean("show_decorations", showDecorations);
        tag.putBoolean("show_tooltip", showTooltip);
        tag.putInt("width", width > 0 ? width : DEFAULT_WIDTH);
        tag.putInt("height", height > 0 ? height : DEFAULT_HEIGHT);
        return tag;
    }
}
