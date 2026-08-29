package io.github.projectunified.unidialog.paper.payload;

import io.github.projectunified.unidialog.core.payload.DialogPayload;
import io.papermc.paper.dialog.DialogResponseView;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.UUID;

/**
 * Payload for a dialog response on the Paper platform
 *
 * @param owner the owner of the dialog
 * @param view  the dialog response view
 */
public record PaperDialogPayload(UUID owner, @Nullable DialogResponseView view) implements DialogPayload {
    @Override
    public @Nullable String textValue(String key) {
        if (view == null) return null;
        return view.getText(key);
    }

    @Override
    public @Nullable Boolean booleanValue(String key) {
        if (view == null) return null;
        return view.getBoolean(key);
    }

    @Override
    public @Nullable Number numberValue(String key) {
        if (view == null) return null;
        return view.getFloat(key);
    }

    @Override
    public Map<String, String> map() {
        if (view == null) return Map.of();
        return PaperDialogPayloadMap.convertDialogResponseToMap(view);
    }
}
