package io.github.projectunified.unidialog.paper.body;

import io.github.projectunified.unidialog.core.body.ItemBody;
import io.papermc.paper.registry.data.dialog.body.DialogBody;
import io.papermc.paper.registry.data.dialog.body.PlainMessageDialogBody;
import net.kyori.adventure.text.Component;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;
import java.util.function.Function;

public class PaperItemBody implements ItemBody<ItemStack, PaperTextBody, PaperItemBody>, PaperDialogBody {
    private final Function<String, Component> componentDeserializer;
    private ItemStack item;
    private @Nullable PlainMessageDialogBody description;
    private boolean showDecorations = true;
    private boolean showTooltip = true;
    private int width;
    private int height;

    public PaperItemBody(Function<String, Component> componentDeserializer) {
        this.componentDeserializer = componentDeserializer;
    }

    @Override
    public PaperItemBody item(ItemStack item) {
        this.item = item;
        return this;
    }

    @Override
    public PaperItemBody description(@Nullable Consumer<PaperTextBody> descriptionBuilder) {
        if (descriptionBuilder == null) {
            this.description = null;
        } else {
            PaperTextBody textBody = new PaperTextBody(componentDeserializer);
            descriptionBuilder.accept(textBody);
            this.description = textBody.getDialogBody();
        }
        return this;
    }

    @Override
    public PaperItemBody showDecorations(boolean showDecorations) {
        this.showDecorations = showDecorations;
        return this;
    }

    @Override
    public PaperItemBody showTooltip(boolean showTooltip) {
        this.showTooltip = showTooltip;
        return this;
    }

    @Override
    public PaperItemBody width(int width) {
        this.width = width;
        return this;
    }

    @Override
    public PaperItemBody height(int height) {
        this.height = height;
        return this;
    }

    @Override
    public DialogBody getDialogBody() {
        if (item == null) {
            throw new IllegalStateException("Item must be set before getting the dialog body.");
        }
        return DialogBody.item(
                item,
                description,
                showDecorations,
                showTooltip,
                width > 0 ? width : DEFAULT_WIDTH,
                height > 0 ? height : DEFAULT_HEIGHT
        );
    }
}
