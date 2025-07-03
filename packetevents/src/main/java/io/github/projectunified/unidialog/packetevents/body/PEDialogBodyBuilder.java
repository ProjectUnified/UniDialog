package io.github.projectunified.unidialog.packetevents.body;

import com.github.retrooper.packetevents.protocol.dialog.body.DialogBody;
import com.github.retrooper.packetevents.protocol.item.ItemStack;
import io.github.projectunified.unidialog.core.body.DialogBodyBuilder;

@SuppressWarnings("unchecked")
public class PEDialogBodyBuilder implements DialogBodyBuilder<ItemStack>, PEDialogBody {
    private PEDialogBody current;

    @Override
    public PEItemBody item() {
        PEItemBody item = new PEItemBody();
        current = item;
        return item;
    }

    @Override
    public PETextBody text() {
        PETextBody text = new PETextBody();
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
