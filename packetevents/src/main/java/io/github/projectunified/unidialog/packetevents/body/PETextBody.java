package io.github.projectunified.unidialog.packetevents.body;

import com.github.retrooper.packetevents.protocol.dialog.body.DialogBody;
import com.github.retrooper.packetevents.protocol.dialog.body.PlainMessage;
import com.github.retrooper.packetevents.protocol.dialog.body.PlainMessageDialogBody;
import io.github.projectunified.unidialog.adventure.body.AdventureTextBody;
import net.kyori.adventure.text.Component;

import java.util.function.Function;

public class PETextBody implements AdventureTextBody<PETextBody>, PEDialogBody {
    private final Function<String, Component> componentDeserializer;
    private Component text = Component.empty();
    private int width = 0;

    public PETextBody(Function<String, Component> componentDeserializer) {
        this.componentDeserializer = componentDeserializer;
    }

    @Override
    public Function<String, Component> getComponentDeserializer() {
        return componentDeserializer;
    }

    @Override
    public PETextBody text(Component text) {
        this.text = text;
        return this;
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
