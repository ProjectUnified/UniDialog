package io.github.projectunified.unidialog.spigot.action;

import io.github.projectunified.unidialog.core.action.DialogActionBuilder;
import io.github.projectunified.unidialog.core.opener.DialogOpener;
import io.github.projectunified.unidialog.spigot.dialog.SpigotDialog;
import io.github.projectunified.unidialog.spigot.opener.SpigotDialogOpener;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.dialog.action.*;
import net.md_5.bungee.api.dialog.chat.ShowDialogClickEvent;
import org.jetbrains.annotations.Nullable;

public class SpigotDialogActionBuilder implements DialogActionBuilder<SpigotDialog<?>, SpigotDialogActionBuilder> {
    private final String defaultNamespace;
    private BaseComponent label;
    private @Nullable BaseComponent tooltip;
    private int width;
    private Action action;

    public SpigotDialogActionBuilder(String defaultNamespace) {
        this.defaultNamespace = defaultNamespace;
    }

    /**
     * Set the label for the dialog action
     *
     * @param label the label to set
     * @return the current instance of the builder for method chaining
     */
    public SpigotDialogActionBuilder label(BaseComponent label) {
        this.label = label;
        return this;
    }

    @Override
    public SpigotDialogActionBuilder label(String label) {
        return label(TextComponent.fromLegacy(label));
    }

    /**
     * Set the tooltip for the dialog action
     *
     * @param tooltip the tooltip to set, can be null
     * @return the current instance of the builder for method chaining
     */
    public SpigotDialogActionBuilder tooltip(@Nullable BaseComponent tooltip) {
        this.tooltip = tooltip;
        return this;
    }

    @Override
    public SpigotDialogActionBuilder tooltip(@Nullable String tooltip) {
        return tooltip(tooltip == null ? null : TextComponent.fromLegacy(tooltip));
    }

    @Override
    public SpigotDialogActionBuilder width(int width) {
        this.width = width;
        return this;
    }

    private SpigotDialogActionBuilder action(Action action) {
        this.action = action;
        return this;
    }

    @Override
    public SpigotDialogActionBuilder copyToClipboard(String value) {
        return action(new StaticAction(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, value)));
    }

    @Override
    public SpigotDialogActionBuilder dynamicCustom(String id) {
        return dynamicCustom(defaultNamespace, id);
    }

    @Override
    public SpigotDialogActionBuilder dynamicCustom(String namespace, String id) {
        return action(new CustomClickAction(namespace + ":" + id));
    }

    @Override
    public SpigotDialogActionBuilder dynamicRunCommand(String template) {
        return action(new RunCommandAction(template));
    }

    @Override
    public SpigotDialogActionBuilder openUrl(String url) {
        return action(new StaticAction(new ClickEvent(ClickEvent.Action.OPEN_URL, url)));
    }

    @Override
    public SpigotDialogActionBuilder runCommand(String command) {
        return action(new StaticAction(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command)));
    }

    @Override
    public SpigotDialogActionBuilder suggestCommand(String command) {
        return action(new StaticAction(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, command)));
    }

    @Override
    public SpigotDialogActionBuilder showDialog(SpigotDialog<?> dialog) {
        return action(new StaticAction(new ShowDialogClickEvent(dialog.getDialog())));
    }

    @Override
    public SpigotDialogActionBuilder showDialog(String namespace, String dialogId) {
        return action(new StaticAction(new ShowDialogClickEvent(namespace + ":" + dialogId)));
    }

    @Override
    public SpigotDialogActionBuilder showDialog(DialogOpener dialogOpener) {
        if (!(dialogOpener instanceof SpigotDialogOpener(net.md_5.bungee.api.dialog.Dialog dialog))) {
            throw new IllegalArgumentException("Dialog opener must be an instance of SpigotDialogOpener");
        }
        return action(new StaticAction(new ShowDialogClickEvent(dialog)));
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
