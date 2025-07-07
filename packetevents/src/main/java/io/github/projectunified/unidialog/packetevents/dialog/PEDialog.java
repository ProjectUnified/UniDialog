package io.github.projectunified.unidialog.packetevents.dialog;

import com.github.retrooper.packetevents.protocol.dialog.CommonDialogData;
import com.github.retrooper.packetevents.protocol.dialog.DialogAction;
import com.github.retrooper.packetevents.protocol.dialog.body.DialogBody;
import com.github.retrooper.packetevents.protocol.dialog.button.ActionButton;
import com.github.retrooper.packetevents.protocol.dialog.input.Input;
import com.github.retrooper.packetevents.protocol.dialog.input.InputControl;
import com.github.retrooper.packetevents.protocol.item.ItemStack;
import io.github.projectunified.unidialog.adventure.dialog.AdventureDialog;
import io.github.projectunified.unidialog.core.opener.DialogOpener;
import io.github.projectunified.unidialog.packetevents.action.PEDialogActionBuilder;
import io.github.projectunified.unidialog.packetevents.body.PEDialogBodyBuilder;
import io.github.projectunified.unidialog.packetevents.input.PEDialogInputBuilder;
import io.github.projectunified.unidialog.packetevents.opener.PEDialogOpener;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;

@SuppressWarnings("unchecked")
public abstract class PEDialog<T extends PEDialog<T>> implements AdventureDialog<ItemStack, PEDialogBodyBuilder, PEDialogInputBuilder, T> {
    private final String defaultNamespace;
    private final Function<String, Component> componentDeserializer;
    private final Function<UUID, @Nullable Object> playerFunction;

    private Component title;
    private @Nullable Component externalTitle;
    private boolean canCloseWithEscape = true;
    private boolean pause = false;
    private AfterAction afterAction = AfterAction.CLOSE;
    private List<DialogBody> bodies;
    private List<Input> inputs;

    protected PEDialog(String defaultNamespace, Function<String, Component> componentDeserializer, Function<UUID, @Nullable Object> playerFunction) {
        this.defaultNamespace = defaultNamespace;
        this.componentDeserializer = componentDeserializer;
        this.playerFunction = playerFunction;
    }

    private static DialogAction toPEDialogAction(AfterAction afterAction) {
        return switch (afterAction) {
            case CLOSE -> DialogAction.CLOSE;
            case WAIT_FOR_RESPONSE -> DialogAction.WAIT_FOR_RESPONSE;
            case NONE -> DialogAction.NONE;
        };
    }

    @Override
    public Function<String, Component> getComponentDeserializer() {
        return componentDeserializer;
    }

    @Override
    public T title(Component title) {
        this.title = title;
        return (T) this;
    }

    @Override
    public T externalTitle(@Nullable Component externalTitle) {
        this.externalTitle = externalTitle;
        return (T) this;
    }

    @Override
    public T canCloseWithEscape(boolean canCloseWithEscape) {
        this.canCloseWithEscape = canCloseWithEscape;
        return (T) this;
    }

    @Override
    public T pause(boolean pause) {
        this.pause = pause;
        return (T) this;
    }

    @Override
    public T afterAction(AfterAction afterAction) {
        this.afterAction = afterAction;
        return (T) this;
    }

    @Override
    public T body(Consumer<PEDialogBodyBuilder> bodyBuilder) {
        if (bodies == null) {
            bodies = new ArrayList<>();
        }
        PEDialogBodyBuilder builder = new PEDialogBodyBuilder(componentDeserializer);
        bodyBuilder.accept(builder);
        DialogBody dialogBody = builder.getDialogBody();
        bodies.add(dialogBody);
        return (T) this;
    }

    @Override
    public T input(String key, Consumer<PEDialogInputBuilder> inputBuilder) {
        if (inputs == null) {
            inputs = new ArrayList<>();
        }
        PEDialogInputBuilder builder = new PEDialogInputBuilder(componentDeserializer);
        inputBuilder.accept(builder);
        InputControl inputControl = builder.getInput();
        inputs.add(new Input(key, inputControl));
        return (T) this;
    }

    protected ActionButton getAction(Consumer<PEDialogActionBuilder> action) {
        PEDialogActionBuilder actionBuilder = new PEDialogActionBuilder(defaultNamespace, componentDeserializer);
        action.accept(actionBuilder);
        return actionBuilder.getAction();
    }

    protected abstract com.github.retrooper.packetevents.protocol.dialog.Dialog constructDialog(CommonDialogData commonDialogData);

    public final com.github.retrooper.packetevents.protocol.dialog.Dialog getDialog() {
        CommonDialogData commonDialogData = new CommonDialogData(
                title != null ? title : Component.text("Dialog"),
                externalTitle,
                canCloseWithEscape,
                pause,
                toPEDialogAction(afterAction),
                bodies != null ? bodies : Collections.emptyList(),
                inputs != null ? inputs : Collections.emptyList()
        );
        return constructDialog(commonDialogData);
    }

    @Override
    public PEDialogOpener opener() {
        return new PEDialogOpener(getDialog(), playerFunction);
    }
}
