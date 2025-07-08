package io.github.projectunified.unidialog.paper.dialog;

import io.github.projectunified.unidialog.adventure.dialog.AdventureDialog;
import io.github.projectunified.unidialog.paper.action.PaperDialogActionBuilder;
import io.github.projectunified.unidialog.paper.body.PaperDialogBodyBuilder;
import io.github.projectunified.unidialog.paper.input.PaperDialogInputBuilder;
import io.github.projectunified.unidialog.paper.opener.PaperDialogOpener;
import io.papermc.paper.dialog.Dialog;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.TypedKey;
import io.papermc.paper.registry.data.dialog.ActionButton;
import io.papermc.paper.registry.data.dialog.DialogBase;
import io.papermc.paper.registry.data.dialog.DialogRegistryEntry;
import io.papermc.paper.registry.data.dialog.body.DialogBody;
import io.papermc.paper.registry.data.dialog.input.DialogInput;
import io.papermc.paper.registry.data.dialog.type.DialogType;
import io.papermc.paper.registry.event.RegistryComposeEvent;
import io.papermc.paper.registry.event.WritableRegistry;
import net.kyori.adventure.key.Key;
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

    /**
     * Get the dialog builder consumer that can be used to create a dialog
     *
     * @return a consumer that accepts a DialogRegistryEntry.Builder
     */
    public final Consumer<DialogRegistryEntry.Builder> getDialogBuilder() {
        return builder -> builder.base(getDialogBase()).type(getDialogType());
    }

    /**
     * Create a dialog using the provided factory
     *
     * @return a Dialog instance created using the dialog builder
     */
    public final Dialog getDialog() {
        return Dialog.create(factory -> getDialogBuilder().accept(factory.empty()));
    }

    /**
     * Register the dialog with the provided key in the given registry.
     *
     * @param key      the key to register the dialog under
     * @param registry the registry to register the dialog in
     */
    public final void register(Key key, WritableRegistry<Dialog, DialogRegistryEntry.Builder> registry) {
        registry.register(TypedKey.create(RegistryKey.DIALOG, key), getDialogBuilder());
    }

    /**
     * Register the dialog with the provided key using the given event.
     *
     * @param key   the key to register the dialog under
     * @param event the registry compose event to register the dialog in
     */
    public final void register(Key key, RegistryComposeEvent<Dialog, DialogRegistryEntry.Builder> event) {
        register(key, event.registry());
    }

    /**
     * Register the dialog with the default namespace and the provided dialog ID in the given registry.
     *
     * @param namespace the namespace to use
     * @param dialogId  the dialog ID to register under
     * @param registry  the registry to register the dialog in
     */
    public final void register(String namespace, String dialogId, WritableRegistry<Dialog, DialogRegistryEntry.Builder> registry) {
        Key key = Key.key(namespace, dialogId);
        register(key, registry);
    }

    /**
     * Register the dialog with the default namespace and the provided dialog ID using the given event.
     *
     * @param namespace the namespace to use
     * @param dialogId  the dialog ID to register under
     * @param event     the registry compose event to register the dialog in
     */
    public final void register(String namespace, String dialogId, RegistryComposeEvent<Dialog, DialogRegistryEntry.Builder> event) {
        Key key = Key.key(namespace, dialogId);
        register(key, event);
    }

    /**
     * Register the dialog with the default namespace and the provided dialog ID in the given registry.
     *
     * @param dialogId the dialog ID to register under
     * @param registry the registry to register the dialog in
     */
    public final void register(String dialogId, WritableRegistry<Dialog, DialogRegistryEntry.Builder> registry) {
        Key key = Key.key(defaultNamespace, dialogId);
        register(key, registry);
    }

    /**
     * Register the dialog with the default namespace and the provided dialog ID using the given event.
     *
     * @param dialogId the dialog ID to register under
     * @param event    the registry compose event to register the dialog in
     */
    public final void register(String dialogId, RegistryComposeEvent<Dialog, DialogRegistryEntry.Builder> event) {
        Key key = Key.key(defaultNamespace, dialogId);
        register(key, event);
    }

    @Override
    public PaperDialogOpener opener() {
        return new PaperDialogOpener(getDialog());
    }
}
