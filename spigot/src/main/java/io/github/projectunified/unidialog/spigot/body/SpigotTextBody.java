package io.github.projectunified.unidialog.spigot.body;

import io.github.projectunified.unidialog.core.body.TextBody;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.dialog.body.DialogBody;
import net.md_5.bungee.api.dialog.body.PlainMessageBody;

public class SpigotTextBody implements TextBody<SpigotTextBody>, SpigotDialogBody {
    private BaseComponent text;
    private int width;

    /**
     * Set the text for the body
     *
     * @param text the text to set
     * @return the instance of the text body for method chaining
     */
    public SpigotTextBody text(BaseComponent text) {
        this.text = text;
        return this;
    }

    @Override
    public SpigotTextBody text(String text) {
        return text(TextComponent.fromLegacy(text));
    }

    @Override
    public SpigotTextBody width(int width) {
        this.width = width;
        return this;
    }

    @Override
    public DialogBody getDialogBody() {
        return new PlainMessageBody(
                text == null ? new TextComponent("") : text,
                width > 0 ? width : 200
        );
    }
}
