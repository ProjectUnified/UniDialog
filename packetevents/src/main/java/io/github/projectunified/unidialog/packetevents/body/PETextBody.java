package io.github.projectunified.unidialog.packetevents.body;

import com.github.retrooper.packetevents.protocol.dialog.body.DialogBody;
import com.github.retrooper.packetevents.protocol.dialog.body.PlainMessage;
import com.github.retrooper.packetevents.protocol.dialog.body.PlainMessageDialogBody;
import io.github.projectunified.unidialog.core.body.TextBody;
import io.github.retrooper.packetevents.adventure.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.Component;

public class PETextBody implements TextBody<PETextBody>, PEDialogBody {
    private Component text = Component.empty();
    private int width = 0;

    public PETextBody text(Component text) {
        this.text = text;
        return this;
    }

    @Override
    public PETextBody text(String text) {
        return text(LegacyComponentSerializer.legacySection().deserialize(text));
    }

    @Override
    public PETextBody width(int width) {
        this.width = width;
        return this;
    }

    public PlainMessage getPlainMessage() {
        return new PlainMessage(text, width > 0 ? width : 200);
    }

    @Override
    public DialogBody getDialogBody() {
        return new PlainMessageDialogBody(getPlainMessage());
    }
}
