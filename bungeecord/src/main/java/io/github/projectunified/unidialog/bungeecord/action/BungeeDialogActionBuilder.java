package io.github.projectunified.unidialog.bungeecord.action;

import io.github.projectunified.unidialog.bungeecord.dialog.BungeeDialog;
import io.github.projectunified.unidialog.bungeecord.opener.BungeeDialogOpener;
import io.github.projectunified.unidialog.core.action.DialogActionBuilder;
import io.github.projectunified.unidialog.core.opener.DialogOpener;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.dialog.action.*;
import net.md_5.bungee.api.dialog.chat.ShowDialogClickEvent;
import org.jetbrains.annotations.Nullable;

public class BungeeDialogActionBuilder implements DialogActionBuilder<BungeeDialog<?, ?>, BungeeDialogActionBuilder> {
    private final String defaultNamespace;
    private BaseComponent label;
    private @Nullable BaseComponent tooltip;
    private int width;
    private Action action;

    public BungeeDialogActionBuilder(String defaultNamespace) {
        this.defaultNamespace = defaultNamespace;
    }

    /**
     * Set the label for the dialog action
     *
     * @param label the label to set
     * @return the current instance of the builder for method chaining
     */
    public BungeeDialogActionBuilder label(BaseComponent label) {
        this.label = label;
        return this;
    }

    @Override
    public BungeeDialogActionBuilder label(String label) {
        return label(TextComponent.fromLegacy(label));
    }

    /**
     * Set the tooltip for the dialog action
     *
     * @param tooltip the tooltip to set, can be null
     * @return the current instance of the builder for method chaining
     */
    public BungeeDialogActionBuilder tooltip(@Nullable BaseComponent tooltip) {
        this.tooltip = tooltip;
        return this;
    }

    @Override
    public BungeeDialogActionBuilder tooltip(@Nullable String tooltip) {
        return tooltip(tooltip == null ? null : TextComponent.fromLegacy(tooltip));
    }

    @Override
    public BungeeDialogActionBuilder width(int width) {
        this.width = width;
        return this;
    }

    private BungeeDialogActionBuilder action(Action action) {
        this.action = action;
        return this;
    }

    @Override
    public BungeeDialogActionBuilder copyToClipboard(String value) {
        return action(new StaticAction(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, value)));
    }

    @Override
    public BungeeDialogActionBuilder dynamicCustom(String id) {
        return dynamicCustom(defaultNamespace, id);
    }

    @Override
    public BungeeDialogActionBuilder dynamicCustom(String namespace, String id) {
        return action(new CustomClickAction(namespace + ":" + id));
    }

    @Override
    public BungeeDialogActionBuilder dynamicRunCommand(String template) {
        return action(new RunCommandAction(template));
    }

    @Override
    public BungeeDialogActionBuilder openUrl(String url) {
        return action(new StaticAction(new ClickEvent(ClickEvent.Action.OPEN_URL, url)));
    }

    @Override
    public BungeeDialogActionBuilder runCommand(String command) {
        return action(new StaticAction(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command)));
    }

    @Override
    public BungeeDialogActionBuilder suggestCommand(String command) {
        return action(new StaticAction(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, command)));
    }

    @Override
    public BungeeDialogActionBuilder showDialog(BungeeDialog<?, ?> dialog) {
        return action(new StaticAction(new ShowDialogClickEvent(dialog.getDialog())));
    }

    @Override
    public BungeeDialogActionBuilder showDialog(String namespace, String dialogId) {
        return action(new StaticAction(new ShowDialogClickEvent(namespace + ":" + dialogId)));
    }

    @Override
    public BungeeDialogActionBuilder showDialog(DialogOpener dialogOpener) {
        if (!(dialogOpener instanceof BungeeDialogOpener bungeeDialogOpener)) {
            throw new IllegalArgumentException("Dialog opener must be an instance of SpigotDialogOpener");
        }
        return action(new StaticAction(new ShowDialogClickEvent(bungeeDialogOpener.getDialog())));
    }

    public ActionButton getAction() {
        return new ActionButton(
                label != null ? label : new TextComponent(""),
                tooltip,
                width > 0 ? width : DEFAULT_WIDTH,
                action != null ? action : new Action() {
                    @Override
                    public int hashCode() {
                        return super.hashCode();
                    }
                }
        );
    }
}
