package io.github.projectunified.unidialog.packetevents.body;

import com.github.retrooper.packetevents.protocol.dialog.body.DialogBody;
import com.github.retrooper.packetevents.protocol.dialog.body.ItemDialogBody;
import com.github.retrooper.packetevents.protocol.item.ItemStack;
import io.github.projectunified.unidialog.core.body.ItemBody;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;
import java.util.function.Function;

public class PEItemBody implements ItemBody<ItemStack, PETextBody, PEItemBody>, PEDialogBody {
    private final Function<String, Component> componentDeserializer;
    private ItemStack itemStack;
    private @Nullable PETextBody description;
    private boolean showDecorations = true;
    private boolean showTooltip = true;
    private int width;
    private int height;

    public PEItemBody(Function<String, Component> componentDeserializer) {
        this.componentDeserializer = componentDeserializer;
    }

    @Override
    public PEItemBody item(ItemStack item) {
        this.itemStack = item;
        return this;
    }

    @Override
    public PEItemBody description(@Nullable Consumer<PETextBody> descriptionBuilder) {
        if (descriptionBuilder == null) {
            this.description = null;
        } else {
            PETextBody textBody = new PETextBody(componentDeserializer);
            descriptionBuilder.accept(textBody);
            this.description = textBody;
        }
        return this;
    }

    @Override
    public PEItemBody showDecorations(boolean showDecorations) {
        this.showDecorations = showDecorations;
        return this;
    }

    @Override
    public PEItemBody showTooltip(boolean showTooltip) {
        this.showTooltip = showTooltip;
        return this;
    }

    @Override
    public PEItemBody width(int width) {
        this.width = width;
        return this;
    }

    @Override
    public PEItemBody height(int height) {
        this.height = height;
        return this;
    }

    @Override
    public DialogBody getDialogBody() {
        if (itemStack == null) {
            throw new IllegalStateException("ItemStack must be set");
        }
        return new ItemDialogBody(
                itemStack,
                description != null ? description.getPlainMessage() : null,
                showDecorations,
                showTooltip,
                width > 0 ? width : DEFAULT_WIDTH,
                height > 0 ? height : DEFAULT_HEIGHT
        );
    }
}
