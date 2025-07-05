package io.github.projectunified.unidialog.packetevents.body;

import com.github.retrooper.packetevents.protocol.dialog.body.DialogBody;
import com.github.retrooper.packetevents.protocol.item.ItemStack;
import io.github.projectunified.unidialog.core.body.DialogBodyBuilder;
import net.kyori.adventure.text.Component;

import java.util.function.Function;

@SuppressWarnings("unchecked")
public class PEDialogBodyBuilder implements DialogBodyBuilder<ItemStack>, PEDialogBody {
    private final Function<String, Component> componentDeserializer;
    private PEDialogBody current;

    public PEDialogBodyBuilder(Function<String, Component> componentDeserializer) {
        this.componentDeserializer = componentDeserializer;
    }

    @Override
    public PEItemBody item() {
        PEItemBody item = new PEItemBody(componentDeserializer);
        current = item;
        return item;
    }

    @Override
    public PETextBody text() {
        PETextBody text = new PETextBody(componentDeserializer);
        current = text;
        return text;
    }

    @Override
    public DialogBody getDialogBody() {
        if (current == null) {
            throw new IllegalStateException("No dialog body has been created yet");
        }
        return current.getDialogBody();
    }
}
