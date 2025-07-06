package io.github.projectunified.unidialog.spigot.body;

import io.github.projectunified.unidialog.core.body.DialogBodyBuilder;
import net.md_5.bungee.api.dialog.body.DialogBody;
import org.bukkit.inventory.ItemStack;

@SuppressWarnings("unchecked")
public class SpigotDialogBodyBuilder implements DialogBodyBuilder<ItemStack>, SpigotDialogBody {
    private SpigotDialogBody current;

    @Override
    public SpigotItemBody item() {
        current = new SpigotItemBody();
        return (SpigotItemBody) current;
    }

    @Override
    public SpigotTextBody text() {
        current = new SpigotTextBody();
        return (SpigotTextBody) current;
    }

    @Override
    public DialogBody getDialogBody() {
        if (current == null) {
            throw new IllegalStateException("No dialog body has been created yet.");
        }
        return current.getDialogBody();
    }
}
