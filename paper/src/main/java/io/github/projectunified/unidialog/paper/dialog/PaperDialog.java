package io.github.projectunified.unidialog.paper.dialog;

import io.github.projectunified.unidialog.adventure.dialog.AdventureDialog;
import io.github.projectunified.unidialog.paper.action.PaperDialogActionBuilder;
import io.github.projectunified.unidialog.paper.body.PaperDialogBodyBuilder;
import io.github.projectunified.unidialog.paper.input.PaperDialogInputBuilder;
import io.github.projectunified.unidialog.paper.opener.PaperDialogOpener;
import io.papermc.paper.dialog.Dialog;
import io.papermc.paper.registry.data.dialog.ActionButton;
import io.papermc.paper.registry.data.dialog.DialogBase;
import io.papermc.paper.registry.data.dialog.DialogRegistryEntry;
import io.papermc.paper.registry.data.dialog.body.DialogBody;
import io.papermc.paper.registry.data.dialog.input.DialogInput;
import io.papermc.paper.registry.data.dialog.type.DialogType;
import net.kyori.adventure.text.Component;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

@SuppressWarnings("unchecked")
public abstract class PaperDialog<T extends PaperDialog<T>> implements AdventureDialog<ItemStack, PaperDialogBodyBuilder, PaperDialogInputBuilder, T> {
    private final String defaultNamespace;
    private final Function<String, Component> componentDeserializer;

    private Component title;
    private @Nullable Component externalTitle;
    private boolean canCloseWithEscape = true;
    private boolean pause = false;
    private DialogBase.DialogAfterAction afterAction;
    private List<DialogBody> body;
    private List<DialogInput> input;

    protected PaperDialog(String defaultNamespace, Function<String, Component> componentDeserializer) {
        this.defaultNamespace = defaultNamespace;
        this.componentDeserializer = componentDeserializer;
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
    public Function<String, Component> getComponentDeserializer() {
        return componentDeserializer;
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
        this.afterAction = switch (afterAction) {
            case CLOSE -> DialogBase.DialogAfterAction.CLOSE;
            case WAIT_FOR_RESPONSE -> DialogBase.DialogAfterAction.WAIT_FOR_RESPONSE;
            case NONE -> DialogBase.DialogAfterAction.NONE;
        };
        return (T) this;
    }

    @Override
    public T body(Consumer<PaperDialogBodyBuilder> bodyBuilder) {
        if (body == null) {
            body = new ArrayList<>();
        }
        PaperDialogBodyBuilder builder = new PaperDialogBodyBuilder(componentDeserializer);
        bodyBuilder.accept(builder);
        body.add(builder.getDialogBody());
        return (T) this;
    }

    @Override
    public T input(String key, Consumer<PaperDialogInputBuilder> inputBuilder) {
        if (input == null) {
            input = new ArrayList<>();
        }
        PaperDialogInputBuilder builder = new PaperDialogInputBuilder(key, componentDeserializer);
        inputBuilder.accept(builder);
        input.add(builder.getDialogInput());
        return (T) this;
    }

    protected ActionButton getAction(Consumer<PaperDialogActionBuilder> actionBuilder) {
        PaperDialogActionBuilder builder = new PaperDialogActionBuilder(defaultNamespace, componentDeserializer);
        actionBuilder.accept(builder);
        return builder.getAction();
    }

    protected abstract DialogType getDialogType();

    private DialogBase getDialogBase() {
        return DialogBase.create(
                title != null ? title : Component.text("Dialog"),
                externalTitle,
                canCloseWithEscape,
                pause,
                afterAction,
                body != null ? body : Collections.emptyList(),
                input != null ? input : Collections.emptyList()
        );
    }

    public final Consumer<DialogRegistryEntry.Builder> getDialogBuilder() {
        return builder -> builder.base(getDialogBase()).type(getDialogType());
    }

    public final Dialog getDialog() {
        return Dialog.create(factory -> getDialogBuilder().accept(factory.empty()));
    }

    @Override
    public PaperDialogOpener opener() {
        return new PaperDialogOpener(getDialog());
    }
}
