package io.github.projectunified.unidialog.paper.body;

import io.github.projectunified.unidialog.core.body.DialogBodyBuilder;
import io.papermc.paper.registry.data.dialog.body.DialogBody;
import net.kyori.adventure.text.Component;
import org.bukkit.inventory.ItemStack;

import java.util.function.Function;

@SuppressWarnings("unchecked")
public class PaperDialogBodyBuilder implements DialogBodyBuilder<ItemStack>, PaperDialogBody {
    private final Function<String, Component> componentDeserializer;
    private PaperDialogBody current;

    public PaperDialogBodyBuilder(Function<String, Component> componentDeserializer) {
        this.componentDeserializer = componentDeserializer;
    }

    @Override
    public PaperItemBody item() {
        PaperItemBody itemBody = new PaperItemBody(componentDeserializer);
        current = itemBody;
        return itemBody;
    }

    @Override
    public PaperTextBody text() {
        PaperTextBody textBody = new PaperTextBody(componentDeserializer);
        current = textBody;
        return textBody;
    }

    @Override
    public DialogBody getDialogBody() {
        if (current == null) {
            throw new IllegalStateException("No dialog body has been created yet.");
        }
        return current.getDialogBody();
    }
}
