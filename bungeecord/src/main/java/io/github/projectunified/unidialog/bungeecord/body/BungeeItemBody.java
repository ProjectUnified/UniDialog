package io.github.projectunified.unidialog.bungeecord.body;

import io.github.projectunified.unidialog.core.body.ItemBody;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.dialog.body.DialogBody;
import net.md_5.bungee.api.dialog.body.PlainMessageBody;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class BungeeItemBody implements ItemBody<Object, BungeeTextBody, BungeeItemBody>, BungeeDialogBody {
    private static final BaseComponent UNSUPPORTED_COMPONENT = TextComponent.fromLegacy(ChatColor.translateAlternateColorCodes('&', "&cThis item body is not supported in Spigot!"));

    @Override
    public BungeeItemBody item(Object item) {
        return this;
    }

    @Override
    public BungeeItemBody description(@Nullable Consumer<BungeeTextBody> descriptionBuilder) {
        return this;
    }

    @Override
    public BungeeItemBody showDecorations(boolean showDecorations) {
        return this;
    }

    @Override
    public BungeeItemBody showTooltip(boolean showTooltip) {
        return this;
    }

    @Override
    public BungeeItemBody width(int width) {
        return this;
    }

    @Override
    public BungeeItemBody height(int height) {
        return this;
    }

    @Override
    public DialogBody getDialogBody() {
        return new PlainMessageBody(UNSUPPORTED_COMPONENT);
    }
}
