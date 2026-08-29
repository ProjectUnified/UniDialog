package io.github.projectunified.unidialog.packetevents.body;

import com.github.retrooper.packetevents.protocol.dialog.body.DialogBody;
import com.github.retrooper.packetevents.protocol.dialog.body.PlainMessage;
import com.github.retrooper.packetevents.protocol.dialog.body.PlainMessageDialogBody;
import io.github.projectunified.unidialog.adventure.body.AdventureTextBody;
import net.kyori.adventure.text.Component;

import java.util.function.Function;

/**
 * A PacketEvents-based implementation of {@link AdventureTextBody} for building text bodies.
 */
public class PETextBody implements AdventureTextBody<PETextBody>, PEDialogBody {
    private final Function<String, Component> componentDeserializer;
    private Component text = Component.empty();
    private int width = 0;

    /**
     * Constructor for PETextBody
     *
     * @param componentDeserializer a function to deserialize components from strings
     */
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

    /**
     * Get the plain message for this text body
     *
     * @return the plain message
     */
    public PlainMessage getPlainMessage() {
        return new PlainMessage(text, width > 0 ? width : DEFAULT_WIDTH);
    }

    @Override
    public DialogBody getDialogBody() {
        return new PlainMessageDialogBody(getPlainMessage());
    }
}
