package io.github.projectunified.unidialog.spigot.dialog;

import io.github.projectunified.unidialog.core.dialog.Dialog;
import io.github.projectunified.unidialog.core.opener.DialogOpener;
import io.github.projectunified.unidialog.spigot.action.SpigotDialogActionBuilder;
import io.github.projectunified.unidialog.spigot.body.SpigotDialogBodyBuilder;
import io.github.projectunified.unidialog.spigot.input.SpigotDialogInputBuilder;
import io.github.projectunified.unidialog.spigot.opener.SpigotDialogOpener;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.dialog.DialogBase;
import net.md_5.bungee.api.dialog.action.ActionButton;
import net.md_5.bungee.api.dialog.body.DialogBody;
import net.md_5.bungee.api.dialog.input.DialogInput;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

@SuppressWarnings("unchecked")
public abstract class SpigotDialog<T extends SpigotDialog<T>> implements Dialog<ItemStack, SpigotDialogBodyBuilder, SpigotDialogInputBuilder, T> {
    private final String defaultNamespace;
    private BaseComponent title;
    private @Nullable BaseComponent externalTitle;
    private List<DialogInput> inputs;
    private List<DialogBody> body;
    private boolean canCloseWithEscape = true;
    private boolean pause = false;
    private DialogBase.AfterAction afterAction;

    public SpigotDialog(String defaultNamespace) {
        this.defaultNamespace = defaultNamespace;
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
    public T body(Consumer<SpigotDialogBodyBuilder> bodyBuilder) {
        if (body == null) {
            body = new ArrayList<>();
        }
        SpigotDialogBodyBuilder builder = new SpigotDialogBodyBuilder();
        bodyBuilder.accept(builder);
        DialogBody dialogBody = builder.getDialogBody();
        body.add(dialogBody);
        return (T) this;
    }

    @Override
    public T input(String key, Consumer<SpigotDialogInputBuilder> inputBuilder) {
        if (inputs == null) {
            inputs = new ArrayList<>();
        }
        SpigotDialogInputBuilder builder = new SpigotDialogInputBuilder(key);
        inputBuilder.accept(builder);
        DialogInput dialogInput = builder.getDialogInput();
        inputs.add(dialogInput);
        return (T) this;
    }

    protected ActionButton getAction(Consumer<SpigotDialogActionBuilder> action) {
        SpigotDialogActionBuilder actionBuilder = new SpigotDialogActionBuilder(defaultNamespace);
        action.accept(actionBuilder);
        return actionBuilder.getAction();
    }

    protected abstract net.md_5.bungee.api.dialog.Dialog constructDialog(DialogBase dialogBase);

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
    public SpigotDialogOpener opener() {
        return new SpigotDialogOpener(getDialog());
    }
}
