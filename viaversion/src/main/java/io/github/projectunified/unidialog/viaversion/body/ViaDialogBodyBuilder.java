package io.github.projectunified.unidialog.viaversion.body;

import com.viaversion.viaversion.libs.mcstructs.text.TextComponent;
import io.github.projectunified.unidialog.core.body.DialogBodyBuilder;
import io.github.projectunified.unidialog.viaversion.payload.ViaItem;

import java.util.function.Function;

/**
 * A ViaVersion-based implementation of {@link DialogBodyBuilder} for building dialog bodies.
 */
@SuppressWarnings("unchecked")
public class ViaDialogBodyBuilder implements DialogBodyBuilder<ViaItem> {
    private final Function<String, TextComponent> componentDeserializer;
    private ViaDialogBody current;

    /**
     * Constructor for ViaDialogBodyBuilder
     *
     * @param componentDeserializer the function to deserialize components from strings
     */
    public ViaDialogBodyBuilder(Function<String, TextComponent> componentDeserializer) {
        this.componentDeserializer = componentDeserializer;
    }

    @Override
    public ViaItemBody item() {
        ViaItemBody item = new ViaItemBody(componentDeserializer);
        current = item;
        return item;
    }

    @Override
    public ViaTextBody text() {
        ViaTextBody text = new ViaTextBody(componentDeserializer);
        current = text;
        return text;
    }

    /**
     * Get the built dialog body
     *
     * @return the dialog body
     */
    public ViaDialogBody getDialogBody() {
        if (current == null) {
            throw new IllegalStateException("No dialog body has been created yet");
        }
        return current;
    }
}
