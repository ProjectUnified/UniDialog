package io.github.projectunified.unidialog.viaversion.body;

import com.viaversion.nbt.tag.CompoundTag;
import com.viaversion.viaversion.libs.mcstructs.text.TextComponent;
import com.viaversion.viaversion.libs.mcstructs.text.components.StringComponent;
import com.viaversion.viaversion.util.SerializerVersion;
import io.github.projectunified.unidialog.core.body.TextBody;
import io.github.projectunified.unidialog.viaversion.ViaDialogTagBuilder;

import java.util.function.Function;

/**
 * A ViaVersion-based implementation of {@link TextBody} for building text bodies.
 */
public class ViaTextBody implements TextBody<ViaTextBody>, ViaDialogBody {
    private final Function<String, TextComponent> componentDeserializer;
    private TextComponent text;
    private int width = 0;

    /**
     * Constructor for ViaTextBody
     *
     * @param componentDeserializer the function to deserialize components from strings
     */
    public ViaTextBody(Function<String, TextComponent> componentDeserializer) {
        this.componentDeserializer = componentDeserializer;
    }

    /**
     * Set the text for the body
     *
     * @param text the text
     * @return the text body itself for method chaining
     */
    public ViaTextBody text(TextComponent text) {
        this.text = text;
        return this;
    }

    @Override
    public ViaTextBody text(String text) {
        return text(componentDeserializer.apply(text));
    }

    @Override
    public ViaTextBody width(int width) {
        this.width = width;
        return this;
    }

    @Override
    public CompoundTag toTag(SerializerVersion baseVersion, SerializerVersion targetVersion) {
        CompoundTag tag = new CompoundTag();
        tag.putString("type", "minecraft:plain_message");
        tag.put("contents", ViaDialogTagBuilder.component(text != null ? text : new StringComponent(""), baseVersion, targetVersion));
        tag.putInt("width", width > 0 ? width : DEFAULT_WIDTH);
        return tag;
    }

    TextComponent text() {
        return text;
    }

    int width() {
        return width;
    }
}
