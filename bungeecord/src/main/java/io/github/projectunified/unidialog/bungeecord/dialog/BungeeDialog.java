package io.github.projectunified.unidialog.bungeecord.dialog;

import io.github.projectunified.unidialog.bungeecord.action.BungeeDialogActionBuilder;
import io.github.projectunified.unidialog.bungeecord.body.BungeeDialogBodyBuilder;
import io.github.projectunified.unidialog.bungeecord.input.BungeeDialogInputBuilder;
import io.github.projectunified.unidialog.bungeecord.opener.BungeeDialogOpener;
import io.github.projectunified.unidialog.core.dialog.Dialog;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.dialog.DialogBase;
import net.md_5.bungee.api.dialog.action.ActionButton;
import net.md_5.bungee.api.dialog.body.DialogBody;
import net.md_5.bungee.api.dialog.input.DialogInput;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * The Bungee implementation of the dialog
 *
 * @param <O> the type of the dialog opener
 * @param <T> the type of the dialog itself, for method chaining
 */
@SuppressWarnings("unchecked")
public abstract class BungeeDialog<O extends BungeeDialogOpener, T extends BungeeDialog<O, T>> implements Dialog<Object, BungeeDialogBodyBuilder, BungeeDialogInputBuilder, T> {
    private final String defaultNamespace;
    private final Function<net.md_5.bungee.api.dialog.Dialog, O> openerFunction;
    private BaseComponent title;
    private @Nullable BaseComponent externalTitle;
    private List<DialogInput> inputs;
    private List<DialogBody> body;
    private boolean canCloseWithEscape = true;
    private boolean pause = false;
    private DialogBase.AfterAction afterAction;

    /**
     * Create a new dialog
     *
     * @param defaultNamespace the default namespace for the dialog actions
     * @param openerFunction   the function to create the dialog opener from the Bungee dialog
     */
    public BungeeDialog(String defaultNamespace, Function<net.md_5.bungee.api.dialog.Dialog, O> openerFunction) {
        this.defaultNamespace = defaultNamespace;
        this.openerFunction = openerFunction;
    }

    /**
     * Set the title of the dialog
     *
     * @param title the title of the dialog
     * @return the dialog itself for method chaining
     */
    public T title(BaseComponent title) {
        this.title = title;
        return (T) this;
    }

    @Override
    public T title(String title) {
        return title(TextComponent.fromLegacy(title));
    }

    /**
     * Set the external title of the dialog, which is used for displaying the dialog in a multi-dialog dialog
     *
     * @param externalTitle the external title of the dialog
     * @return the dialog itself for method chaining
     */
    public T externalTitle(@Nullable BaseComponent externalTitle) {
        this.externalTitle = externalTitle;
        return (T) this;
    }

    @Override
    public T externalTitle(@Nullable String externalTitle) {
        return externalTitle(externalTitle == null ? null : TextComponent.fromLegacy(externalTitle));
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
            case CLOSE -> DialogBase.AfterAction.CLOSE;
            case WAIT_FOR_RESPONSE -> DialogBase.AfterAction.WAIT_FOR_RESPONSE;
            case NONE -> DialogBase.AfterAction.NONE;
        };
        return (T) this;
    }

    @Override
    public T body(Consumer<BungeeDialogBodyBuilder> bodyBuilder) {
        if (body == null) {
            body = new ArrayList<>();
        }
        BungeeDialogBodyBuilder builder = new BungeeDialogBodyBuilder();
        bodyBuilder.accept(builder);
        DialogBody dialogBody = builder.getDialogBody();
        body.add(dialogBody);
        return (T) this;
    }

    @Override
    public T input(String key, Consumer<BungeeDialogInputBuilder> inputBuilder) {
        if (inputs == null) {
            inputs = new ArrayList<>();
        }
        BungeeDialogInputBuilder builder = new BungeeDialogInputBuilder(key);
        inputBuilder.accept(builder);
        DialogInput dialogInput = builder.getDialogInput();
        inputs.add(dialogInput);
        return (T) this;
    }

    /**
     * Build an action button from the given action builder consumer
     *
     * @param action the consumer to configure the action builder
     * @return the built action button
     */
    protected ActionButton getAction(Consumer<BungeeDialogActionBuilder> action) {
        BungeeDialogActionBuilder actionBuilder = new BungeeDialogActionBuilder(defaultNamespace);
        action.accept(actionBuilder);
        return actionBuilder.getAction();
    }

    /**
     * Construct the Bungee dialog from the given dialog base
     *
     * @param dialogBase the dialog base containing the shared dialog data
     * @return the constructed Bungee dialog
     */
    protected abstract net.md_5.bungee.api.dialog.Dialog constructDialog(DialogBase dialogBase);

    /**
     * Get the built Bungee dialog
     *
     * @return the built Bungee dialog
     */
    public final net.md_5.bungee.api.dialog.Dialog getDialog() {
        DialogBase dialogBase = new DialogBase(
                title != null ? title : TextComponent.fromLegacy("Dialog"),
                externalTitle,
                inputs != null ? inputs : Collections.emptyList(),
                body != null ? body : Collections.emptyList(),
                canCloseWithEscape,
                pause,
                afterAction != null ? afterAction : DialogBase.AfterAction.CLOSE
        );
        return constructDialog(dialogBase);
    }

    @Override
    public O opener() {
        return openerFunction.apply(getDialog());
    }
}
