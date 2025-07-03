package io.github.projectunified.unidialog.core.action;

import org.jetbrains.annotations.Nullable;

/**
 * Interface for building dialog actions.
 * This interface provides methods to set various properties of a dialog action,
 * such as label, tooltip, width, and different types of actions like copying to clipboard,
 * running commands, and opening URLs.
 * It is designed to be implemented by classes that build specific types of dialog actions.
 *
 * @param <T> the type of the implementing class, allowing for method chaining
 */
public interface DialogActionBuilder<T extends DialogActionBuilder<T>> {
    /**
     * Set the label for the dialog action
     *
     * @param label the label to set
     * @return the current instance of the builder for method chaining
     */
    T label(String label);

    /**
     * Set the tooltip for the dialog action
     *
     * @param tooltip the tooltip to set, can be null
     * @return the current instance of the builder for method chaining
     */
    T tooltip(@Nullable String tooltip);

    /**
     * Set the width for the dialog action
     *
     * @param width the width to set
     * @return the current instance of the builder for method chaining
     */
    T width(int width);

    /**
     * Set the action to be a copy-to-clipboard action
     *
     * @param value the value to copy to the clipboard
     * @return the current instance of the builder for method chaining
     */
    T copyToClipboard(String value);

    /**
     * Set the action to be a dynamic custom action
     *
     * @param id the identifier for the dynamic custom action
     * @return the current instance of the builder for method chaining
     */
    T dynamicCustom(String id);

    /**
     * Set the action to be a dynamic custom action with a namespace
     *
     * @param namespace the namespace for the dynamic custom action
     * @param id        the identifier for the dynamic custom action
     * @return the current instance of the builder for method chaining
     */
    T dynamicCustom(String namespace, String id);

    /**
     * Set the action to be a dynamic run command action
     *
     * @param template the command template to run
     * @return the current instance of the builder for method chaining
     */
    T dynamicRunCommand(String template);

    /**
     * Set the action to be an open-URL action
     *
     * @param url the URL to open
     * @return the current instance of the builder for method chaining
     */
    T openUrl(String url);

    /**
     * Set the action to be a run command action
     *
     * @param command the command to run
     * @return the current instance of the builder for method chaining
     */
    T runCommand(String command);

    /**
     * Set the action to be a suggest command action
     *
     * @param command the command to suggest
     * @return the current instance of the builder for method chaining
     */
    T suggestCommand(String command);
}
