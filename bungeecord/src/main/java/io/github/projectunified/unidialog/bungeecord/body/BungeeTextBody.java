package io.github.projectunified.unidialog.bungeecord.body;

import io.github.projectunified.unidialog.core.body.TextBody;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.dialog.body.DialogBody;
import net.md_5.bungee.api.dialog.body.PlainMessageBody;

public class BungeeTextBody implements TextBody<BungeeTextBody>, BungeeDialogBody {
    private BaseComponent text;
    private int width;

    /**
     * Set the text for the body
     *
     * @param text the text to set
     * @return the instance of the text body for method chaining
     */
    public BungeeTextBody text(BaseComponent text) {
        this.text = text;
        return this;
    }

    @Override
    public BungeeTextBody text(String text) {
        return text(TextComponent.fromLegacy(text));
    }

    @Override
    public BungeeTextBody width(int width) {
        this.width = width;
        return this;
    }

    @Override
    public DialogBody getDialogBody() {
        return new PlainMessageBody(
                text != null ? text : new TextComponent(""),
                width > 0 ? width : DEFAULT_WIDTH
        );
    }
}
