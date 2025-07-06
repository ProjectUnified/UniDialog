package io.github.projectunified.unidialog.spigot.body;

import io.github.projectunified.unidialog.core.body.ItemBody;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.dialog.body.DialogBody;
import net.md_5.bungee.api.dialog.body.PlainMessageBody;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class SpigotItemBody implements ItemBody<ItemStack, SpigotTextBody, SpigotItemBody>, SpigotDialogBody {
    private static final BaseComponent UNSUPPORTED_COMPONENT = TextComponent.fromLegacy(ChatColor.translateAlternateColorCodes('&', "&cThis item body is not supported in Spigot!"));

    @Override
    public SpigotItemBody item(ItemStack item) {
        return this;
    }

    @Override
    public SpigotItemBody description(@Nullable Consumer<SpigotTextBody> descriptionBuilder) {
        return this;
    }

    @Override
    public SpigotItemBody showDecorations(boolean showDecorations) {
        return this;
    }

    @Override
    public SpigotItemBody showTooltip(boolean showTooltip) {
        return this;
    }

    @Override
    public SpigotItemBody width(int width) {
        return this;
    }

    @Override
    public SpigotItemBody height(int height) {
        return this;
    }

    @Override
    public DialogBody getDialogBody() {
        return new PlainMessageBody(UNSUPPORTED_COMPONENT);
    }
}
