package io.github.projectunified.unidialog.core.payload;

import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.UUID;

/**
 * The payload which is sent back to the server when the player run a custom action in the dialog
 */
public interface DialogPayload {
    /**
     * Get the owner of the payload
     *
     * @return the id of the owner
     */
    UUID owner();

    /**
     * Get the text value from the payload
     *
     * @param key the key to search for
     * @return the text, or null if the key doesn't exist in the payload
     */
    @Nullable String textValue(String key);

    /**
     * Get the boolean value from the payload
     *
     * @param key the key to search for
     * @return the boolean, or null if the key doesn't exist in the payload
     */
    @Nullable Boolean booleanValue(String key);

    /**
     * Get the number value from the payload
     *
     * @param key the key to search for
     * @return the number, or null if the key doesn't exist in the payload or the value is not a number
     */
    @Nullable Number numberValue(String key);

    /**
     * Get the map of all values in the payload
     *
     * @return the map
     */
    Map<String, String> map();
}
